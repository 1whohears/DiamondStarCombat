package com.onewhohears.dscombat.data.radar;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.weapon.RadarTargetTypes;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import com.onewhohears.onewholibs.common.core.DistantVisibleManager;
import com.onewhohears.onewholibs.common.core.SimulatedEntityManager;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;

public class RadarInstance<T extends RadarStats> extends JsonPresetInstance<T> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Set<Integer> forRemoval = new HashSet<>();

    private String slotId = "";
	private Vec3 pos = Vec3.ZERO;
	private boolean freshTargets;
	private int scanTicks;
	
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
	
	public void resetPings(EntityVehicle radar) {
        forRemoval.forEach(radar.radarSystem::removeTarget);
        forRemoval.clear();
	}

    public int getPingTimeOut() {
        return Math.max(getStats().getScanRate()*2, 40);
    }
	
	public void tickUpdateTargets(EntityVehicle radar, IntObjectMap<RadarTarget> vehiclePings) {
		if (radar.getWorld().isClientSide()) return;
		if (scanTicks > getStats().getScanRate()) scanTicks = 0;
		else {
			++scanTicks;
			freshTargets = false;
			return;
		}
		maxCheckDist = Config.COMMON.maxBlockCheckDepth.get();
		resetPings(radar);
		freshTargets = true;
		Entity controller = radar.getControllingPlayerOrBot();
		RadarFilterMode mode = radar.getRadarMode();
		if (mode.isOff()) return;
		AABB radarArea = getRadarBoundingBox(radar);
		double rangeSqr = getStats().getRange()*getStats().getRange();
		if (getStats().isScanPlayers() && (mode.isPlayersOrBots() || mode.canScan(RadarFilterMode.VEHICLES))) {
			scanPlayersVehicles(radar, controller, rangeSqr,
					mode.isPlayersOnly(), mode.isVehiclesOnly());
		}
		if (getStats().isScanMobs() && mode.canScan(RadarFilterMode.MOBS)) {
			scanMobs(radar, controller, radarArea);
		}
		if (getStats().isScanMissiles() && mode.isOn()) {
			scanMissiles(radar, controller, rangeSqr);
		}
	}

	private void scanPlayersVehicles(EntityVehicle radar, Entity controller,
									 double rangeSqr, boolean playersOnly, boolean vehiclesOnly) {
		MinecraftServer server = radar.getWorld().getServer();
		if (server == null) return;
		List<ServerPlayer> players = server.getPlayerList().getPlayers();
		for (ServerPlayer player : players) {
			handleScanPlayerVehicle(radar, controller, rangeSqr, playersOnly,
					vehiclesOnly, player, true);
		}
		Collection<Entity> entities = TrackableEntitiesManager.getTrackableEntities();
		for (Entity entity : entities) {
			handleScanPlayerVehicle(radar, controller, rangeSqr, playersOnly,
					vehiclesOnly, entity, false);
		}
	}

	private void handleScanPlayerVehicle(EntityVehicle radar, Entity controller,
										 double rangeSqr, boolean playersOnly, boolean vehiclesOnly,
										 Entity entity, boolean isTargetPlayer) {
        if (radar.getServer() == null || entity.isSpectator()) {
			cancelVisibleQuery(radar, entity);
			return;
		}
		if (playersOnly && !isTargetPlayer) {
			cancelVisibleQuery(radar, entity);
			return;
		}

		if (entity.distanceToSqr(radar) > rangeSqr) {
			cancelVisibleQuery(radar, entity);
			return;
		}
		if (!UtilEntity.getLevel(entity).dimension().equals(radar.getWorld().dimension())) {
			cancelVisibleQuery(radar, entity);
			return;
		}

		EntityVehicle vehicle = toTargetVehicle(entity, isTargetPlayer);
		if (vehiclesOnly && vehicle == null) {
			cancelVisibleQuery(radar, entity);
			return;
		}

		@NotNull Entity pingEntity = vehicle != null ? vehicle : entity;
		if (!isTargetPlayer) {
			//if (alreadyScanned(vehiclePings, pingEntity)) return;
			if (vehicle == null) {
				if (entity.getRootVehicle().getType().is(ModTags.EntityTypes.VEHICLE))
					pingEntity = entity.getRootVehicle();
			}
		}

		double stealth = 1;
		if (vehicle != null) stealth = vehicle.getStealth();
		if (isFailBasicCheck(radar, pingEntity, stealth, false)) {
			cancelVisibleQuery(radar, pingEntity);
			return;
		}

        DistantVisibleManager.queryVisible(radar.getServer(), radar, pingEntity, RADAR_SCAN_HANDLER);
	}

	private void cancelVisibleQuery(EntityVehicle radar, Entity target) {
		removePing(target);
		DistantVisibleManager.cancelFirstEntityQuery(radar.getId(), target.getId(), RADAR_SCAN_HANDLER.typeId());
	}

    // TODO bring back getStats().getThroWaterRange() and getStats().getThroGroundRange()
	private static final int REQUEST_ID = 0x2401;
    public final DistantVisibleManager.VisibleRequestData RADAR_SCAN_HANDLER = new DistantVisibleManager.VisibleRequestData(
			REQUEST_ID, getPingTimeOut(), getStats().getScanRate(), event -> {
        if (!event.result().computeComplete || event.result().failed) {
			DistantVisibleManager.cancelFirstEntityQuery(event.data().entityId1, event.data().entityId2, REQUEST_ID);
            return;
        }
        @NotNull EntityVehicle radarVehicle;
        @NotNull Entity targetEntity;
        if (event.entity1() instanceof EntityVehicle ev) {
            radarVehicle = ev;
            targetEntity = event.entity2();
        }else {
			DistantVisibleManager.cancelFirstEntityQuery(event.data().entityId1, event.data().entityId2, REQUEST_ID);
            LOGGER.error("Radar Visible Check Failed. Entity 1 is not a vehicle {} {}", event.entity1(), event.entity2());
            return;
        }

        //LOGGER.info("RADAR VISIBLE RESULT {} {} {} {}", event.result(), event.approxObstructPos(), event.entity1(), event.entity2());
        if (!event.result().passed || targetEntity.getRemovalReason() == Entity.RemovalReason.KILLED) {
            removePing(targetEntity);
            return;
        }

        @Nullable Entity controllerEntity = radarVehicle.getControllingPlayerOrBot();
        @Nullable EntityVehicle targetVehicle = targetEntity instanceof EntityVehicle v ? v : null;

        PingEntityType pingEntityType = getPingEntityType(UtilEntity.isPlayer(targetEntity), targetVehicle, targetEntity);

        RadarTarget p = new RadarTarget(targetEntity, checkFriendly(controllerEntity, targetEntity), pingEntityType, getPingTimeOut());
        putPing(radarVehicle, p);

        if (targetVehicle != null && !radarVehicle.isAlliedTo(targetVehicle)) targetVehicle.lockedOnto(radarVehicle);
    });

    private void putPing(@NotNull EntityVehicle radar, @NotNull RadarTarget target) {
        radar.radarSystem.addUpdateTarget(target);
        forRemoval.remove(target.entityId);
    }

    private void removePing(@NotNull Entity targetEntity) {
        removePing(targetEntity.getId());
    }

    private void removePing(int targetEntityId) {
        forRemoval.add(targetEntityId);
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

	/*private boolean alreadyScanned(List<RadarPing> vehiclePings, Entity entity) {
		for (RadarPing ping : vehiclePings)
			if (ping.id == entity.getId())
				return true;
		return false;
	}*/
	
	private void scanMobs(EntityVehicle radar, Entity controller, AABB radarArea) {
		//System.out.println("SCANNING MOBS");
		for (int j = 0; j < RadarTargetTypes.get().getRadarMobClasses().size(); ++j) {
			Class<? extends Entity> clazz = RadarTargetTypes.get().getRadarMobClasses().get(j);
			List<? extends Entity> list = radar.getWorld().getEntitiesOfClass(clazz, radarArea);
            for (Entity entity : list) {
                if (entity.isPassenger()) continue;
                if (isFailBasicCheck(radar, entity, 1, true)) continue;
                RadarTarget p = new RadarTarget(entity,
                        checkFriendly(controller, entity),
                        PingEntityType.FRIENDLY_MOB, getPingTimeOut());
                putPing(radar, p);
            }
		}
	}

	private void scanMissiles(EntityVehicle radar, Entity controller, double rangeSqr) {
		Collection<Entity> list = TrackableEntitiesManager.getTrackableEntities();
        for (Entity target : list) {
			if (!target.getType().is(ModTags.EntityTypes.MISSILE)) continue;
			handleMissile(radar, controller, rangeSqr, target);
        }
        List<EntityMissile> missiles = SimulatedEntityManager.get().getAllOfClass(EntityMissile.class,
                EntityMissile::isUnloaded);
        for (EntityMissile target : missiles) {
			handleMissile(radar, controller, rangeSqr, target);
		}
	}

	private void handleMissile(EntityVehicle radar, Entity controller,
							   double rangeSqr, Entity target) {
		if (target.distanceToSqr(radar) > rangeSqr) return;
		if (!UtilEntity.getLevel(target).dimension().equals(radar.getWorld().dimension())) return;
		if (isFailBasicCheck(radar, target, -1, true)) return;
		RadarTarget p = new RadarTarget(target,
				checkFriendly(controller, target),
				PingEntityType.MISSILE, getPingTimeOut());
        putPing(radar, p);
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
		if (ping.getRemovalReason() == Entity.RemovalReason.KILLED) return true;
		//System.out.println("not dead");
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
		if (Math.abs(radar.position().y - target.position().y) > getStats().getVerticalRange()) return false;
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

    public record TimedPing(RadarTarget ping, long gameTime) {}

}
