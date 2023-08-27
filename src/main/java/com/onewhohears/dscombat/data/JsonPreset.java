package com.onewhohears.dscombat.data;

import com.google.gson.JsonObject;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * used to store preset data and parse data from json files.
 * see {@link JsonPresetReloadListener} for how the presets are read from datapacks.
 * see {@link JsonPresetGenerator} for how presets are built by minecraft data generators.
 * see {@link PresetBuilder} for an abstract preset builder for the generator to use.
 * 
 * see {@link com.onewhohears.dscombat.data.aircraft.AircraftPreset},
 * {@link com.onewhohears.dscombat.data.weapon.WeaponData},
 * and {@link com.onewhohears.dscombat.data.radar.RadarData} for examples.
 * @author 1whohears
 */
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
		if (dn.startsWith("preset.")) return Component.translatable(dn);
		return Component.literal(dn);
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
