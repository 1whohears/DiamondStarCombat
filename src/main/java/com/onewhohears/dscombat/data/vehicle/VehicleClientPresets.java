package com.onewhohears.dscombat.data.vehicle;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetAssetReader;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

public class VehicleClientPresets extends JsonPresetAssetReader<VehicleClientPreset> {
	
	private static VehicleClientPresets instance;
	
	public static VehicleClientPresets get() {
		if (instance == null) instance = new VehicleClientPresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private VehicleClientPreset[] presets;
	
	@Override
	public VehicleClientPreset getPresetFromResource(ResourceLocation key, Resource resource) throws IOException {
		JsonObject json = UtilParse.GSON.fromJson(resource.openAsReader(), JsonObject.class);
		return new VehicleClientPreset(key, json);
	}
	
	public VehicleClientPresets() {
		super("aircraft_client");
	}

	@Override
	public VehicleClientPreset[] getAllPresets() {
		if (presets == null) {
			presets = presetMap.values().toArray(new VehicleClientPreset[presetMap.size()]);
		}
		return presets;
	}

	@Override
	protected void resetCache() {
		presets = null;
	}

}
