package com.onewhohears.dscombat.data.jsonpreset;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.data.vehicle.VehiclePresets;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

/**
 * a json file from datapacks reader for this mod's preset system.
 * comes with ways to sort the presets and synch the preset info with the client.
 * see {@link VehiclePresets},
 * {@link com.onewhohears.dscombat.data.weapon.WeaponPresets},
 * and {@link com.onewhohears.dscombat.data.radar.RadarPresets} for examples.
 * 
 * see {@link JsonPresetGenerator} for a way to generate json presets.
 * 
 * @author 1whohears
 * @param <T> the type of preset this reader builds from json files
 */
public abstract class JsonPresetReloadListener<T extends JsonPresetStats> extends SimpleJsonResourceReloadListener {
	
	protected final Logger LOGGER = LogUtils.getLogger();
	protected final Map<String, T> presetMap = new HashMap<>();
	protected final Map<String, JsonPresetType> typeMap = new HashMap<>();
	protected boolean setup = false;
	
	public JsonPresetReloadListener(String directory) {
		super(UtilParse.GSON, directory);
	}
	/**
	 * @param id same as name in the builder
	 * @return an immutable preset stats object. null if it doesn't exist. 
	 */
	@Nullable
	public T get(String id) {
		if (id == null) return null;
		if (!has(id)) return null;
		return presetMap.get(id);
	}
	
	@Nullable
	public T getFromNbt(CompoundTag nbt) {
		if (nbt == null) return null;
		if (!nbt.contains("presetId")) return null;
		String presetId = nbt.getString("presetId");
		return get(presetId);
	}
	
	@Nullable
	public JsonPresetInstance<?> createInstanceFromNbt(CompoundTag nbt) {
		T stats = getFromNbt(nbt);
		if (stats == null) return null;
		return stats.createPresetInstance(nbt);
 	}
	
	public boolean has(String id) {
		return presetMap.containsKey(id);
	}
	
	public abstract T[] getAll();
	
	public String[] getAllIds() {
		String[] names = new String[getAll().length];
		for (int i = 0; i < names.length; ++i) 
			names[i] = getAll()[i].getId();
		return names;
	}
	
	protected abstract void resetCache();
	
	public int getNum() {
		return presetMap.size();
	}
	
	public boolean isSetup() {
		return setup;
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
		LOGGER.info("APPLYING PRESETS TO COMMON CACHE "+getName());
		setup = false;
		presetMap.clear();
		registerPresetTypes();
		map.forEach((key, je) -> { try {
			LOGGER.info("ADD: "+key.toString()/*+" "+je.toString()*/);
			JsonObject json = UtilParse.GSON.fromJson(je, JsonObject.class);
			T data = getFromJson(key, json);
			if (data == null) {
				LOGGER.warn("ERROR: failed to parse preset "+key.toString());
				return;
			}
			if (!presetMap.containsKey(data.getId())) presetMap.put(data.getId(), data);
			else {
				LOGGER.warn("ERROR: Can't have 2 presets with the same name! "+key.toString());
			}
		} catch (Exception e) {
			LOGGER.warn("ERROR: SKIPPING "+key.toString());
			e.printStackTrace();
		}});
		mergeCopyWithParentPresets();
		resetCache();
		setup = true;
	}
	
	protected void mergeCopyWithParentPresets() {
		LOGGER.info("MERGING COPYS WITH PARENT PRESETS "+getName());
		presetMap.forEach((id, preset) -> mergeWithParent(id, preset));
		presetMap.forEach((id, preset) -> { 
			if (preset.isCopy()) presetMap.put(id, 
				preset.getType().createStats(preset.getKey(), preset.copyJsonData()));
		});
	}
	
	protected void mergeWithParent(String id, T preset) {
		if (!preset.isCopy() || preset.hasBeenMerged()) return;
		if (!has(preset.getCopyId())) {
			LOGGER.warn("ERROR: Preset "+preset.getCopyId()+" does not exist so "+id+" can't be merged!");
			return;
		}
		T copy = get(preset.getCopyId());
		if (copy.isCopy() && !copy.hasBeenMerged()) 
			mergeWithParent(copy.getId(), copy);
		if (!preset.mergeWithParent(copy)) {
			LOGGER.warn("MERGE FAIL: "+id+" with "+copy.getId());
			return;
		}
		LOGGER.info("MERGED: "+id+" with "+copy.getId());
	}
	
	@Nullable
	public T getFromJson(ResourceLocation key, JsonObject json) {
		if (!json.has("presetType")) return null;
		String presetType = json.get("presetType").getAsString();
		JsonPresetType type = typeMap.get(presetType);
		if (type == null) {
			LOGGER.warn("ERROR: Preset Type "+presetType+" has not been registered!");
			return null;
		}
		return type.createStats(key, json);
	}
	/**
	 * to add a custom preset type, call this during a reload event
	 * @param type
	 */
	public void addPresetType(JsonPresetType type) {
		typeMap.put(type.getId(), type);
	}
	
	protected abstract void registerPresetTypes();
	
	public void writeToBuffer(FriendlyByteBuf buffer) {
		buffer.writeInt(getNum());
		presetMap.forEach((id, preset) -> {
			buffer.writeUtf(preset.getKey().toString());
			buffer.writeUtf(preset.getJsonData().toString());
		});
	}
	
	public void readBuffer(FriendlyByteBuf buffer) {
		LOGGER.debug("RECIEVING DATA FROM SERVER "+getName());
		setup = false;
		int length = buffer.readInt();
		for (int i = 0; i < length; ++i) {
			String key_string = buffer.readUtf();
			String json_string = buffer.readUtf();
			ResourceLocation key = new ResourceLocation(key_string);
			JsonObject json = UtilParse.GSON.fromJson(json_string, JsonObject.class);
			T data = getFromJson(key, json);
			LOGGER.debug("ADD: "+key.toString());
			presetMap.put(data.getId(), data);
		}
		resetCache();
		setup = true;
	}
	
	public void sort(List<T> presets) {
		Collections.sort(presets, (a, b) -> a.compare(b));
	}
	
	public void sort(T[] presets) {
		Arrays.sort(presets, (a, b) -> a.compare(b));
	}

}
