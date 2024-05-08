package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.StorageInstance;
import com.onewhohears.minigames.util.UtilParse;

import net.minecraft.resources.ResourceLocation;

public class StorageStats extends PartStats {
	
	private final int size;
	
	public StorageStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		size = UtilParse.getIntSafe(json, "size", 0);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.INTERNAL_STORAGE;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new StorageInstance<>(this);
	}
	
	public int getSize() {
		return size;
	}
	
	@Override
	public boolean isStorageBox() {
		return true;
	}

}
