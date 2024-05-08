package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.StorageExternalInstance;

import net.minecraft.resources.ResourceLocation;

public class StorageExternalStats extends StorageStats {

	public StorageExternalStats(ResourceLocation key, JsonObject json) {
		super(key, json);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.EXTERNAL_STORAGE;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new StorageExternalInstance<>(this);
	}

}
