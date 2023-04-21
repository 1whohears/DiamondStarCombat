package com.onewhohears.dscombat.data.radar;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.UtilParse;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RadarData {
	
	private String id;
	private Vec3 pos;
	private double range;
	private double sensitivity;
	private double fov;
	private int scanRate;
	private boolean scanAircraft;
	private boolean scanPlayers;
	private boolean scanMobs;
	private boolean scanGround;
	private boolean scanAir;
	private double throWaterRange;
	private double throGroundRange;
	private String slotId = "";
	
	private boolean freshTargets;
	private int scanTicks;
	private List<RadarPing> pings = new ArrayList<RadarPing>();
	
	public RadarData(CompoundTag tag) {
		id = tag.getString("id");
		pos = new Vec3(0, 0, 0);
		range = tag.getDouble("range");
		sensitivity = UtilParse.fixFloatNbt(tag, "sensitivity", 1);
		fov = tag.getDouble("fov");
		scanRate = tag.getInt("scanRate");
		scanAircraft = tag.getBoolean("scanAircraft");
		scanPlayers = tag.getBoolean("scanPlayers");
		scanMobs = tag.getBoolean("scanMobs");
		scanGround = tag.getBoolean("scanGround");
		scanAir = tag.getBoolean("scanAir");
		throWaterRange = tag.getDouble("throWaterRange");
		throGroundRange = tag.getDouble("throGroundRange");
		slotId = tag.getString("slotId");
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putString("id", id);
		// pos
		tag.putDouble("range", range);
		tag.putDouble("sensitivity", sensitivity);
		tag.putDouble("fov", fov);
		tag.putInt("scanRate", scanRate);
		tag.putBoolean("scanAircraft", scanAircraft);
		tag.putBoolean("scanPlayers", scanPlayers);
		tag.putBoolean("scanMobs", scanMobs);
		tag.putBoolean("scanGround", scanGround);
		tag.putBoolean("scanAir", scanAir);
		tag.putDouble("throWaterRange", throWaterRange);
		tag.putDouble("throGroundRange", throGroundRange);
		tag.putString("slotId", slotId);
		return tag;
	}
	
	public RadarData(FriendlyByteBuf buffer) {
		id = buffer.readUtf();
		pos = new Vec3(0, 0, 0);
		range = buffer.readDouble();
		sensitivity = buffer.readDouble();
		fov = buffer.readDouble();
		scanRate = buffer.readInt();
		scanAircraft = buffer.readBoolean();
		scanPlayers = buffer.readBoolean();
		scanMobs = buffer.readBoolean();
		scanGround = buffer.readBoolean();
		scanAir = buffer.readBoolean();
		throWaterRange = buffer.readDouble();
		throGroundRange = buffer.readDouble();
		slotId = buffer.readUtf();
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUtf(id);
		// pos
		buffer.writeDouble(range);
		buffer.writeDouble(sensitivity);
		buffer.writeDouble(fov);
		buffer.writeInt(scanRate);
		buffer.writeBoolean(scanAircraft);
		buffer.writeBoolean(scanPlayers);
		buffer.writeBoolean(scanMobs);
		buffer.writeBoolean(scanGround);
		buffer.writeBoolean(scanAir);
		buffer.writeDouble(throWaterRange);
		buffer.writeDouble(throGroundRange);
		buffer.writeUtf(slotId);
	}
	
	public void tickUpdateTargets(EntityAircraft radar, List<RadarPing> targets) {
		if (scanTicks > scanRate) scanTicks = 0;
		else {
			++scanTicks;
			freshTargets = false;
			return;
		}
		for (int i = 0; i < pings.size(); ++i) targets.remove(pings.get(i));
		pings.clear();
		freshTargets = true;
		Level level = radar.level;
		Entity controller = radar.getControllingPassenger();
		if (scanAircraft) {
			List<EntityAircraft> list = level.getEntitiesOfClass(
					EntityAircraft.class, getRadarBoundingBox(radar));
			for (int i = 0; i < list.size(); ++i) {
				if (!list.get(i).isOperational()) continue;
				Entity pilot = list.get(i).getControllingPassenger();
				if (radar.isRadarPlayersOnly() && pilot == null) continue;
				if (!basicCheck(radar, list.get(i), list.get(i).getStealth())) continue;
				RadarPing p = new RadarPing(list.get(i), 
					checkFriendly(controller, pilot));
				targets.add(p);
				pings.add(p);
				list.get(i).lockedOnto(radar.position());
			}
		}
		if (scanPlayers) {
			List<Player> list = level.getEntitiesOfClass(
					Player.class, getRadarBoundingBox(radar));
			for (int i = 0; i < list.size(); ++i) {
				if (list.get(i).getRootVehicle() instanceof EntityAircraft) continue;
				if (!basicCheck(radar, list.get(i), 1)) continue;
				RadarPing p = new RadarPing(list.get(i), 
					checkFriendly(controller, list.get(i)));
				targets.add(p);
				pings.add(p);
			}
		}
		if (scanMobs && !radar.isRadarPlayersOnly()) {
			List<Mob> list = level.getEntitiesOfClass(
					Mob.class, getRadarBoundingBox(radar));
			for (int i = 0; i < list.size(); ++i) {
				if (list.get(i).getRootVehicle() instanceof EntityAircraft) continue;
				if (!basicCheck(radar, list.get(i), 1)) continue;
				RadarPing p = new RadarPing(list.get(i), 
						checkFriendly(controller, list.get(i)));
				targets.add(p);
				pings.add(p);
			}
		}
	}
	
	private boolean checkFriendly(Entity controller, Entity target) {
		if (controller == null) return false;
		if (target == null) return false;
		if (controller.getTeam() == null) return false;
		if (target.getTeam() == null) return false;
		return target.getTeam().getName().equals(controller.getTeam().getName());
	}
	
	private boolean basicCheck(EntityAircraft radar, Entity ping, double stealth) {
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
		if (throWaterRange > 0 && ping.isInWater()) return true;
		boolean groundWater = UtilEntity.isOnGroundOrWater(ping);
		if (scanGround) if (groundWater) return true;
		if (scanAir) if (!groundWater) return true;
		return false;
	}
	
	private boolean checkTargetRange(Entity radar, Entity target, double stealth) {
		float dist = radar.distanceTo(target);
		//System.out.println("dist = "+dist+" range = "+range);
		if (fov == -1) {
			if (dist > range) {
				//System.out.println("out of range");
				return false;
			} 
		} else if (!UtilGeometry.isPointInsideCone(
				target.position(), 
				radar.position().add(pos),
				radar.getLookAngle(), 
				fov, range)) {
			//System.out.println("not in cone");
			return false;
		}
		double area = stealth;
		if (target instanceof EntityAircraft plane) area *= plane.getCrossSectionArea();
		else area *= target.getBbHeight() * target.getBbWidth();
		double areaMin = (1-Math.pow(range,-2)*Math.pow(dist-range,2))*sensitivity;
		//System.out.println("area = "+area+" min = "+areaMin);
		return area >= areaMin;
	}
	
	private boolean checkCanSee(Entity radar, Entity target) {
		return UtilEntity.canEntitySeeEntity(radar, target, 200, 
				throWaterRange, throGroundRange);
	}
	
	private AABB getRadarBoundingBox(Entity radar) {
		double x = radar.getX()+pos.x;
		double y = radar.getY()+pos.y;
		double z = radar.getZ()+pos.z;
		double w = range;
		return new AABB(x+w, y+w, z+w, x-w, y-w, z-w);
	}
	
	public String getId() {
		return id;
	}
	
	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public double getFov() {
		return fov;
	}

	public void setFov(double fov) {
		this.fov = fov;
	}

	public int getScanRate() {
		return scanRate;
	}

	public void setScanRate(int scanRate) {
		this.scanRate = scanRate;
	}
	
	public boolean isFreshTargets() {
		return freshTargets;
	}

	public boolean isScanAircraft() {
		return scanAircraft;
	}

	public boolean isScanPlayers() {
		return scanPlayers;
	}

	public boolean isScanMobs() {
		return scanMobs;
	}

	public void setScanAircraft(boolean scanAircraft) {
		this.scanAircraft = scanAircraft;
	}

	public void setScanPlayers(boolean scanPlayers) {
		this.scanPlayers = scanPlayers;
	}

	public void setScanMobs(boolean scanMobs) {
		this.scanMobs = scanMobs;
	}
	
	public boolean isScanGround() {
		return scanGround;
	}

	public void setScanGround(boolean scanGround) {
		this.scanGround = scanGround;
	}

	public boolean isScanAir() {
		return scanAir;
	}

	public void setScanAir(boolean scanAir) {
		this.scanAir = scanAir;
	}

	public static class RadarPing {
		
		public final int id;
		public final Vec3 pos;
		public final boolean isFriendly;
		
		public RadarPing(Entity ping, boolean isFriendly) {
			id = ping.getId();
			pos = ping.position();
			this.isFriendly = isFriendly;
		}
		
		public RadarPing(FriendlyByteBuf buffer) {
			id = buffer.readInt();
			pos = DataSerializers.VEC3.read(buffer);
			isFriendly = buffer.readBoolean();
		}
		
		public void write(FriendlyByteBuf buffer) {
			buffer.writeInt(id);
			DataSerializers.VEC3.write(buffer, pos);
			buffer.writeBoolean(isFriendly);
		}
		
		@Override
		public String toString() {
			return "PING["+(int)pos.x+","+(int)pos.y+","+(int)pos.z+"]";
		}
		
	}
	
	@Override
	public String toString() {
		return "["+id+":"+fov+":"+range+"]";
	}
	
	public RadarData copy() {
		return new RadarData(write());
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
	
	public boolean idMatch(String id, String slotId) {
		if (slotId == null) return false;
		if (id == null) return false;
		return this.id.equals(id) && this.slotId.equals(slotId);
	}
	
}
