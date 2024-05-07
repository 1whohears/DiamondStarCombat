package com.onewhohears.dscombat.data.aircraft;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetAssetReader;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

public class AircraftClientPresets extends JsonPresetAssetReader<AircraftClientPreset> {
	
	private static AircraftClientPresets instance;
	
	public static AircraftClientPresets get() {
		if (instance == null) instance = new AircraftClientPresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private AircraftClientPreset[] presets;
	
	@Override
	public AircraftClientPreset getPresetFromResource(ResourceLocation key, Resource resource) throws IOException {
		JsonObject json = UtilParse.GSON.fromJson(resource.openAsReader(), JsonObject.class);
		return new AircraftClientPreset(key, json);
	}
	
	public AircraftClientPresets() {
		super("aircraft_client");
	}

	@Override
	public AircraftClientPreset[] getAllPresets() {
		if (presets == null) {
			presets = presetMap.values().toArray(new AircraftClientPreset[presetMap.size()]);
		}
		return presets;
	}

	@Override
	protected void resetCache() {
		presets = null;
	}

}
