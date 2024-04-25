package com.onewhohears.dscombat.data.radar;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.jsonpreset.JsonPreset;
import com.onewhohears.dscombat.data.jsonpreset.PresetBuilder;
import com.onewhohears.dscombat.data.weapon.RadarTargetTypes;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RadarData extends JsonPreset {
	
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
	
	private int maxCheckDist = 150;
	
	public void resetPings(List<RadarPing> vehiclePings) {
		for (int i = 0; i < pings.size(); ++i) vehiclePings.remove(pings.get(i));
		pings.clear();
	}
	
	public void tickUpdateTargets(EntityVehicle radar, List<RadarPing> vehiclePings) {
		if (scanTicks > scanRate) scanTicks = 0;
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
		if (scanAircraft && (mode.canScan(RadarMode.VEHICLES) || mode.isPlayersOrBots())) {
			scanAircraft(radar, controller, vehiclePings, radarArea, mode.isPlayersOnly(), mode.isPlayersOrBots());
		}
		if (scanPlayers && (mode.canScan(RadarMode.PLAYERS) || mode.isPlayersOrBots())) {
			scanPlayers(radar, controller, vehiclePings, radarArea);
		}
		if (scanMobs && mode.canScan(RadarMode.MOBS)) {
			scanMobs(radar, controller, vehiclePings, radarArea);
		}
	}
	
	private void scanAircraft(EntityVehicle radar, Entity controller, List<RadarPing> vehiclePings, AABB radarArea, 
			boolean playersOnly, boolean isPlayersOrBots) {
		//System.out.println("SCANNING VEHICLES");
		List<Entity> list = radar.level.getEntities(radar, radarArea, 
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
		List<Player> list = radar.level.getEntitiesOfClass(Player.class, radarArea);
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
			List<? extends Entity> list = radar.level.getEntitiesOfClass(clazz, radarArea);
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
		if (throWaterRange > 0 && ping.isInWater()) return true;
		boolean groundWater = UtilEntity.isOnGroundOrWater(ping);
		if (scanGround && groundWater) return true;
		if (scanAir && !groundWater) return true;
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
		double area = UtilEntity.getCrossSectionalArea(target) * stealth;
		double areaMin = (1-Math.pow(range,-2)*Math.pow(dist-range,2))*sensitivity;
		//System.out.println("area = "+area+" min = "+areaMin);
		return area >= areaMin;
	}
	
	private boolean checkCanSee(Entity radar, Entity target) {
		// throWaterRange+0.5 is needed for ground radar to see boats in water
		return UtilEntity.canPosSeeEntity(radar.position().add(pos), target, maxCheckDist, 
				throWaterRange+1, throGroundRange);
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
		public final PingTerrainType terrainType;
		public final PingEntityType entityType;
		private boolean isShared;
		private Vec3 clientPos;
		public RadarPing(Entity ping, boolean isFriendly, PingEntityType entityType) {
			id = ping.getId();
			pos = ping.getBoundingBox().getCenter();
			this.isFriendly = isFriendly;
			this.isShared = false;
			this.terrainType = PingTerrainType.getByEntity(ping);
			this.entityType = entityType;
		}
		private RadarPing(int id, Vec3 pos, boolean isFriendly, boolean isShared, PingTerrainType terrainType, PingEntityType entityType) {
			this.id = id;
			this.pos = pos;
			this.isFriendly = isFriendly;
			this.isShared = isShared;
			this.terrainType = terrainType;
			this.entityType = entityType;
		}
		public RadarPing(FriendlyByteBuf buffer) {
			id = buffer.readInt();
			pos = DataSerializers.VEC3.read(buffer);
			isFriendly = buffer.readBoolean();
			isShared = buffer.readBoolean();
			terrainType = PingTerrainType.getById(buffer.readByte());
			entityType = PingEntityType.getById(buffer.readByte());
		}
		public void write(FriendlyByteBuf buffer) {
			buffer.writeInt(id);
			DataSerializers.VEC3.write(buffer, pos);
			buffer.writeBoolean(isFriendly);
			buffer.writeBoolean(isShared);
			buffer.writeByte(terrainType.id);
			buffer.writeByte(entityType.id);
		}
		public boolean isShared() {
			return this.isShared;
		}
		public RadarPing getCopy(boolean isShared) {
			return new RadarPing(id, pos, isFriendly, isShared, terrainType, entityType);
		}
		public Vec3 getPosForClient() {
			if (clientPos != null) return clientPos;
			return pos;
		}
		public void setClientPos(Level level) {
			Entity e = level.getEntity(id);
			if (e == null) {
				clientPos = null;
				return;
			}
			clientPos = e.getBoundingBox().getCenter();
		}
		@Override
		public String toString() {
			return "PING["+(int)pos.x+","+(int)pos.y+","+(int)pos.z+"]";
		}
		@Override
		public boolean equals(Object o) {
			if (o instanceof RadarPing ping && ping.id == this.id) return true;
			return false;
		}
		public boolean dontDisplayByMode(RadarMode mode) {
			if (mode.isOff()) return true;
			if (mode.isAll()) return false;
			if (mode.isMobsOnly()) return !entityType.isMob();
			if (mode.isPlayersOnly()) return !entityType.isPlayer();
			if (mode.isPlayersOrBots()) return !entityType.isBot();
			if (mode.isVehiclesOnly()) return !entityType.isVehicle();
			return false;
		}
	}
	
	public static enum PingTerrainType {
		GROUND((byte)0, 0),
		AIR((byte)1, 2),
		WATER((byte)2, 1);
		public final byte id;
		public final int offset;
		private PingTerrainType(byte id, int offset) {
			this.id = id;
			this.offset = offset;
		}
		public int getIconOffset(int size) {
			return offset * size;
		}
		public boolean isGround() {
			return this == GROUND;
		}
		public boolean isAir() {
			return this == AIR;
		}
		public boolean isWater() {
			return this == WATER;
		}
		public static PingTerrainType getById(byte id) {
			for (int i = 0; i < values().length; ++i) 
				if (values()[i].id == id) 
					return values()[i];
			return GROUND;
		}
		public static PingTerrainType getByEntity(Entity e) {
			if (e.isInWater()) return WATER;
			if (UtilEntity.isOnGroundOrWater(e)) return GROUND;
			return AIR;
		}
	}
	
	public static enum PingEntityType {
		PLAYER((byte)0, 0),
		HOSTILE_MOB((byte)1, 1),
		FRIENDLY_MOB((byte)2, 2),
		VEHICLE((byte)3, 3),
		VEHICLE_PLAYER((byte)4, 3),
		VEHICLE_BOT((byte)5, 3);
		public final byte id;
		public final int offset;
		private PingEntityType(byte id, int offset) {
			this.id = id;
			this.offset = offset;
		}
		public int getIconOffset(int size) {
			return offset * size;
		}
		public boolean isMob() {
			return this == HOSTILE_MOB || this == FRIENDLY_MOB;
		}
		public boolean isVehicle() {
			return this == VEHICLE || this == VEHICLE_PLAYER || this == VEHICLE_BOT;
		}
		public boolean isPlayer() {
			return this == PLAYER || this == VEHICLE_PLAYER;
		}
		public boolean isBot() {
			return this == PLAYER || this == VEHICLE_PLAYER || this == VEHICLE_BOT;
		}
		public static PingEntityType getById(byte id) {
			for (int i = 0; i < values().length; ++i) 
				if (values()[i].id == id) 
					return values()[i];
			return FRIENDLY_MOB;
		}
	}
	
	public static enum RadarMode {
		ALL,
		PLAYERS,
		BOTS,
		VEHICLES,
		MOBS,
		OFF;
		public RadarMode cycle() {
			int i = this.ordinal();
			++i;
			if (i >= RadarMode.values().length) i = 0;
			return RadarMode.values()[i];
		}
		public boolean canScan(RadarMode mode) {
			if (this == ALL) return true;
			return this == mode;
		}
		public boolean isPlayersOnly() {
			return this == PLAYERS;
		}
		public boolean isPlayersOrBots() {
			return isPlayersOnly() || this == BOTS;
		}
		public boolean isMobsOnly() {
			return this == MOBS;
		}
		public boolean isVehiclesOnly() {
			return this == VEHICLES;
		}
		public boolean isOff() {
			return this == OFF;
		}
		public boolean isAll() {
			return this == ALL;
		}
		public static RadarMode byId(int id) {
			if (id < 0 || id >= values().length) return ALL;
			return values()[id];
		}
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
	
	public void setPos(Vec3 pos) {
		this.pos = pos;
	}
	
	public boolean idMatch(String id, String slotId) {
		if (slotId == null) return false;
		if (id == null) return false;
		return getId().equals(id) && slotId.equals(slotId);
	}
	
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
	
}
