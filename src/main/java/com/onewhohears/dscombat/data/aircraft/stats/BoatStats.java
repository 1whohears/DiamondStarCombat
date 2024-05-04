package com.onewhohears.dscombat.data.aircraft.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.aircraft.VehicleType;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;

import net.minecraft.resources.ResourceLocation;

public class BoatStats extends VehicleStats {

	public BoatStats(ResourceLocation key, JsonObject json) {
		super(key, json);
	}
	
	@Override
	public JsonPresetType getType() {
		return VehicleType.BOAT;
	}

}
