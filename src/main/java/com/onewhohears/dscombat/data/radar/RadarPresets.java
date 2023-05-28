package com.onewhohears.dscombat.data.radar;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.JsonPresetReloadListener;

import net.minecraft.resources.ResourceLocation;

public class RadarPresets extends JsonPresetReloadListener<RadarData> {
	
	private static RadarPresets instance;
	
	public static RadarPresets get() {
		if (instance == null) instance = new RadarPresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private RadarData[] radarList;
	
	public RadarPresets() {
		super("radars");
	}

	@Override
	public RadarData[] getAllPresets() {
		if (radarList == null) {
			radarList = presetMap.values().toArray(new RadarData[presetMap.size()]);
		}
		return radarList;
	}

	@Override
	public RadarData getFromJson(ResourceLocation key, JsonObject json) {
		return new RadarData(key, json);
	}

	@Override
	protected void resetCache() {
		radarList = null;
	}
	
}
