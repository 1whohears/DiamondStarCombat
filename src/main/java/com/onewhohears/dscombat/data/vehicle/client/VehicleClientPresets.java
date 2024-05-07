package com.onewhohears.dscombat.data.vehicle.client;

import com.onewhohears.dscombat.data.jsonpreset.JsonPresetAssetReader;

public class VehicleClientPresets extends JsonPresetAssetReader<VehicleClientStats> {
	
	private static VehicleClientPresets instance;
	
	public static VehicleClientPresets get() {
		if (instance == null) instance = new VehicleClientPresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private VehicleClientStats[] presets;
	
	public VehicleClientPresets() {
		super("aircraft_client");
	}
	
	@Override
	protected void registerPresetTypes() {
		addPresetType(VehicleClientType.STANDARD);
	}

	@Override
	public VehicleClientStats[] getAll() {
		if (presets == null) {
			presets = presetMap.values().toArray(new VehicleClientStats[presetMap.size()]);
		}
		return presets;
	}

	@Override
	protected void resetCache() {
		presets = null;
	}

}
