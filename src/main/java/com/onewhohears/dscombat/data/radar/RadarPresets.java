package com.onewhohears.dscombat.data.radar;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;

public class RadarPresets {
	
	public static List<RadarData> radars = new ArrayList<RadarData>();
	public static List<CompoundTag> radarNbt = new ArrayList<CompoundTag>();
	
	public static void setupPresets() {
		String dir = "/data/dscombat/radars/";
		JsonObject jo = UtilParse.getJsonFromResource(dir+"radars.json");
		
		JsonArray jar = jo.get("radars").getAsJsonArray();
		for (int i = 0; i < jar.size(); ++i) add(UtilParse.getCompoundFromJson(jar.get(i).getAsJsonObject()));
	}
	
	public static void add(RadarData data) {
		radars.add(data);
		radarNbt.add(data.write());
	}
	
	public static void add(CompoundTag tag) {
		RadarData data = new RadarData(tag);
		radars.add(data);
		radarNbt.add(tag);
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
