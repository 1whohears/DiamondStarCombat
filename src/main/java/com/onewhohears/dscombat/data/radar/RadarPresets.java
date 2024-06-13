package com.onewhohears.dscombat.data.radar;

import com.onewhohears.dscombat.data.jsonpreset.JsonPresetReloadListener;

import net.minecraft.nbt.CompoundTag;

public class RadarPresets extends JsonPresetReloadListener<RadarStats> {
	
	private static RadarPresets instance;
	
	public static RadarPresets get() {
		if (instance == null) instance = new RadarPresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private RadarStats[] radarList;
	
	public RadarPresets() {
		super("radars");
	}
	
	@Override
	public void registerDefaultPresetTypes() {
		addPresetType(RadarType.STANDARD);
	}
	
	@Override
	public RadarStats getFromNbt(CompoundTag nbt) {
		if (nbt == null) return null;
		RadarStats w = super.getFromNbt(nbt);
		if (w != null) return w;
		if (!nbt.contains("id")) return null;
		String presetId = nbt.getString("id");
		return get(presetId);
	}

	@Override
	public RadarStats[] getAll() {
		if (radarList == null) {
			radarList = presetMap.values().toArray(new RadarStats[presetMap.size()]);
		}
		return radarList;
	}

	@Override
	protected void resetCache() {
		radarList = null;
	}
	
}
