package com.onewhohears.dscombat.data.jsonpreset;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.vehicle.VehiclePresets;
import org.slf4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.util.UtilParse;

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
public abstract class JsonPresetReloadListener<T extends JsonPreset> extends SimpleJsonResourceReloadListener {
	
	protected final Logger LOGGER = LogUtils.getLogger();
	protected final Map<String, T> presetMap = new HashMap<>();
	protected boolean setup = false;
	
	public JsonPresetReloadListener(String directory) {
		super(UtilParse.GSON, directory);
	}
	
	/**
	 * @param id same as name in the builder
	 * @return a new instance of preset. null if none have id.
	 */
	@Nullable
	public T getPreset(String id) {
		if (!has(id)) return null;
		return presetMap.get(id).copy();
	}
	
	@Nullable
	public T getPresetNonCopy(String id) {
		if (!has(id)) return null;
		return presetMap.get(id);
	}
	
	public boolean has(String id) {
		return presetMap.containsKey(id);
	}
	
	public abstract T[] getAllPresets();
	
	public String[] getPresetIds() {
		String[] names = new String[getAllPresets().length];
		for (int i = 0; i < names.length; ++i) 
			names[i] = getAllPresets()[i].getId();
		return names;
	}
	
	protected abstract void resetCache();
	
	public int getPresetNum() {
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
	}
	
	protected void mergeWithParent(String id, T preset) {
		if (!preset.isCopy() || preset.hasBeenMerged()) return;
		if (!has(preset.getCopyId())) {
			LOGGER.warn("ERROR: Preset "+preset.getCopyId()+" does not exist so "+id+" can't be merged!");
			return;
		}
		T copy = getPresetNonCopy(preset.getCopyId());
		if (copy.isCopy() && !copy.hasBeenMerged()) 
			mergeWithParent(copy.getId(), copy);
		if (!preset.mergeWithParent(copy)) {
			LOGGER.warn("MERGE FAIL: "+id+" with "+copy.getId());
			return;
		}
		LOGGER.info("MERGED: "+id+" with "+copy.getId());
	}
	
	public abstract T getFromJson(ResourceLocation key, JsonObject json);
	
	public void writeToBuffer(FriendlyByteBuf buffer) {
		buffer.writeInt(getPresetNum());
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
