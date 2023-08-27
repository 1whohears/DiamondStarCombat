package com.onewhohears.dscombat.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

/**
 * a json file from asset pack reader for this mod's asset system.
 * see {@link com.onewhohears.dscombat.data.aircraft.AircraftClientPresets} for examples.
 * 
 * see {@link JsonPresetGenerator} for a way to generate json assets.
 * 
 * @author 1whohears
 * @param <T> the type of preset this reader builds from json files
 */
public abstract class JsonPresetAssetReader<T extends JsonPreset> implements ResourceManagerReloadListener {
	
	protected final Map<String, T> presetMap = new HashMap<>();
	protected final String directory;
	
	public JsonPresetAssetReader(String directory) {
		this.directory = directory;
	}
	
	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		System.out.println("RELOAD ASSET: "+directory);
		presetMap.clear();
		manager.listResources(directory, (key) -> {
            return key.getPath().endsWith(".json");
		}).forEach((key, resource) -> {
			System.out.println("key = "+key);
			try {
				T data = getPresetFromResource(key, resource);
				if (!presetMap.containsKey(data.getId())) presetMap.put(data.getId(), data);
				else {
					System.out.println("ERROR: Can't have 2 presets with the same name! "+key.toString());
				}
			} catch (IOException e) {
				System.out.println("ERROR: SKIPPING "+key.toString());
				e.printStackTrace();
			}
		});
		resetCache();
	}
	
	/**
	 * @param id same as name in the builder
	 * @return a new instance of preset. null if none have id.
	 */
	@Nullable
	public T getPreset(String id) {
		if (!presetMap.containsKey(id)) return null;
		return presetMap.get(id).copy();
	}
	
	public abstract T getPresetFromResource(ResourceLocation key, Resource resource) throws IOException;
	
	public abstract T[] getAllPresets();
	
	protected abstract void resetCache();

}
