package com.onewhohears.dscombat.data;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.JsonPreset.JsonPresetFactory;

import net.minecraft.resources.ResourceLocation;

/**
 * the parent class for json preset builders. usually called within a {@link JsonPresetGenerator}.
 * 
 * see {@link com.onewhohears.dscombat.data.aircraft.AircraftPreset},
 * {@link com.onewhohears.dscombat.data.weapon.AbstractWeaponBuilders},
 * and {@link com.onewhohears.dscombat.data.radar.RadarData} for example builders.
 * 
 * @author 1whohears
 * @param <C>
 */
public abstract class PresetBuilder<C extends PresetBuilder<C>> {
	
	private final String name;
	private final ResourceLocation key;
	private final JsonObject data;
	protected final JsonPresetFactory<? extends JsonPreset> sup;
	
	private final String copyId;
	private final JsonObject copyData;
	
	public PresetBuilder(String namespace, String name, JsonPresetFactory<? extends JsonPreset> sup) {
		this.name = name;
		key = new ResourceLocation(namespace, name);
		data = new JsonObject();
		data.addProperty("presetId", name);
		data.addProperty("displayName", "preset."+namespace+"."+name);
		this.sup = sup;
		copyId = "";
		copyData = new JsonObject();
		setupJsonData();
	}
	
	public PresetBuilder(String namespace, String name, JsonPresetFactory<? extends JsonPreset> sup, JsonObject copy) {
		this.name = name;
		key = new ResourceLocation(namespace, name);
		data = new JsonObject();
		data.addProperty("presetId", name);
		data.addProperty("displayName", "preset."+namespace+"."+name);
		this.sup = sup;
		copyId = copy.get("presetId").getAsString();
		copyData = copy;
		data.addProperty("copyId", copyId);
		setupJsonData();
	}
	
	public <T extends JsonPreset> T build() {
		return (T) sup.create(getKey(), getData());
	}
	
	protected void setupJsonData() {
		
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
	
	public boolean isCopy() {
		return !copyId.isEmpty();
	}
	
	public String getCopyId() {
		return copyId;
	}
	
	public JsonObject getCopyData() {
		return copyData;
	}
	
	public C setSortFactor(int sort_factor) {
		return setInt("sort_factor", sort_factor);
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
