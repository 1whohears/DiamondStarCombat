package com.onewhohears.dscombat.data.radar;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetStats;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.onewholibs.data.jsonpreset.PresetBuilder;
import com.onewhohears.dscombat.init.DataSerializers;

import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RadarStats extends JsonPresetStats {
	
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
	private final boolean scanMissiles;
    private final boolean useDistanceScale;
	
	public RadarStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		range = UtilParse.getFloatSafe(json, "range", 0);
		sensitivity = UtilParse.getFloatSafe(json, "sensitivity", 0);
		fov = UtilParse.getFloatSafe(json, "fov", 0);
		scanRate = UtilParse.getIntSafe(json, "scanRate", 100);
		scanAircraft = UtilParse.getBooleanSafe(json, "scanAircraft", false);
		scanPlayers = UtilParse.getBooleanSafe(json, "scanPlayers", false);
		scanMobs = UtilParse.getBooleanSafe(json, "scanMobs", false);
		scanGround = UtilParse.getBooleanSafe(json, "scanGround", false);
		scanAir = UtilParse.getBooleanSafe(json, "scanAir", false);
		throWaterRange = UtilParse.getFloatSafe(json, "throWaterRange", 0);
		throGroundRange = UtilParse.getFloatSafe(json, "throGroundRange", 0);
		scanMissiles = UtilParse.getBooleanSafe(json, "scanMissiles", false);
        useDistanceScale = UtilParse.getBooleanSafe(json, "useDistanceScale", false);
	}
	
	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new RadarInstance<>(this);
	}
	
	public RadarInstance<?> createRadarInstance() {
		return (RadarInstance<?>) createPresetInstance();
	}
	
	@Override
	public JsonPresetType getType() {
		return RadarType.STANDARD;
	}
	
	public double getUnscaledRange() {
		return range;
	}

	public double getFov() {
		return fov;
	}

	public int getScanRate() {
		return scanRate;
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
	
	public double getSensitivity() {
		return sensitivity;
	}

	public double getThroWaterRange() {
		return throWaterRange;
	}

	public double getThroGroundRange() {
		return throGroundRange;
	}

	public boolean isScanMissiles() {
		return scanMissiles;
	}

    public boolean isUseDistanceScale() {
        return useDistanceScale;
    }

    public double getRange() {
        if (isUseDistanceScale()) return DSCPhyCons.getIRLScale() * getUnscaledRange();
        return getUnscaledRange();
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
		public RadarPing(int id, Vec3 pos, boolean isFriendly, boolean isShared,
						  PingTerrainType terrainType, PingEntityType entityType) {
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
			if (entityType.isMissile()) return false;
			if (mode.isMobsOnly()) return !entityType.isMob();
			if (mode.isPlayersOnly()) return !entityType.isPlayer();
			if (mode.isPlayersOrBots()) return !entityType.isBot();
			if (mode.isVehiclesOnly()) return !entityType.isVehicle();
			return false;
		}
	}
	
	public enum PingTerrainType {
		GROUND((byte)0, 6),
		AIR((byte)1, 8),
		WATER((byte)2, 7);
		public final byte id;
		public final int index;
		PingTerrainType(byte id, int index) {
			this.id = id;
			this.index = index;
		}
		public int getIconIndex() {
			return index;
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
			if (UtilVehicleEntity.isOnGroundOrWater(e)) return GROUND;
			return AIR;
		}
	}
	
	public enum PingEntityType {
		PLAYER((byte)0, 0),
		HOSTILE_MOB((byte)1, 1),
		FRIENDLY_MOB((byte)2, 2),
		VEHICLE((byte)3, 3),
		VEHICLE_PLAYER((byte)4, 3),
		VEHICLE_BOT((byte)5, 3),
		MISSILE((byte)6, 5);
		public final byte id;
		public final int index;
		PingEntityType(byte id, int index) {
			this.id = id;
			this.index = index;
		}
		public int getIconIndex() {
			return index;
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
		public boolean isMissile() {
			return this == MISSILE;
		}
		public static PingEntityType getById(byte id) {
			for (int i = 0; i < values().length; ++i) 
				if (values()[i].id == id) 
					return values()[i];
			return FRIENDLY_MOB;
		}
	}
	
	public enum RadarMode {
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
		public boolean isOn() {
			return !isOff();
		}
		public boolean isAll() {
			return this == ALL;
		}
		public String getTranslatable() {
			return "radarmode.dscombat."+name().toLowerCase();
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

	public static class Builder extends PresetBuilder<Builder> {
		public Builder(String namespace, String name, RadarType type) {
			super(namespace, name, type);
		}
		public static Builder create(String namespace, String name) {
			return new Builder(namespace, name, RadarType.STANDARD);
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
		public Builder setScanMissiles(boolean scanMissiles) {
			return setBoolean("scanMissiles", scanMissiles);
		}
        public Builder setUseDistanceScale(boolean useDistanceScale) {
            return setBoolean("useDistanceScale", useDistanceScale);
        }
	}
	
}
