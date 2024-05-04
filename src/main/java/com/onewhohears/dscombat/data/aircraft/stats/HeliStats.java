package com.onewhohears.dscombat.data.aircraft.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.aircraft.VehicleType;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.resources.ResourceLocation;

public class HeliStats extends VehicleStats {
	
	public final float accForward, accSide, heliLiftFactor;
	public final boolean alwaysLandingGear;
	
	public HeliStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		JsonObject heli = json.getAsJsonObject("stats").getAsJsonObject("heli");
		accForward = UtilParse.getFloatSafe(heli, "accForward", 0.1f);
		accSide = UtilParse.getFloatSafe(heli, "accSide", 0.1f);
		heliLiftFactor = UtilParse.getFloatSafe(heli, "heliLiftFactor", 1);
		alwaysLandingGear = UtilParse.getBooleanSafe(heli, "alwaysLandingGear", false);
	}
	
	@Override
	public JsonPresetType getType() {
		return VehicleType.HELICOPTER;
	}

}
