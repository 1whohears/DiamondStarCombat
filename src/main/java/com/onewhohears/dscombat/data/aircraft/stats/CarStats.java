package com.onewhohears.dscombat.data.aircraft.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.aircraft.VehicleType;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.resources.ResourceLocation;

public class CarStats extends VehicleStats {
	
	public final boolean isTank;
	
	public CarStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		JsonObject car = json.getAsJsonObject("stats").getAsJsonObject("car");
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
