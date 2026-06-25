package com.onewhohears.dscombat.data.vehicle.client;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetAssetReader;
import com.onewhohears.onewholibs.data.jsonpreset.PresetStatsHolder;

public class VehicleClientPresets extends JsonPresetAssetReader<VehicleClientStats> {
	
	private static VehicleClientPresets instance;
	
	public static VehicleClientPresets get() {
		if (instance == null) instance = new VehicleClientPresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	@Override
	public VehicleClientStats get(String id) {
		VehicleClientStats stats = super.get(id);
		if (stats != null) return stats;
		return addNew(id);
	}

	@Override
	public PresetStatsHolder<VehicleClientStats> getHolder(String id) {
		if (!has(id)) addNew(id);
		return super.getHolder(id);
	}
	
	protected VehicleClientStats addNew(String id) {
		VehicleClientStats stats = VehicleClientStats.Builder.create(DSCombatMod.MODID, id).build();
		presetMap.put(id, stats);
		return stats;
	}
	
	public VehicleClientPresets() {
		super("vehicle_client");
	}
	
	@Override
	protected void registerPresetTypes() {
		addPresetType(VehicleClientType.STANDARD);
	}

	@Override
	public VehicleClientStats[] getNewArray(int i) {
		return new VehicleClientStats[i];
	}

	@Override
	protected void resetCache() {

	}

}
