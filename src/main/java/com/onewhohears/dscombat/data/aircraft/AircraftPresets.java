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
	
	private AircraftPreset[] allPresets, allCraftablePresets;
	private AircraftPreset[] tanks, helis, planes, boats;
	
	public AircraftPresets() {
		super("aircraft");
	}
	
	public AircraftPreset[] getAllPresets() {
		if (allPresets == null) {
			allPresets = presetMap.values().toArray(new AircraftPreset[presetMap.size()]);
		}
		return allPresets;
	}
	
	public AircraftPreset[] getCraftablePresets() {
		if (allCraftablePresets == null) {
			List<AircraftPreset> list = new ArrayList<>();
			presetMap.forEach((name, preset) -> {
				if (preset.isCraftable()) list.add(preset);
			});
			allCraftablePresets = list.toArray(new AircraftPreset[list.size()]);
			sort(allCraftablePresets);
		}
		return allCraftablePresets;
	}
	
	public int getCraftablePresetNum() {
		return getCraftablePresets().length;
	}
	
	public AircraftPreset[] getCraftableTanks() {
		if (tanks == null) {
			List<AircraftPreset> list = new ArrayList<>();
			presetMap.forEach((name, preset) -> {
				if (preset.isCraftable() && preset.getAircraftType().isTank()) list.add(preset);
			});
			tanks = list.toArray(new AircraftPreset[list.size()]);
			sort(tanks);
		}
		return tanks;
	}
	
	public AircraftPreset[] getCraftableHelis() {
		if (helis == null) {
			List<AircraftPreset> list = new ArrayList<>();
			presetMap.forEach((name, preset) -> {
				if (preset.isCraftable() && preset.getAircraftType().isHeli()) list.add(preset);
			});
			helis = list.toArray(new AircraftPreset[list.size()]);
			sort(helis);
		}
		return helis;
	}
	
	public AircraftPreset[] getCraftablePlanes() {
		if (planes == null) {
			List<AircraftPreset> list = new ArrayList<>();
			presetMap.forEach((name, preset) -> {
				if (preset.isCraftable() && preset.getAircraftType().isPlane()) list.add(preset);
			});
			planes = list.toArray(new AircraftPreset[list.size()]);
			sort(planes);
		}
		return planes;
	}

	public AircraftPreset[] getCraftableBoats() {
		if (boats == null) {
			List<AircraftPreset> list = new ArrayList<>();
			presetMap.forEach((name, preset) -> {
				if (preset.isCraftable() && preset.getAircraftType().isBoat()) list.add(preset);
			});
			boats = list.toArray(new AircraftPreset[list.size()]);
			sort(boats);
		}
		return boats;
	}

	@Override
	public AircraftPreset getFromJson(ResourceLocation key, JsonObject json) {
		return new AircraftPreset(key, json);
	}

	@Override
	protected void resetCache() {
		allPresets = null;
		allCraftablePresets = null;
	}
	
}
