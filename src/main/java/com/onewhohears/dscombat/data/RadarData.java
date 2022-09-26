package com.onewhohears.dscombat.data;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityRocket;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RadarData extends PartData {
	
	private double range;
	private double fov;
	private int scanRate;
	private boolean scanAircraft;
	private boolean scanPlayers;
	private boolean scanMobs;
	
	public List<Entity> targets = new ArrayList<Entity>();
	public Entity selected;
	private int scanTicks;
	private boolean freshTargets;
	private List<EntityRocket> rockets = new ArrayList<EntityRocket>();
	
	public RadarData(String id, double range, double fov, int scanRate) {
		super(id, Vec3.ZERO);
		this.range = range;
		this.fov = fov;
		this.scanRate = scanRate;
	}
	
	public RadarData(CompoundTag tag) {
		super(tag);
		range = tag.getDouble("range");
		fov = tag.getDouble("fov");
		scanRate = tag.getInt("scanRate");
		scanAircraft = tag.getBoolean("scanAircraft");
		scanPlayers = tag.getBoolean("scanPlayers");
		scanMobs = tag.getBoolean("scanMobs");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putDouble("range", range);
		tag.putDouble("fov", fov);
		tag.putInt("scanRate", scanRate);
		tag.putBoolean("scanAircraft", scanAircraft);
		tag.putBoolean("scanPlayers", scanPlayers);
		tag.putBoolean("scanMobs", scanMobs);
		return tag;
	}
	
	public RadarData(FriendlyByteBuf buffer) {
		super(buffer);
		range = buffer.readDouble();
		fov = buffer.readDouble();
		scanRate = buffer.readInt();
		scanAircraft = buffer.readBoolean();
		scanPlayers = buffer.readBoolean();
		scanMobs = buffer.readBoolean();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeDouble(range);
		buffer.writeDouble(fov);
		buffer.writeInt(scanRate);
		buffer.writeBoolean(scanAircraft);
		buffer.writeBoolean(scanPlayers);
		buffer.writeBoolean(scanMobs);
	}
	
	public List<Entity> tickUpdateTargets() {
		if (scanTicks > scanRate) scanTicks = 0;
		else {
			++scanTicks;
			freshTargets = false;
			return targets;
		}
		freshTargets = true;
		targets.clear();
		Entity radar = this.getParent();
		Level level = radar.level;
		List<Entity> list = level.getEntities(radar, getRadarBoundingBox(radar));
		for (int i = 0; i < list.size(); ++i) {
			Entity target = list.get(i);
			if (target.isInvisible()) continue;
			if (this.scanAircraft && target instanceof EntityAbstractAircraft plane) {
				if (!checkTargetRange(radar, target, plane.getStealth())) continue;
				if (!checkCanSee(radar, target)) continue;
				targets.add(target);
			} else if (this.scanPlayers && target instanceof Player player) {
				if (!checkTargetRange(radar, target, 1)) continue;
				if (!checkCanSee(radar, target)) continue;
				targets.add(target);
			} else if (this.scanMobs && target instanceof Mob mob) {
				if (!checkTargetRange(radar, target, 1)) continue;
				if (!checkCanSee(radar, target)) continue;
				targets.add(target);
			}
		}
		updateRockets();
		// TODO send new targets to client
		return targets;
	}
	
	private void updateRockets() {
		for (int i = 0; i < rockets.size(); ++i) {
			EntityRocket r = rockets.get(i);
			if (r.isRemoved()) {
				rockets.remove(i--);
				continue;
			}
			// TODO check if this radar is still locked onto this rocket's target
		}
	}
	
	public void addRocket(EntityRocket r) {
		if (!rockets.contains(r)) rockets.add(r);
	}
	
	/**
	 * USE ON CLIENT SIDE
	 * @param target
	 */
	public void selectTarget(Entity target) {
		selected = target;
		// TODO send packet to server of selected target
	}
	
	/**
	 * USE ON CLIENT SIDE
	 * @return
	 */
	public List<Entity> getRadarPings() {
		return targets;
	}
	
	private boolean checkTargetRange(Entity radar, Entity target, double rangeMod) {
		if (fov == -1) return true;
		return UtilGeometry.isPointInsideCone(
				target.position(), 
				radar.position().add(getRelativePos()), 
				radar.getLookAngle(), 
				fov, range*rangeMod);
	}
	
	private boolean checkCanSee(Entity radar, Entity target) {
		return UtilGeometry.canEntitySeeEntity(radar, target);
	}
	
	private AABB getRadarBoundingBox(Entity radar) {
		double x = radar.getX();
		double y = radar.getY();
		double z = radar.getZ();
		double w = range/2;
		return new AABB(x+w, y+w, z+w, x-w, y-w, z-w);
	}
	
	@Override
	public PartType getType() {
		return PartType.RADAR;
	}

	@Override
	public void setup(EntityAbstractAircraft craft) {
		super.setup(craft);
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
	
	public Entity getSelectedTarget() {
		return selected;
	}
	
}
