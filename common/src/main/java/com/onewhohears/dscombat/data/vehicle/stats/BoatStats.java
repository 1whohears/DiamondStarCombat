package com.onewhohears.dscombat.data.vehicle.stats;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.vehicle.VehicleType;

import net.minecraft.resources.ResourceLocation;

public class BoatStats extends VehicleStats {

	public BoatStats(ResourceLocation key, JsonObject json) {
		super(key, json);
	}
	
	@Override
	public JsonPresetType getType() {
		return VehicleType.BOAT;
	}
	
	@Override 
	public BoatStats asBoat() { 
		return this; 
	}
	
	@Override
	public boolean isBoat() {
		return true;
	}

}
