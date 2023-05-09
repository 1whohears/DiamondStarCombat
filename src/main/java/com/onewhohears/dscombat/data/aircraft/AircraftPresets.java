package com.onewhohears.dscombat.data.aircraft;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.JsonPresetReloadListener;

import net.minecraft.resources.ResourceLocation;

public class AircraftPresets extends JsonPresetReloadListener<AircraftPreset> {
	
	private static AircraftPresets instance;
	
	public static AircraftPresets get() {
		if (instance == null) instance = new AircraftPresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private AircraftPreset[] allPresets;
	private AircraftPreset[] craftablePresets;
	
	public AircraftPresets() {
		super("aircraft");
	}
	
	public AircraftPreset[] getAllPresets() {
		if (allPresets == null) {
			allPresets = presetMap.values().toArray(new AircraftPreset[presetMap.size()]);
			// FIXME 4.1 sort by vehicle type the alphabetically
		}
		return allPresets;
	}
	
	public AircraftPreset[] getCraftablePresets() {
		if (craftablePresets == null) {
			List<AircraftPreset> list = new ArrayList<>();
			presetMap.forEach((name, preset) -> {
				if (preset.isCraftable()) list.add(preset);
			});
			craftablePresets = list.toArray(new AircraftPreset[list.size()]);
		}
		return craftablePresets;
	}
	
	public int getCraftablePresetNum() {
		return getCraftablePresets().length;
	}

	@Override
	public AircraftPreset getFromJson(ResourceLocation key, JsonObject json) {
		return new AircraftPreset(key, json);
	}

	@Override
	protected void resetCache() {
		allPresets = null;
		craftablePresets = null;
	}
	
}
