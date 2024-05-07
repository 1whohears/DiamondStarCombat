package com.onewhohears.dscombat.data.vehicle.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.vehicle.VehicleType;

import net.minecraft.resources.ResourceLocation;

public class SubmarineStats extends BoatStats {

	public SubmarineStats(ResourceLocation key, JsonObject json) {
		super(key, json);
	}
	
	@Override
	public JsonPresetType getType() {
		return VehicleType.SUBMARINE;
	}
	
	@Override 
	public SubmarineStats asSubmarine() { 
		return this; 
	}
	
	@Override
	public boolean isBoat() {
		return true;
	}
	
	@Override
	public boolean isSub() {
		return true;
	}

}
