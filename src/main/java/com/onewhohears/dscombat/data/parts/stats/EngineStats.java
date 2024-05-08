package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.EngineInstance;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.resources.ResourceLocation;

public class EngineStats extends PartStats {
	
	public static enum EngineType {
		SPIN,
		PUSH
	}
	
	private final EngineType engineType;
	private final float thrust;
	private final float heat;
	private final float fuelRate;
	
	public EngineStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		engineType = UtilParse.getEnumSafe(json, "engineType", EngineType.class);
		thrust = UtilParse.getFloatSafe(json, "thrust", 0);
		heat = UtilParse.getFloatSafe(json, "heat", 0);
		fuelRate = UtilParse.getFloatSafe(json, "fuelRate", 0);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.INTERNAL_ENGINE;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new EngineInstance<>(this);
	}
	
	public EngineType getEngineType() {
		return engineType;
	}
	
	public float getThrust() {
		return thrust;
	}
	
	public float getHeat() {
		return heat;
	}
	
	public float getFuelPerTick() {
		return fuelRate;
	}
	
	@Override
	public boolean isEngine() {
		return true;
	}

}
