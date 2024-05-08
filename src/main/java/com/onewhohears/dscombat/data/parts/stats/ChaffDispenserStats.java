package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.ChaffDispenserInstance;

import net.minecraft.resources.ResourceLocation;

public class ChaffDispenserStats extends PartStats {

	public ChaffDispenserStats(ResourceLocation key, JsonObject json) {
		super(key, json);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.CHAFF_DISPENSER;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new ChaffDispenserInstance<>(this);
	}

}
