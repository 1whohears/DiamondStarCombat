package com.onewhohears.dscombat.data.aircraft.client;

import com.onewhohears.dscombat.data.jsonpreset.JsonPresetAssetReader;

public class AircraftClientPresets extends JsonPresetAssetReader<VehicleClientStats> {
	
	private static AircraftClientPresets instance;
	
	public static AircraftClientPresets get() {
		if (instance == null) instance = new AircraftClientPresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private VehicleClientStats[] presets;
	
	public AircraftClientPresets() {
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
