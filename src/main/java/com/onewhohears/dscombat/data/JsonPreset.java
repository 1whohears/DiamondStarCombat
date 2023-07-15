package com.onewhohears.dscombat.data;

import com.google.gson.JsonObject;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public abstract class JsonPreset {
	
	private final ResourceLocation key;
	private final JsonObject data;
	private final String id;
	private final String displayName;
	
	public JsonPreset(ResourceLocation key, JsonObject json) {
		this.key = key;
		this.data = json;
		this.id = json.get("presetId").getAsString();
		this.displayName = json.get("displayName").getAsString();
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
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getNameSpace() {
		return getKey().getNamespace();
	}
	
	public MutableComponent getDisplayNameComponent() {
		String dn = getDisplayName();
		if (dn.startsWith("preset.")) return new TranslatableComponent(dn);
		return new TextComponent(dn);
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
	
	public <T extends JsonPreset> int compare(T other) {
		return this.getId().compareToIgnoreCase(other.getId());
	}
	
}
