package com.onewhohears.dscombat.data;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.JsonPreset.JsonPresetFactory;

import net.minecraft.resources.ResourceLocation;

public abstract class PresetBuilder<C extends PresetBuilder<C>> {
	
	private final String name;
	private final ResourceLocation key;
	private final JsonObject data;
	protected final JsonPresetFactory<? extends JsonPreset> sup;
	
	public PresetBuilder(String namespace, String name, JsonPresetFactory<? extends JsonPreset> sup) {
		this.name = name;
		key = new ResourceLocation(namespace, name);
		data = new JsonObject();
		data.addProperty("presetId", name);
		data.addProperty("displayName", "preset."+namespace+"."+name);
		this.sup = sup;
	}
	
	public PresetBuilder(String namespace, String name, JsonPresetFactory<? extends JsonPreset> sup, JsonObject copy) {
		this.name = name;
		key = new ResourceLocation(namespace, name);
		data = copy;
		data.addProperty("presetId", name);
		data.addProperty("displayName", "preset."+namespace+"."+name);
		this.sup = sup;
	}
	
	public <T extends JsonPreset> T build() {
		return (T) sup.create(getKey(), getData());
	}
	
	public String getPresetId() {
		return name;
	}
	
	public ResourceLocation getKey() {
		return key;
	}
	
	public JsonObject getData() {
		return data;
	}
	
	public C setDisplayName(String name) {
		return setString("displayName", name);
	}
	
	public C setBoolean(String key, boolean value) {
		data.addProperty(key, value);
		return (C) this;
	}
	
	public C setInt(String key, int value) {
		data.addProperty(key, value);
		return (C) this;
	}
	
	public C setFloat(String key, float value) {
		data.addProperty(key, value);
		return (C) this;
	}
	
	public C setString(String key, String value) {
		data.addProperty(key, value);
		return (C) this;
	}
	
}
