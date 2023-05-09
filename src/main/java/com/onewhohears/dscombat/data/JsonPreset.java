package com.onewhohears.dscombat.data;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;

public abstract class JsonPreset {
	
	private final ResourceLocation key;
	private final JsonObject data;
	private final String id;
	
	public JsonPreset(ResourceLocation key, JsonObject json) {
		this.key = key;
		this.data = json;
		this.id = json.get("presetId").getAsString();
	}
	
	public ResourceLocation getKey() {
		return key;
	}
	
	public JsonObject getJsonData() {
		return data.deepCopy();
	}
	
	public String getId() {
		return id;
	}
	
	public String getNameSpace() {
		return getKey().getNamespace();
	}
	
	public abstract <T extends JsonPreset> T copy();
	
	public interface JsonPresetFactory<T extends JsonPreset> {
		T create(ResourceLocation key, JsonObject data);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof JsonPreset j) return j.getId().equals(getId());
		return false;
	}
	
	@Override
	public String toString() {
		return getKey().toString()+" "+getJsonData().toString();
	}
	
}
