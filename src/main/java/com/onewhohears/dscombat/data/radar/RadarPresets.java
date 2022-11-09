package com.onewhohears.dscombat.data.radar;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;

public class RadarPresets {
	
	public static List<RadarData> radars = new ArrayList<RadarData>();
	public static List<CompoundTag> radarNbt = new ArrayList<CompoundTag>();
	
	public static void setupPresets() {
		// TODO add radar presets
	}
	
	public static void add(RadarData data) {
		radars.add(data);
		radarNbt.add(data.write());
	}
	
	@Nullable
	public static CompoundTag getNbtById(String id) {
		for (CompoundTag w : radarNbt) if (w.getString("id").equals(id)) return w.copy();
		return null;
	}
	
	@Nullable
	public static RadarData getById(String id) {
		for (RadarData r : radars) if (r.getId().equals(id)) return r.copy();
		return null;
	}
	
}
