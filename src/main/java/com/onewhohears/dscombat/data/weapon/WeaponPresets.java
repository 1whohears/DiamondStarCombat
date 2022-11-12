package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;

public class WeaponPresets {
	
	public static final String[] TEST_MISSILE_RACK = new String[] 
			{"aim7e","aim7mh","aim9x","aim120b","aim120c","agm65g","agm65l","agm84e","agm114k"};
	public static final String[] TEST_BIG_GUN = new String[] 
			{"bullet_1","bullet_2"};
	
	public static List<WeaponData> weapons = new ArrayList<WeaponData>();
	public static List<CompoundTag> weaponNbt = new ArrayList<CompoundTag>();
	
	public static void setupPresets() {						
		String dir = "/data/dscombat/weapons/";
		JsonArray ja = UtilParse.getJsonFromResource(dir+"weapons.json").get("weapons").getAsJsonArray();
		for (int i = 0; i < ja.size(); ++i) add(UtilParse.getCompoundFromJsonResource(dir+ja.get(i).getAsString()));
		
		defaultBullet = (BulletData) getById("bullet_1");
		defaultMissile = (MissileData) getById("aim120b");
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
		for (CompoundTag w : weaponNbt) if (w.getString("id").equals(id)) return w.copy();
		return null;
	}
	
	@Nullable
	public static WeaponData getById(String id) {
		for (WeaponData w : weapons) if (w.getId().equals(id)) return w.copy();
		return null;
	}
	
	private static BulletData defaultBullet;
	private static MissileData defaultMissile;
	
	public static BulletData getDefaultBullet() {
		return defaultBullet;
	}
	
	public static MissileData getDefaultMissile() {
		return defaultMissile;
	}
	
}
