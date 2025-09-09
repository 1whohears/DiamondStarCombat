package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.FuelTankExternalInstance;

import net.minecraft.resources.ResourceLocation;

public class FuelTankExternalStats extends FuelTankStats {

	public FuelTankExternalStats(ResourceLocation key, JsonObject json) {
		super(key, json);
	}
	
	@Override
	public JsonPresetType getType() {
		return PartType.EXTERNAL_FUEL_TANK;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new FuelTankExternalInstance<>(this);
	}

}
