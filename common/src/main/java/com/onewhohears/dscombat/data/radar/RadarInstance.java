package com.onewhohears.dscombat.data.radar;

import java.util.*;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import com.onewhohears.onewholibs.common.core.DistantRayCastManager;
import com.onewhohears.onewholibs.common.core.SimulatedEntityManager;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.radar.RadarStats.PingEntityType;
import com.onewhohears.dscombat.data.radar.RadarStats.RadarMode;
import com.onewhohears.dscombat.data.radar.RadarStats.RadarPing;
import com.onewhohears.dscombat.data.weapon.RadarTargetTypes;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.math.UtilGeometry;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class RadarInstance<T extends RadarStats> extends JsonPresetInstance<T> {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final long RAY_CAST_TIMEOUT = 1000;

    private String slotId = "";
	private Vec3 pos = Vec3.ZERO;
	private boolean freshTargets;
	private int scanTicks;
	private final IntObjectMap<TimedPing> pings = new IntObjectHashMap<>();
    private final Set<Integer> forRemoval = new HashSet<>();
	
	public RadarInstance(T stats) {
		super(stats);
	}
	
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
		setSlot(tag.getString("slotId"));
	}
	
	@Override
	public CompoundTag writeNBT() {
		CompoundTag tag = super.writeNBT();
		tag.putString("slotId", slotId);
		return tag;
	}
	
	private int maxCheckDist = 150;
	
	public void resetPings(List<RadarPing> vehiclePings, long currentTime) {
        pings.forEach((id, ping) -> {
            long timeDiff = currentTime - ping.gameTime();
            if (timeDiff > Math.max(getStats().getScanRate()+10, 20)) {
                removePing(vehiclePings, id);
            }
        });
        forRemoval.forEach(pings::remove);
        forRemoval.clear();
	}
	
	public void tickUpdateTargets(EntityVehicle radar, List<RadarPing> vehiclePings) {
		if (radar.getWorld().isClientSide()) return;
		if (scanTicks > getStats().getScanRate()) scanTicks = 0;
		else {
			++scanTicks;
			freshTargets = false;
			return;
		}
		maxCheckDist = Config.COMMON.maxBlockCheckDepth.get();
		resetPings(vehiclePings, UtilEntity.getLevel(radar).getGameTime());
		freshTargets = true;
		Entity controller = radar.getControllingPlayerOrBot();
		RadarMode mode = radar.getRadarMode();
		if (mode.isOff()) return;
		AABB radarArea = getRadarBoundingBox(radar);
		double rangeSqr = getStats().getRange()*getStats().getRange();
		if (getStats().isScanPlayers() && (mode.isPlayersOrBots() || mode.canScan(RadarMode.VEHICLES))) {
			scanPlayersVehicles(radar, controller, vehiclePings, rangeSqr,
					mode.isPlayersOnly(), mode.isVehiclesOnly());
		}
		if (getStats().isScanMobs() && mode.canScan(RadarMode.MOBS)) {
			scanMobs(radar, controller, vehiclePings, radarArea);
		}
		if (getStats().isScanMissiles() && mode.isOn()) {
			scanMissiles(radar, controller, vehiclePings, rangeSqr);
		}
	}

	private void scanPlayersVehicles(EntityVehicle radar, Entity controller, List<RadarPing> vehiclePings,
									 double rangeSqr, boolean playersOnly, boolean vehiclesOnly) {
		MinecraftServer server = radar.getWorld().getServer();
		if (server == null) return;
		List<ServerPlayer> players = server.getPlayerList().getPlayers();
		for (ServerPlayer player : players) {
			handleScanPlayerVehicle(radar, controller, vehiclePings, rangeSqr, playersOnly,
					vehiclesOnly, player, true);
		}
		Collection<Entity> entities = TrackableEntitiesManager.getTrackableEntities();
		for (Entity entity : entities) {
			handleScanPlayerVehicle(radar, controller, vehiclePings, rangeSqr, playersOnly,
					vehiclesOnly, entity, false);
		}
	}

	private void handleScanPlayerVehicle(EntityVehicle radar, Entity controller, List<RadarPing> vehiclePings,
										 double rangeSqr, boolean playersOnly, boolean vehiclesOnly,
										 Entity entity, boolean isTargetPlayer) {
		if (entity.isSpectator()) return;
		if (playersOnly && !isTargetPlayer) return;

		if (entity.distanceToSqr(radar) > rangeSqr) return;
		if (!UtilEntity.getLevel(entity).dimension().equals(radar.getWorld().dimension())) return;

		EntityVehicle vehicle = toTargetVehicle(entity, isTargetPlayer);
		if (vehiclesOnly && vehicle == null) return;

		@NotNull Entity pingEntity = vehicle != null ? vehicle : entity;
		if (!isTargetPlayer) {
			if (alreadyScanned(vehiclePings, pingEntity)) return;
			if (vehicle == null) {
				if (entity.getRootVehicle().getType().is(ModTags.EntityTypes.VEHICLE))
					pingEntity = entity.getRootVehicle();
			}
		}

		double stealth = 1;
		if (vehicle != null) stealth = vehicle.getStealth();
		if (isFailBasicCheck(radar, pingEntity, stealth, false)) return;

        DistantRayCastManager.distantRayCast((ServerLevel) UtilEntity.getLevel(radar), radar, pingEntity,
                (event) -> {
                    @NotNull EntityVehicle radarVehicle;
                    @NotNull Entity targetEntity;
                    if (event.completeId() == event.eyeEntity().getId() && event.eyeEntity() instanceof EntityVehicle v) {
                        radarVehicle = v;
                        targetEntity = event.targetEntity();
                    } else if (event.completeId() == event.targetEntity().getId() && event.targetEntity() instanceof EntityVehicle v) {
                        radarVehicle = v;
                        targetEntity = event.eyeEntity();
                    } else {
                        LOGGER.error("Neither the eye or target entities are the radar {} {}", event.eyeEntity(), event.targetEntity());
                        return;
                    }
                    if (!event.pass()) {
                        removePing(vehiclePings, targetEntity);
                        return;
                    }
                    @Nullable Entity controllerEntity = radarVehicle.getControllingPlayerOrBot();
                    @Nullable EntityVehicle targetVehicle = targetEntity instanceof EntityVehicle v ? v : null;

                    PingEntityType pingEntityType = getPingEntityType(isTargetPlayer, targetVehicle, targetEntity);

                    RadarPing p = new RadarPing(targetEntity, checkFriendly(controllerEntity, targetEntity), pingEntityType);
                    putPing(vehiclePings, p);
                    pings.put(p.id, new TimedPing(p, event.level().getGameTime()));

                    if (targetVehicle != null && !radarVehicle.isAlliedTo(targetVehicle)) targetVehicle.lockedOnto(radarVehicle);
                }, radar.getId(), RAY_CAST_TIMEOUT, Math.max(getStats().getScanRate() * 50L + 100, 1000),
                getStats().getThroWaterRange()+1, getStats().getThroGroundRange());
	}

    private static void putPing(@NotNull List<RadarPing> vehiclePings, @NotNull RadarPing ping) {
        for (int i = 0; i < vehiclePings.size(); ++i) {
            if (vehiclePings.get(i).id == ping.id) {
                vehiclePings.set(i, ping);
                return;
            }
        }
        vehiclePings.add(ping);
    }

    private void removePing(@NotNull List<RadarPing> vehiclePings, @NotNull Entity targetEntity) {
        removePing(vehiclePings, targetEntity.getId());
    }

    private void removePing(@NotNull List<RadarPing> vehiclePings, int targetEntityId) {
        forRemoval.add(targetEntityId);
        for (int i = 0; i < vehiclePings.size(); ++i) {
            if (vehiclePings.get(i).id == targetEntityId) {
                vehiclePings.remove(i--);
            }
        }
    }

    private static @NotNull PingEntityType getPingEntityType(boolean player, @Nullable EntityVehicle targetVehicle,
                                                             @NotNull Entity targetEntity) {
        PingEntityType pingEntityType;
        if (player) {
            if (targetVehicle != null || targetEntity.hasControllingPassenger())
                pingEntityType = PingEntityType.VEHICLE_PLAYER;
            else pingEntityType = PingEntityType.PLAYER;
        } else {
            if (targetVehicle != null || targetEntity.hasControllingPassenger())
                pingEntityType = PingEntityType.VEHICLE_BOT;
            else pingEntityType = PingEntityType.HOSTILE_MOB;
        }
        return pingEntityType;
    }

    @Nullable
    private EntityVehicle toTargetVehicle(Entity entity, boolean player) {
        if (!player && entity instanceof EntityVehicle ev) return ev;
        else if (entity.getRootVehicle() instanceof EntityVehicle ev) return ev;
        return null;
    }

	private boolean alreadyScanned(List<RadarPing> vehiclePings, Entity entity) {
		for (RadarPing ping : vehiclePings)
			if (ping.id == entity.getId())
				return true;
		return false;
	}
	
	private void scanMobs(EntityVehicle radar, Entity controller, List<RadarPing> vehiclePings, AABB radarArea) {
		//System.out.println("SCANNING MOBS");
		for (int j = 0; j < RadarTargetTypes.get().getRadarMobClasses().size(); ++j) {
			Class<? extends Entity> clazz = RadarTargetTypes.get().getRadarMobClasses().get(j);
			List<? extends Entity> list = radar.getWorld().getEntitiesOfClass(clazz, radarArea);
            for (Entity entity : list) {
                if (entity.isPassenger()) continue;
                if (isFailBasicCheck(radar, entity, 1, true)) continue;
                RadarPing p = new RadarPing(entity,
                        checkFriendly(controller, entity),
                        PingEntityType.FRIENDLY_MOB);
                putPing(vehiclePings, p);
                pings.put(p.id, new TimedPing(p, UtilEntity.getLevel(radar).getGameTime()));
            }
		}
	}

	private void scanMissiles(EntityVehicle radar, Entity controller, List<RadarPing> vehiclePings, double rangeSqr) {
		Collection<Entity> list = TrackableEntitiesManager.getTrackableEntities();
        for (Entity target : list) {
			if (!target.getType().is(ModTags.EntityTypes.MISSILE)) continue;
			handleMissile(radar, controller, vehiclePings, rangeSqr, target);
        }
        List<EntityMissile> missiles = SimulatedEntityManager.get().getAllOfClass(EntityMissile.class,
                EntityMissile::isUnloaded);
        for (EntityMissile target : missiles) {
			handleMissile(radar, controller, vehiclePings, rangeSqr, target);
		}
	}

	private void handleMissile(EntityVehicle radar, Entity controller, List<RadarPing> vehiclePings,
							   double rangeSqr, Entity target) {
		if (target.distanceToSqr(radar) > rangeSqr) return;
		if (!UtilEntity.getLevel(target).dimension().equals(radar.getWorld().dimension())) return;
		if (isFailBasicCheck(radar, target, -1, true)) return;
		RadarPing p = new RadarPing(target,
				checkFriendly(controller, target),
				PingEntityType.MISSILE);
        putPing(vehiclePings, p);
        pings.put(p.id, new TimedPing(p, UtilEntity.getLevel(radar).getGameTime()));
	}
	
	private boolean checkFriendly(Entity controller, Entity target) {
		if (target == null) return false;
		if (controller == null) return false;
		return UtilEntity.areEntitiesAllied(target, controller);
	}
	
	private boolean isFailBasicCheck(EntityVehicle radar, Entity ping, double stealth, boolean checkCanSee) {
		//System.out.println("RADAR CHECK "+ping);
		if (radar.equals(ping)) return true;
		//System.out.println("not equal");
		if (!groundCheck(ping)) return true;
		//System.out.println("passed ground check");
		if (radar.isVehicleOf(ping)) return true;
		//System.out.println("not a vehicle of ping");
		if (!checkTargetRange(radar, ping, stealth)) return true;
		//System.out.println("passed target range check");
        if (!checkCanSee) return false;
        return !checkCanSee(radar, ping);
    }
	
	private boolean groundCheck(Entity ping) {
		if (getStats().getThroWaterRange() > 0 && ping.isInWater()) return true;
		boolean groundWater = UtilVehicleEntity.isOnGroundOrWater(ping);
		if (getStats().isScanGround() && groundWater) return true;
		return getStats().isScanAir() && !groundWater && UtilVehicleEntity.getDistFromGround(ping, 6, false) >= 6;
	}
	
	private boolean checkTargetRange(Entity radar, Entity target, double stealth) {
		float dist = radar.distanceTo(target);
		//System.out.println("dist = "+dist+" range = "+range);
		if (getStats().getFov() == -1) {
			if (dist > getStats().getRange()) {
				//System.out.println("out of range");
				return false;
			} 
		} else if (!UtilGeometry.isPointInsideCone(
				target.position(), 
				radar.position().add(pos),
				radar.getLookAngle(), 
				getStats().getFov(), getStats().getRange())) {
			//System.out.println("not in cone");
			return false;
		}
		if (stealth == -1) return true;
		double area = UtilVehicleEntity.getRadarCrossSectionalArea(target, radar.position()) * stealth;
		double areaMin = (1-Math.pow(getStats().getRange(),-2)*Math.pow(dist-getStats().getRange(),2))*getStats().getSensitivity();
		//System.out.println("area = "+area+" min = "+areaMin);
		return area >= areaMin;
	}
	
	private boolean checkCanSee(Entity radar, Entity target) {
		// throWaterRange+0.5 is needed for ground radar to see boats in water
		return UtilEntity.canPosSeeEntity(radar.position().add(pos), target, maxCheckDist, 
				getStats().getThroWaterRange()+1, getStats().getThroGroundRange());
	}
	
	private AABB getRadarBoundingBox(Entity radar) {
		double x = radar.getX()+pos.x;
		double y = radar.getY()+pos.y;
		double z = radar.getZ()+pos.z;
		double w = getStats().getRange();
		return new AABB(x+w, y+w, z+w, x-w, y-w, z-w);
	}
	
	public boolean isFreshTargets() {
		return freshTargets;
	}
	
	public String getSlotId() {
		return slotId;
	}
	
	public boolean isInternal() {
		return Objects.equals(slotId, "");
	}
	
	public void setSlot(String slotId) {
		this.slotId = slotId;
	}
	
	public void setInternal() {
		this.slotId = "";
	}
	
	public void setPos(Vec3 pos) {
		this.pos = pos;
	}
	
	public boolean idMatch(String id, String slotId) {
		if (slotId == null) return false;
		if (id == null) return false;
		return getStatsId().equals(id) && slotId.equals(this.slotId);
	}

    public record TimedPing(RadarPing ping, long gameTime) {}

}
