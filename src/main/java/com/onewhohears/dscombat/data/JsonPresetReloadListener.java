package com.onewhohears.dscombat.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

/**
 * a json file from datapacks reader for this mod's preset system.
 * comes with ways to sort the presets and synch the preset info with the client.
 * see {@link com.onewhohears.dscombat.data.aircraft.AircraftPresets},
 * {@link com.onewhohears.dscombat.data.weapon.WeaponPresets},
 * and {@link com.onewhohears.dscombat.data.radar.RadarPresets} for examples.
 * 
 * see {@link JsonPresetGenerator} for a way to generate json presets.
 * 
 * @author 1whohears
 * @param <T> the type of preset this reader builds from json files
 */
public abstract class JsonPresetReloadListener<T extends JsonPreset> extends SimpleJsonResourceReloadListener {
	
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
	
	public boolean has(String id) {
		return presetMap.containsKey(id);
	}
	
	public abstract T[] getAllPresets();
	
	protected abstract void resetCache();
	
	public int getPresetNum() {
		return presetMap.size();
	}
	
	public boolean isSetup() {
		return setup;
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
		System.out.println("APPLYING PRESETS TO COMMON CACHE "+getName());
		setup = false;
		presetMap.clear();
		map.forEach((key, je) -> { try {
			System.out.println("ADD: "+key.toString()/*+" "+je.toString()*/);
			JsonObject json = UtilParse.GSON.fromJson(je, JsonObject.class);
			T data = getFromJson(key, json);
			if (data == null) {
				System.out.println("ERROR: failed to parse preset "+key.toString());
				return;
			}
			if (!presetMap.containsKey(data.getId())) presetMap.put(data.getId(), data);
			else {
				System.out.println("ERROR: Can't have 2 presets with the same name! "+key.toString());
			}
		} catch (Exception e) {
			System.out.println("ERROR: SKIPPING "+key.toString());
			e.printStackTrace();
		}});
		resetCache();
		setup = true;
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
		System.out.println("RECIEVING DATA FROM SERVER "+getName());
		setup = false;
		int length = buffer.readInt();
		for (int i = 0; i < length; ++i) {
			String key_string = buffer.readUtf();
			String json_string = buffer.readUtf();
			ResourceLocation key = new ResourceLocation(key_string);
			JsonObject json = UtilParse.GSON.fromJson(json_string, JsonObject.class);
			T data = getFromJson(key, json);
			System.out.println("ADD: "+data.toString());
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
