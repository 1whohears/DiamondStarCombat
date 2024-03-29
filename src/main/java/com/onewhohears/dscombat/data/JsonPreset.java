package com.onewhohears.dscombat.data;

import org.slf4j.Logger;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.util.UtilGsonMerge;
import com.onewhohears.dscombat.util.UtilGsonMerge.ConflictStrategy;
import com.onewhohears.dscombat.util.UtilGsonMerge.JsonObjectExtensionConflictException;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.UtilParse;

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
	
	protected final Logger LOGGER = LogUtils.getLogger();
	
	private final ResourceLocation key;
	private final JsonObject data;
	private final String id;
	private final String displayName;
	private final int sort_factor;
	private final String copyId;
	private boolean hasBeenMerged = false;
	
	public JsonPreset(ResourceLocation key, JsonObject json) {
		this.key = key;
		this.data = json;
		this.id = UtilParse.getStringSafe(json, "presetId", "");
		this.displayName = UtilParse.getStringSafe(json, "displayName", "preset.dscombat."+id);
		this.sort_factor = UtilParse.getIntSafe(json, "sort_factor", 0);
		this.copyId = UtilParse.getStringSafe(json, "copyId", "");
	}
	
	public ResourceLocation getKey() {
		return key;
	}
	
	public JsonObject getJsonData() {
		return data.deepCopy();
	}
	
	protected JsonObject getJsonDataNotCopy() {
		return data;
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
		if (dn.startsWith("preset.")) return UtilMCText.translatable(dn);
		return UtilMCText.literal(dn);
	}
	
	public int getSortFactor() {
		return sort_factor;
	}
	
	public boolean isCopy() {
		return !copyId.isEmpty();
	}
	
	public String getCopyId() {
		return copyId;
	}
	
	public boolean hasBeenMerged() {
		return hasBeenMerged;
	}
	
	public boolean mergeWithParent(JsonPreset parent) {
		try {
			UtilGsonMerge.extendJsonObject(data, ConflictStrategy.PREFER_FIRST_OBJ, parent.getJsonData());
			hasBeenMerged = true;
			return true;
		} catch (JsonObjectExtensionConflictException e) {
			e.printStackTrace();
		}
		return false;
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
		if (this.getSortFactor() != other.getSortFactor()) 
			return this.getSortFactor() - other.getSortFactor();
		return this.getId().compareToIgnoreCase(other.getId());
	}
	
}
