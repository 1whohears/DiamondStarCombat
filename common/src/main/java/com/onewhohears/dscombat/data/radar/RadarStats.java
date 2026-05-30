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
	private final double verticalRange;
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
		verticalRange = UtilParse.getFloatSafe(json, "verticalRange", 2000);
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

	public double getVerticalRange() {
		return verticalRange;
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
		public Builder setVerticalRange(float range) {
			return setFloat("verticalRange", range);
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
