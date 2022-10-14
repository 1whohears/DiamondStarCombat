package com.onewhohears.dscombat.data;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.util.UtilEntity;
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
	private double fov;
	private int scanRate;
	private boolean scanAircraft;
	private boolean scanPlayers;
	private boolean scanMobs;
	private boolean scanGround;
	private boolean scanAir;
	
	private boolean freshTargets;
	private int scanTicks;
	private List<RadarPing> pings = new ArrayList<RadarPing>();
	
	public RadarData(String id, double range, double fov, int scanRate) {
		this.id = id;
		this.pos = new Vec3(0, 0, 0);
		this.range = range;
		this.fov = fov;
		this.scanRate = scanRate;
	}
	
	public RadarData(CompoundTag tag) {
		id = tag.getString("id");
		pos = new Vec3(0, 0, 0);
		range = tag.getDouble("range");
		fov = tag.getDouble("fov");
		scanRate = tag.getInt("scanRate");
		scanAircraft = tag.getBoolean("scanAircraft");
		scanPlayers = tag.getBoolean("scanPlayers");
		scanMobs = tag.getBoolean("scanMobs");
		scanGround = tag.getBoolean("scanGround");
		scanAir = tag.getBoolean("scanAir");
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putString("id", id);
		// pos
		tag.putDouble("range", range);
		tag.putDouble("fov", fov);
		tag.putInt("scanRate", scanRate);
		tag.putBoolean("scanAircraft", scanAircraft);
		tag.putBoolean("scanPlayers", scanPlayers);
		tag.putBoolean("scanMobs", scanMobs);
		tag.putBoolean("scanGround", scanGround);
		tag.putBoolean("scanAir", scanAir);
		return tag;
	}
	
	public RadarData(FriendlyByteBuf buffer) {
		id = buffer.readUtf();
		pos = new Vec3(0, 0, 0);
		range = buffer.readDouble();
		fov = buffer.readDouble();
		scanRate = buffer.readInt();
		scanAircraft = buffer.readBoolean();
		scanPlayers = buffer.readBoolean();
		scanMobs = buffer.readBoolean();
		scanGround = buffer.readBoolean();
		scanAir = buffer.readBoolean();
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUtf(id);
		// pos
		buffer.writeDouble(range);
		buffer.writeDouble(fov);
		buffer.writeInt(scanRate);
		buffer.writeBoolean(scanAircraft);
		buffer.writeBoolean(scanPlayers);
		buffer.writeBoolean(scanMobs);
		buffer.writeBoolean(scanGround);
		buffer.writeBoolean(scanAir);
	}
	
	public void tickUpdateTargets(EntityAbstractAircraft radar, List<RadarPing> targets) {
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
		if (scanAircraft) {
			List<EntityAbstractAircraft> list = level.getEntitiesOfClass(
					EntityAbstractAircraft.class, getRadarBoundingBox(radar));
			for (int i = 0; i < list.size(); ++i) {
				if (!basicCheck(radar, list.get(i), list.get(i).getStealth())) continue;
				RadarPing p = new RadarPing(list.get(i));
				targets.add(p);
				pings.add(p);
				list.get(i).lockedOnto();
			}
		}
		if (scanPlayers) {
			List<Player> list = level.getEntitiesOfClass(
					Player.class, getRadarBoundingBox(radar));
			for (int i = 0; i < list.size(); ++i) {
				if (list.get(i).getRootVehicle() instanceof EntityAbstractAircraft) continue;
				if (!basicCheck(radar, list.get(i), 1)) continue;
				RadarPing p = new RadarPing(list.get(i));
				targets.add(p);
				pings.add(p);
			}
		}
		if (scanMobs) {
			List<Mob> list = level.getEntitiesOfClass(
					Mob.class, getRadarBoundingBox(radar));
			for (int i = 0; i < list.size(); ++i) {
				if (list.get(i).getRootVehicle() instanceof EntityAbstractAircraft) continue;
				if (!basicCheck(radar, list.get(i), 1)) continue;
				RadarPing p = new RadarPing(list.get(i));
				targets.add(p);
				pings.add(p);
			}
		}
	}
	
	private boolean basicCheck(EntityAbstractAircraft radar, Entity ping, double rangeMod) {
		if (radar.equals(ping)) return false;
		if (ping.isOnGround() && !scanGround) return false;
		if (!ping.isOnGround() && !scanAir) return false;
		if (radar.isVehicleOf(ping)) return false;
		if (!checkTargetRange(radar, ping, rangeMod)) return false;
		if (!checkCanSee(radar, ping)) return false;
		return true;
	}
	
	private boolean checkTargetRange(Entity radar, Entity target, double rangeMod) {
		if (fov == -1) return radar.distanceTo(target) <= range;
		return UtilGeometry.isPointInsideCone(
				target.position(), 
				radar.position(), // TODO change radar position based on pos
				radar.getLookAngle(), 
				fov, range*rangeMod);
	}
	
	private boolean checkCanSee(Entity radar, Entity target) {
		return UtilEntity.canEntitySeeEntity(radar, target);
	}
	
	private AABB getRadarBoundingBox(Entity radar) {
		double x = radar.getX();
		double y = radar.getY();
		double z = radar.getZ();
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
		
		public RadarPing(Entity ping) {
			id = ping.getId();
			pos = ping.position();
		}
		
		public RadarPing(FriendlyByteBuf buffer) {
			id = buffer.readInt();
			double x, y, z;
			x = buffer.readDouble();
			y = buffer.readDouble();
			z = buffer.readDouble();
			pos = new Vec3(x, y, z);
		}
		
		public void write(FriendlyByteBuf buffer) {
			buffer.writeInt(id);
			buffer.writeDouble(pos.x);
			buffer.writeDouble(pos.y);
			buffer.writeDouble(pos.z);
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
	
}
