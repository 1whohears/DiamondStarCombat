package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.FlareDispenserInstance;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.resources.ResourceLocation;

public class FlareDispenserStats extends PartStats {
	
	private final int max, age;
	private final float heat;
	
	public FlareDispenserStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		max = UtilParse.getIntSafe(json, "max", 0);
		age = UtilParse.getIntSafe(json, "age", 0);
		heat = UtilParse.getFloatSafe(json, "heat", 0);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.FLARE_DISPENSER;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new FlareDispenserInstance<>(this);
	}
	
	public int getMaxFlares() {
		return max;
	}
	
	public int getMaxAge() {
		return age;
	}
	
	public float getInitHeat() {
		return heat;
	}

}
