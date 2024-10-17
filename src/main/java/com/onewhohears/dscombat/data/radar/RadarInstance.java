package com.onewhohears.dscombat.data.radar;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.Config;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RadarInstance<T extends RadarStats> extends JsonPresetInstance<T> {
	
	private String slotId = "";
	private Vec3 pos = Vec3.ZERO;
	private boolean freshTargets;
	private int scanTicks;
	private List<RadarPing> pings = new ArrayList<>();
	
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
		AABB radarArea = getRadarBoundingBox(radar);
		if (getStats().isScanAircraft() && (mode.canScan(RadarMode.VEHICLES) || mode.isPlayersOrBots())) {
			scanAircraft(radar, controller, vehiclePings, radarArea, mode.isPlayersOnly(), mode.isPlayersOrBots());
		}
		if (getStats().isScanPlayers() && (mode.canScan(RadarMode.PLAYERS) || mode.isPlayersOrBots())) {
			scanPlayers(radar, controller, vehiclePings, radarArea);
		}
		if (getStats().isScanMobs() && mode.canScan(RadarMode.MOBS)) {
			scanMobs(radar, controller, vehiclePings, radarArea);
		}
	}
	
	private void scanAircraft(EntityVehicle radar, Entity controller, List<RadarPing> vehiclePings, AABB radarArea, 
			boolean playersOnly, boolean isPlayersOrBots) {
		//System.out.println("SCANNING VEHICLES");
		List<Entity> list = radar.level().getEntities(radar, radarArea,
				(entity) -> entity.getType().is(ModTags.EntityTypes.VEHICLE));
		for (int i = 0; i < list.size(); ++i) {
			Entity e = list.get(i);
			EntityVehicle ev = null;
			if (e instanceof EntityVehicle vehicle) ev = vehicle;
			boolean isPlayer = false, isPlayerOrBot = false;
			if (ev == null) { 
				isPlayer = e.getControllingPassenger() instanceof Player;
				if (!isPlayer && (playersOnly || isPlayersOrBots)) continue;
			} else if (ev != null) {
				isPlayer = ev.isPlayerRiding();
				if (!isPlayer && playersOnly) continue;
				isPlayerOrBot = ev.isPlayerOrBotRiding();
				if (!isPlayerOrBot && isPlayersOrBots) continue;
			}
			double stealth = 1;
			if (ev != null) stealth = ev.getStealth();
			if (!basicCheck(radar, e, stealth)) continue;
			PingEntityType pingEntityType;
			if (isPlayer) pingEntityType = PingEntityType.VEHICLE_PLAYER;
			else if (isPlayerOrBot) pingEntityType = PingEntityType.VEHICLE_BOT;
			else pingEntityType = PingEntityType.VEHICLE;
			RadarPing p = new RadarPing(e, checkFriendly(controller, e), pingEntityType);
			vehiclePings.add(p);
			pings.add(p);
			if (ev != null && !radar.isAlliedTo(ev)) ev.lockedOnto(radar);
		}
	}
	
	private void scanPlayers(EntityVehicle radar, Entity controller, List<RadarPing> vehiclePings, AABB radarArea) {
		//System.out.println("SCANNING PLAYERS");
		List<Player> list = radar.level().getEntitiesOfClass(Player.class, radarArea);
		for (int i = 0; i < list.size(); ++i) {
			Player target = list.get(i);
			if (target.isPassenger() && target.getRootVehicle().getType().is(ModTags.EntityTypes.VEHICLE)) continue;
			if (!basicCheck(radar, target, 1)) continue;
			RadarPing p = new RadarPing(target, 
					checkFriendly(controller, target),
					PingEntityType.PLAYER);
			vehiclePings.add(p);
			pings.add(p);
		}
	}
	
	private void scanMobs(EntityVehicle radar, Entity controller, List<RadarPing> vehiclePings, AABB radarArea) {
		//System.out.println("SCANNING MOBS");
		for (int j = 0; j < RadarTargetTypes.get().getRadarMobClasses().size(); ++j) {
			Class<? extends Entity> clazz = RadarTargetTypes.get().getRadarMobClasses().get(j);
			List<? extends Entity> list = radar.level().getEntitiesOfClass(clazz, radarArea);
			for (int i = 0; i < list.size(); ++i) {
				if (list.get(i).isPassenger()) continue;
				if (!basicCheck(radar, list.get(i), 1)) continue;
				RadarPing p = new RadarPing(list.get(i), 
						checkFriendly(controller, list.get(i)), 
						PingEntityType.FRIENDLY_MOB);
				vehiclePings.add(p);
				pings.add(p);
			}
		}
	}
	
	private boolean checkFriendly(Entity controller, Entity target) {
		if (target == null) return false;
		if (controller == null) return false;
		return target.isAlliedTo(controller);
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
		if (getStats().isScanAir() && !groundWater) return true;
		return false;
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
		double area = UtilVehicleEntity.getCrossSectionalArea(target) * stealth;
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
