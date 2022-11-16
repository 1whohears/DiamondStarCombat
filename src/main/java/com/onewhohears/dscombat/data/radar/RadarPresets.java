package com.onewhohears.dscombat.data.radar;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;

public class RadarPresets {
	
	public static List<RadarData> radars = new ArrayList<RadarData>();
	public static List<CompoundTag> radarNbt = new ArrayList<CompoundTag>();
	
	public static void setupPresets() {
		// TODO make radar presets json file
		
		RadarData test_air = new RadarData("test_air", 1500, 70, 20);
		test_air.setScanAircraft(true);
		test_air.setScanPlayers(true);
		test_air.setScanMobs(false);
		test_air.setScanAir(true);
		test_air.setScanGround(false);
		add(test_air);
		
		RadarData test_ground = new RadarData("test_ground", 300, -1, 20);
		test_ground.setScanAircraft(true);
		test_ground.setScanPlayers(true);
		test_ground.setScanMobs(true);
		test_ground.setScanAir(false);
		test_ground.setScanGround(true);
		add(test_ground);
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
