package com.onewhohears.dscombat.data.vehicle.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.vehicle.VehicleType;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.resources.ResourceLocation;

public class CarStats extends VehicleStats {
	
	public final boolean isTank;
	
	public CarStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		JsonObject car = UtilParse.getJsonSafe(UtilParse.getJsonSafe(json,"stats"), "car");
		isTank = UtilParse.getBooleanSafe(car, "isTank", false);
	}
	
	@Override
	public JsonPresetType getType() {
		return VehicleType.CAR;
	}
	
	@Override 
	public CarStats asCar() { 
		return this; 
	}
	
	@Override
	public boolean isTank() {
		return true;
	}

}
