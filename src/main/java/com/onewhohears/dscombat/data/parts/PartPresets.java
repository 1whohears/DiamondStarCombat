package com.onewhohears.dscombat.data.parts;

import java.util.Arrays;

import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetReloadListener;
import com.onewhohears.dscombat.data.parts.stats.PartStats;

public class PartPresets extends JsonPresetReloadListener<PartStats> {
	
	private static PartPresets instance;
	
	public static PartPresets get() {
		if (instance == null) instance = new PartPresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private PartStats[] allPresets;
	
	@Override
	public void registerDefaultPresetTypes() {
		addPresetType(PartType.BUFF);
		addPresetType(PartType.CHAFF_DISPENSER);
		addPresetType(PartType.CHAIN_HOOK);
		addPresetType(PartType.EXTERNAL_ENGINE);
		addPresetType(PartType.EXTERNAL_FUEL_TANK);
		addPresetType(PartType.EXTERNAL_RADAR);
		addPresetType(PartType.EXTERNAL_STORAGE);
		addPresetType(PartType.EXTERNAL_WEAPON);
		addPresetType(PartType.FLARE_DISPENSER);
		addPresetType(PartType.FUEL_TANK);
		addPresetType(PartType.GIMBAL);
		addPresetType(PartType.INTERNAL_ENGINE);
		addPresetType(PartType.INTERNAL_RADAR);
		addPresetType(PartType.INTERNAL_STORAGE);
		addPresetType(PartType.INTERNAL_WEAPON);
		addPresetType(PartType.SEAT);
		addPresetType(PartType.TURRENT);
	}
	
	public PartPresets() {
		super("parts");
	}

	@Override
	public PartStats[] getAll() {
		if (allPresets == null) {
			allPresets = presetMap.values().toArray(new PartStats[presetMap.size()]);
			sort(allPresets);
		}
		return allPresets;
	}

	@Override
	protected void resetCache() {
		allPresets = null;
		SlotType.updateSlotTypeChildren();
	}
	
	public void sort(PartStats[] recipes) {
		Arrays.sort(recipes, (a, b) -> a.compare(b));
	}

}
