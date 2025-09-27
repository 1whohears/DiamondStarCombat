package com.onewhohears.dscombat.data.vehicle.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.vehicle.VehicleType;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;

public class StationaryStats extends VehicleStats {

    private final boolean isStationaryRadar;

    public StationaryStats(ResourceLocation key, JsonObject json) {
        super(key, json);
        JsonObject car = UtilParse.getJsonSafe(UtilParse.getJsonSafe(json,"stats"), "stationary");
        isStationaryRadar = UtilParse.getBooleanSafe(car, "isStationaryRadar", false);
    }

    @Override
    public JsonPresetType getType() {
        return VehicleType.STATIONARY;
    }

    @Override
    public boolean isStationaryRadar() {
        return isStationaryRadar;
    }
}
