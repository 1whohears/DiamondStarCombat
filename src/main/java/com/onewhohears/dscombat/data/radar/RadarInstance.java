package com.onewhohears.dscombat.data.radar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.weapon.NonTickingMissileManager;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.radar.RadarStats.PingEntityType;
import com.onewhohears.dscombat.data.radar.RadarStats.RadarMode;
import com.onewhohears.dscombat.data.radar.RadarStats.RadarPing;
import com.onewhohears.dscombat.data.weapon.RadarTargetTypes;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.math.UtilGeometry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RadarInstance<T extends RadarStats> extends JsonPresetInstance<T> {
	
	private String slotId = "";
	private Vec3 pos = Vec3.ZERO;
	private boolean freshTargets;
	private int scanTicks;
	private final List<RadarPing> pings = new ArrayList<>();
	
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
	
	public void resetPings(List<RadarPing> vehiclePings) {
		for (int i = 0; i < pings.size(); ++i) vehiclePings.remove(pings.get(i));
		pings.clear();
	}
	
	public void tickUpdateTargets(EntityVehicle radar, List<RadarPing> vehiclePings) {
		if (radar.getLevel().isClientSide()) return;
		if (scanTicks > getStats().getScanRate()) scanTicks = 0;
		else {
			++scanTicks;
			freshTargets = false;
			return;
		}
		maxCheckDist = Config.COMMON.maxBlockCheckDepth.get();
		resetPings(vehiclePings);
		freshTargets = true;
		Entity controller = radar.getControllingPlayerOrBot();
		RadarMode mode = radar.getRadarMode();
		if (mode.isOff()) return;
		AABB radarArea = getRadarBoundingBox(radar);
		double rangeSqr = getStats().getRange()*getStats().getRange();
		if (getStats().isScanPlayers() && (mode.isPlayersOrBots() || mode.canScan(RadarMode.VEHICLES))) {
			scanPlayersVehicles(radar, controller, vehiclePings, rangeSqr,
					mode.isPlayersOnly(), mode.isPlayersOrBots(), mode.isVehiclesOnly());
		}
		if (getStats().isScanMobs() && mode.canScan(RadarMode.MOBS)) {
			scanMobs(radar, controller, vehiclePings, radarArea);
		}
		if (getStats().isScanMissiles() && mode.isOn()) {
			scanMissiles(radar, controller, vehiclePings, rangeSqr);
		}
	}

	private void scanPlayersVehicles(EntityVehicle radar, Entity controller, List<RadarPing> vehiclePings,
									 double rangeSqr, boolean playersOnly, boolean isPlayersOrBots, boolean vehiclesOnly) {
		MinecraftServer server = radar.getLevel().getServer();
		if (server == null) return;
		List<ServerPlayer> players = server.getPlayerList().getPlayers();
		for (ServerPlayer player : players) {
			handleScanPlayerVehicle(radar, controller, vehiclePings, rangeSqr, playersOnly,
					isPlayersOrBots, vehiclesOnly, player, true);
		}
		Collection<Entity> entities = TrackableEntitiesManager.getTrackableEntities();
		for (Entity entity : entities) {
			handleScanPlayerVehicle(radar, controller, vehiclePings, rangeSqr, playersOnly,
					isPlayersOrBots, vehiclesOnly, entity, false);
		}
	}

	private void handleScanPlayerVehicle(EntityVehicle radar, Entity controller, List<RadarPing> vehiclePings,
										 double rangeSqr, boolean playersOnly, boolean isPlayersOrBots, boolean vehiclesOnly,
										 Entity entity, boolean player) {
		if (playersOnly && !player) return;

		if (entity.distanceToSqr(radar) > rangeSqr) return;
		if (!entity.getLevel().dimension().equals(radar.getLevel().dimension())) return;

		EntityVehicle vehicle = null;
		if (!player && entity instanceof EntityVehicle ev) vehicle = ev;
		else if (entity.getRootVehicle() instanceof EntityVehicle ev) vehicle = ev;
		if (vehiclesOnly && vehicle == null) return;

		@NotNull Entity pingEntity = vehicle != null ? vehicle : entity;
		if (!player) {
			if (alreadyScanned(vehiclePings, pingEntity)) return;
			if (vehicle == null) {
				if (entity.getRootVehicle().getType().is(ModTags.EntityTypes.VEHICLE))
					pingEntity = entity.getRootVehicle();
				else return;
			}
		}

		double stealth = 1;
		if (vehicle != null) stealth = vehicle.getStealth();
		if (!basicCheck(radar, pingEntity, stealth)) return;

		PingEntityType pingEntityType;
		if (player) {
			if (vehicle != null || pingEntity.getId() != entity.getId())
				pingEntityType = PingEntityType.VEHICLE_PLAYER;
			else pingEntityType = PingEntityType.PLAYER;
		} else {
			if (vehicle != null || pingEntity.getId() != entity.getId())
				pingEntityType = PingEntityType.VEHICLE_BOT;
			else pingEntityType = PingEntityType.HOSTILE_MOB;
		}

		RadarPing p = new RadarPing(pingEntity, checkFriendly(controller, pingEntity), pingEntityType);
		vehiclePings.add(p);
		pings.add(p);

		if (vehicle != null && !radar.isAlliedTo(vehicle)) vehicle.lockedOnto(radar);
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
			List<? extends Entity> list = radar.level.getEntitiesOfClass(clazz, radarArea);
            for (Entity entity : list) {
                if (entity.isPassenger()) continue;
                if (!basicCheck(radar, entity, 1)) continue;
                RadarPing p = new RadarPing(entity,
                        checkFriendly(controller, entity),
                        PingEntityType.FRIENDLY_MOB);
                vehiclePings.add(p);
                pings.add(p);
            }
		}
	}

	private void scanMissiles(EntityVehicle radar, Entity controller, List<RadarPing> vehiclePings, double rangeSqr) {
		Collection<Entity> list = TrackableEntitiesManager.getTrackableEntities();
        for (Entity target : list) {
			if (!target.getType().is(ModTags.EntityTypes.MISSILE)) continue;
			handleMissile(radar, controller, vehiclePings, rangeSqr, target);
        }
		for (EntityMissile<?> target : NonTickingMissileManager.getMissiles()) {
			handleMissile(radar, controller, vehiclePings, rangeSqr, target);
		}
	}

	private void handleMissile(EntityVehicle radar, Entity controller, List<RadarPing> vehiclePings,
							   double rangeSqr, Entity target) {
		if (target.distanceToSqr(radar) > rangeSqr) return;
		if (!target.getLevel().dimension().equals(radar.getLevel().dimension())) return;
		if (!basicCheck(radar, target, -1)) return;
		RadarPing p = new RadarPing(target,
				checkFriendly(controller, target),
				PingEntityType.MISSILE);
		vehiclePings.add(p);
		pings.add(p);
	}
	
	private boolean checkFriendly(Entity controller, Entity target) {
		if (target == null) return false;
		if (controller == null) return false;
		return UtilEntity.areEntitiesAllied(target, controller);
	}
	
	private boolean basicCheck(EntityVehicle radar, Entity ping, double stealth) {
		//System.out.println("RADAR CHECK "+ping);
		if (radar.equals(ping)) return false;
		//System.out.println("not equal");
		if (!groundCheck(ping)) return false;
		//System.out.println("passed ground check");
		if (radar.isVehicleOf(ping)) return false;
		//System.out.println("not a vehicle of ping");
		if (!checkTargetRange(radar, ping, stealth)) return false;
		//System.out.println("passed target range check");
		if (!checkCanSee(radar, ping)) return false;
		//System.out.println("passed can see check");
		return true;
	}
	
	private boolean groundCheck(Entity ping) {
		if (getStats().getThroWaterRange() > 0 && ping.isInWater()) return true;
		boolean groundWater = UtilVehicleEntity.isOnGroundOrWater(ping);
		if (getStats().isScanGround() && groundWater) return true;
		return getStats().isScanAir() && !groundWater && UtilVehicleEntity.getDistFromGround(ping, 6) >= 6;
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
		return slotId == "";
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
		return getStatsId().equals(id) && slotId.equals(slotId);
	}

}
