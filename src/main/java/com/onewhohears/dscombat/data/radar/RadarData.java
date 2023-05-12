package com.onewhohears.dscombat.data.radar;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.JsonPreset;
import com.onewhohears.dscombat.data.PresetBuilder;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RadarData extends JsonPreset {
	
	public static class Builder extends PresetBuilder<Builder> {

		public Builder(String namespace, String name, JsonPresetFactory<? extends RadarData> sup) {
			super(namespace, name, sup);
		}
		
		public static Builder create(String namespace, String name) {
			return new Builder(namespace, name, (key, json) -> new RadarData(key, json));
		}
		
		public Builder setRange(float range) {
			return setFloat("range", range);
		}
		
		public Builder setSensitivity(float sensitivity) {
			return setFloat("sensitivity", sensitivity);
		}
		
		public Builder setFieldOfView(float fov) {
			return setFloat("fov", fov);
		}
		
		public Builder setScanRate(int scanRate) {
			return setInt("scanRate", scanRate);
		}
		
		public Builder setScanAircraft(boolean scanAircraft) {
			return setBoolean("scanAircraft", scanAircraft);
		}
		
		public Builder setScanPlayers(boolean scanPlayers) {
			return setBoolean("scanPlayers", scanPlayers);
		}
		
		public Builder setScanMobs(boolean scanMobs) {
			return setBoolean("scanMobs", scanMobs);
		}
		
		public Builder setScanGround(boolean scanGround) {
			return setBoolean("scanGround", scanGround);
		}
		
		public Builder setScanAir(boolean scanAir) {
			return setBoolean("scanAir", scanAir);
		}
		
		public Builder setThroWaterRange(float throWaterRange) {
			return setFloat("throWaterRange", throWaterRange);
		}
		
		public Builder setThroGroundRange(float throGroundRange) {
			return setFloat("throGroundRange", throGroundRange);
		}
		
	}
	
	private final double range;
	private final double sensitivity;
	private final double fov;
	private final int scanRate;
	private final boolean scanAircraft;
	private final boolean scanPlayers;
	private final boolean scanMobs;
	private final boolean scanGround;
	private final boolean scanAir;
	private final double throWaterRange;
	private final double throGroundRange;
	
	private String slotId = "";
	private Vec3 pos = Vec3.ZERO;
	private boolean freshTargets;
	private int scanTicks;
	private List<RadarPing> pings = new ArrayList<RadarPing>();
	
	public RadarData(ResourceLocation key, JsonObject json) {
		super(key, json);
		range = json.get("range").getAsDouble();
		sensitivity = json.get("sensitivity").getAsDouble();
		fov = json.get("fov").getAsDouble();
		scanRate = json.get("scanRate").getAsInt();
		scanAircraft = json.get("scanAircraft").getAsBoolean();
		scanPlayers = json.get("scanPlayers").getAsBoolean();
		scanMobs = json.get("scanMobs").getAsBoolean();
		scanGround = json.get("scanGround").getAsBoolean();
		scanAir = json.get("scanAir").getAsBoolean();
		throWaterRange = json.get("throWaterRange").getAsDouble();
		throGroundRange = json.get("throGroundRange").getAsDouble();
	}
	
	public void readNBT(CompoundTag tag) {
		setSlot(tag.getString("slotId"));
	}
	
	public CompoundTag writeNbt() {
		CompoundTag tag = new CompoundTag();
		tag.putString("id", getId());
		tag.putString("slotId", slotId);
		return tag;
	}
	
	public void readBuffer(FriendlyByteBuf buffer) {
		// id String is read in DataSerializers
		slotId = buffer.readUtf();
	}
	
	public void writeBuffer(FriendlyByteBuf buffer) {
		buffer.writeUtf(getId());
		buffer.writeUtf(slotId);
	}
	
	public void tickUpdateTargets(EntityAircraft radar, List<RadarPing> targets) {
		if (scanTicks > scanRate) scanTicks = 0;
		else {
			++scanTicks;
			freshTargets = false;
			return;
		}
		maxCheckDist = Config.SERVER.maxBlockCheckDepth.get();
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
		return controller.isAlliedTo(target);
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
	
	private int maxCheckDist = 150;
	
	private boolean checkCanSee(Entity radar, Entity target) {
		return UtilEntity.canEntitySeeEntity(radar, target, maxCheckDist, 
				throWaterRange, throGroundRange);
	}
	
	private AABB getRadarBoundingBox(Entity radar) {
		double x = radar.getX()+pos.x;
		double y = radar.getY()+pos.y;
		double z = radar.getZ()+pos.z;
		double w = range;
		return new AABB(x+w, y+w, z+w, x-w, y-w, z-w);
	}
	
	public double getRange() {
		return range;
	}


	public double getFov() {
		return fov;
	}


	public int getScanRate() {
		return scanRate;
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
	
	public boolean isScanGround() {
		return scanGround;
	}

	public boolean isScanAir() {
		return scanAir;
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
	
	public static enum RadarMode {
		OFF,
		MOBS,
		PAYERS,
		VEHICLES,
		ALL
	}
	
	@Override
	public String toString() {
		return "["+getId()+":"+fov+":"+range+"]";
	}
	
	public <T extends JsonPreset> T copy() {
		return (T) new RadarData(getKey(), getJsonData());
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
		return getId().equals(id) && slotId.equals(slotId);
	}
	
}
