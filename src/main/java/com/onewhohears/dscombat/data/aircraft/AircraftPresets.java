package com.onewhohears.dscombat.data.aircraft;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class AircraftPresets {
	
	private static final Map<String, AircraftPreset> presets = new HashMap<>();
	
	@Nullable
	public static AircraftPreset getAircraftPreset(String id) {
		return presets.get(id);
	}
	
	public static AircraftPreset[] getPresets() {
		return presets.values().toArray(new AircraftPreset[presets.size()]);
	}
	
	public static int getPresetNum() {
		return presets.size();
	}
	
	public static void resetCachedPresets(ResourceManager manager, boolean isClient) {
		System.out.println("RESETING CACHE isClient? "+isClient);
		presets.clear();
		Map<ResourceLocation, Resource> m = manager.listResources("aircraft", (key) -> {
			return key.getPath().endsWith(".json");
		});
		m.forEach((key, res) -> {
			JsonObject json = UtilParse.getJsonFromResource(res);
			AircraftPreset preset = new AircraftPreset(key, json);
			if (!presets.containsKey(preset.getPresetId())) {
				System.out.println("ADDING PRESET TO CACHE "+key.toString()+" "+json.toString());
				presets.put(preset.getPresetId(), preset);
			} else {
				System.out.println("ERROR: Can't have 2 presets with the same name! "+key.toString());
			}
		});
	}
	
}
