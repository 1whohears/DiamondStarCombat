package com.onewhohears.dscombat.data.jsonpreset;

import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.util.UtilGsonMerge;
import com.onewhohears.dscombat.util.UtilGsonMerge.ConflictStrategy;
import com.onewhohears.dscombat.util.UtilGsonMerge.JsonObjectExtensionConflictException;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * used to store preset data and parse data from json files.
 * see {@link JsonPresetReloadListener} for how the presets are read from datapacks.
 * see {@link JsonPresetGenerator} for how presets are built by minecraft data generators.
 * see {@link PresetBuilder} for an abstract preset builder for the generator to use.
 * 
 * see {@link com.onewhohears.dscombat.data.aircraft.stats.VehicleStats},
 * {@link com.onewhohears.dscombat.data.weapon.stats.WeaponStats},
 * and {@link com.onewhohears.dscombat.data.radar.RadarStats} for examples.
 * @author 1whohears
 */
public abstract class JsonPresetStats {
	
	protected static final Logger LOGGER = LogUtils.getLogger();
	
	private final ResourceLocation key;
	private final JsonObject data;
	private final String id;
	private final String displayName;
	private final int sort_factor;
	private final String copyId;
	private boolean hasBeenMerged = false;
	
	public JsonPresetStats(ResourceLocation key, JsonObject json) {
		this.key = key;
		this.data = json;
		this.id = UtilParse.getStringSafe(json, "presetId", "");
		this.displayName = UtilParse.getStringSafe(json, "displayName", "preset.dscombat."+id);
		this.sort_factor = UtilParse.getIntSafe(json, "sort_factor", 0);
		this.copyId = UtilParse.getStringSafe(json, "copyId", "");
	}
	
	public abstract JsonPresetType getType();
	
	public ResourceLocation getKey() {
		return key;
	}
	
	protected JsonObject getJsonData() {
		return data;
	}
	
	public JsonObject copyJsonData() {
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
	
	public boolean mergeWithParent(JsonPresetStats parent) {
		try {
			UtilGsonMerge.extendJsonObject(data, ConflictStrategy.PREFER_FIRST_OBJ, parent.data.deepCopy());
			hasBeenMerged = true;
			return true;
		} catch (JsonObjectExtensionConflictException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof JsonPresetStats j) return j.getId().equals(getId());
		return false;
	}
	
	@Override
	public String toString() {
		return getKey().toString()+" "+getJsonData().toString();
	}
	
	public <T extends JsonPresetStats> int compare(T other) {
		if (this.getType() != other.getType()) 
			return this.getType().getSortFactor() - other.getType().getSortFactor();
		if (this.getSortFactor() != other.getSortFactor()) 
			return this.getSortFactor() - other.getSortFactor();
		return this.getId().compareToIgnoreCase(other.getId());
	}
	
	@Nullable 
	public JsonPresetInstance<?> createPresetInstance(@Nullable CompoundTag nbt) {
		JsonPresetInstance<?> instance = createPresetInstance();
		if (instance == null) return null;
		if (nbt != null) instance.readNBT(nbt);
		return instance;
	}
	
	@Nullable public abstract JsonPresetInstance<?> createPresetInstance();
	
}
