package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.FuelTankInstance;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.resources.ResourceLocation;

public class FuelTankStats extends PartStats {
	
	private final float max;
	
	public FuelTankStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		max = UtilParse.getIntSafe(json, "max", 0);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.FUEL_TANK;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new FuelTankInstance<>(this);
	}
	
	public float getMaxFuel() {
		return max;
	}
	
	@Override
	public boolean isFuelTank() {
		return true;
	}

}
