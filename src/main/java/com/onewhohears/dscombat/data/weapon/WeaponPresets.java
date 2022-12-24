package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;

public class WeaponPresets {
	
	public static final HashMap<String, String[]> compatibility = new HashMap<String, String[]>();
	public static final List<WeaponData> weapons = new ArrayList<WeaponData>();
	public static final List<CompoundTag> weaponNbt = new ArrayList<CompoundTag>();
	
	public static void setupPresets() {						
		String dir = "/data/dscombat/weapons/";
		JsonObject jo = UtilParse.getJsonFromResource(dir+"weapons.json");
		
		JsonArray jaw = jo.get("weapons").getAsJsonArray();
		for (int i = 0; i < jaw.size(); ++i) add(UtilParse.getCompoundFromJsonResource(dir+jaw.get(i).getAsString()));
		defaultMissile = (TrackMissileData) getById("aim120b");
		
		JsonArray jac = jo.get("compatibility").getAsJsonArray();
		for (int i = 0; i < jac.size(); ++i) {
			JsonObject joi = jac.get(i).getAsJsonObject();
			String type = joi.get("type").getAsString();
			JsonArray jai = joi.get("weapons").getAsJsonArray();
			String[] weapons = new String[jai.size()];
			for (int j = 0; j < weapons.length; ++j) weapons[j] = jai.get(j).getAsString();
			compatibility.put(type, weapons);
 		}
	}
	
	/*public static void setupPresetNbt() {
		for (WeaponData w : weapons) weaponNbt.add(w.write());
	}*/
	
	public static void add(WeaponData data) {
		System.out.println("WEAPON PRESET JAVA "+data);
		weapons.add(data);
		weaponNbt.add(data.write());
	}
	
	public static void add(CompoundTag data) {
		WeaponData wd = UtilParse.parseWeaponFromCompound(data);
		System.out.println("WEAPON PRESET JSON "+wd);
		if (wd == null) return;
		weapons.add(wd);
		weaponNbt.add(data);
	}
	
	@Nullable
	public static CompoundTag getNbtById(String id) {
		for (CompoundTag w : weaponNbt) if (w.getString("id").equals(id)) return w;
		return null;
	}
	
	@Nullable
	public static WeaponData getNewById(String id) {
		for (WeaponData w : weapons) if (w.getId().equals(id)) return w.copy();
		return null;
	}
	
	@Nullable
	public static WeaponData getById(String id) {
		for (WeaponData w : weapons) if (w.getId().equals(id)) return w;
		return null;
	}
	
	private static TrackMissileData defaultMissile;
	
	public static TrackMissileData getDefaultMissile() {
		return (TrackMissileData) defaultMissile.copy();
	}
	
	@Nullable
	public static String[] getCompatibility(String type) {
		return compatibility.get(type);
	}
	
}
