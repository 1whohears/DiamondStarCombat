package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.crafting.WeaponRecipe;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetReloadListener;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

public class WeaponPresets extends JsonPresetReloadListener<WeaponData> {
	
	private static WeaponPresets instance;
	
	public static WeaponPresets get() {
		if (instance == null) instance = new WeaponPresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private Map<String, List<String>> compatiblePartMap = new HashMap<>();
	private Map<String, List<String>> compatibleTurretMap = new HashMap<>();
	private WeaponData[] weaponList;
	private WeaponRecipe[] weaponRecipes;
	
	public WeaponPresets() {
		super("weapons");
	}
	
	@Override
	@Nullable
	public WeaponData getPreset(String id) {
		String updatedId = getUpdatedId(id);
		if (updatedId == null) return null;
		WeaponData data = presetMap.get(updatedId);
		if (data != null) return data.copy();
		return null;
	}
	
	public String getUpdatedId(String id) {
		if (id == null) return null;
		if (presetMap.containsKey(id)) return id;
		if (id.equals("torpedo1")) return "mk13";
		if (id.equals("rifel1")) return "agm88g";
		return null;
	}
	
	@Override
	public boolean has(String id) {
		return getUpdatedId(id) != null;
	}
	
	@Override
	public WeaponData[] getAllPresets() {
		if (weaponList == null) {
			weaponList = presetMap.values().toArray(new WeaponData[presetMap.size()]);
			sort(weaponList);
		}
		return weaponList;
	}
	
	public WeaponRecipe[] getWeaponRecipes(RecipeManager recipeManager) {
		if (weaponRecipes == null) {
			List<WeaponRecipe> list = new ArrayList<>();
			List<WeaponRecipe> wRecipes = recipeManager.getAllRecipesFor(WeaponRecipe.Type.INSTANCE);
			for (WeaponRecipe recipe : wRecipes) list.add(recipe);
			weaponRecipes = list.toArray(new WeaponRecipe[list.size()]);
			sort(weaponRecipes);
		}
		return weaponRecipes;
	}
	
	public void sort(WeaponRecipe[] recipes) {
		Arrays.sort(recipes, (a, b) -> a.compare(b));
	}
	
	public int getWeaponRecipeNum() {
		if (weaponRecipes == null) return 0;
		return weaponRecipes.length;
	}
	
	public List<String> getCompatibleWeapons(ResourceLocation weaponPartItemId) {
		List<String> list = compatiblePartMap.get(weaponPartItemId.toString());
		if (list == null) return new ArrayList<>();
		return list;
	}
	
	public List<String> getTurretWeapons(ResourceLocation turretItemId) {
		List<String> list = compatibleTurretMap.get(turretItemId.toString());
		if (list == null) return new ArrayList<>();
		return list;
	}
	
	@Override
	protected void resetCache() {
		weaponList = null;
		weaponRecipes = null;
		refreshCompatibility();
	}
	
	protected void refreshCompatibility() {
		compatiblePartMap.clear();
		for (int i = 0; i < getAllPresets().length; ++i) {
			String partId = getAllPresets()[i].getCompatibleWeaponPart();
			if (partId.isEmpty()) continue;
			List<String> list = compatiblePartMap.get(partId);
			if (list == null) {
				list = new ArrayList<String>();
				compatiblePartMap.put(partId, list);
			}
			list.add(getAllPresets()[i].getId());
		}
		LOGGER.debug("WEAPON CAPATIBILITY: "+compatiblePartMap.toString());
		compatibleTurretMap.clear();
		for (int i = 0; i < getAllPresets().length; ++i) {
			String turretId = getAllPresets()[i].getCompatibleTurret();
			if (turretId.isEmpty()) continue;
			List<String> list = compatibleTurretMap.get(turretId);
			if (list == null) {
				list = new ArrayList<String>();
				compatibleTurretMap.put(turretId, list);
			}
			list.add(getAllPresets()[i].getId());
		}
		LOGGER.debug("TURRET CAPATIBILITY: "+compatibleTurretMap.toString());
	}
	
	@Nullable
	public WeaponData getFromJson(ResourceLocation key, JsonObject json) {
		String id = json.get("type").getAsString();
		WeaponData.WeaponType type = WeaponData.WeaponType.getById(id);
		switch (type) {
		case BOMB:
			return new BombData(key, json);
		case BULLET:
			return new BulletData(key, json);
		case IR_MISSILE:
			return new IRMissileData(key, json);
		case POS_MISSILE:
			return new PosMissileData(key, json);
		case TRACK_MISSILE:
			return new TrackMissileData(key, json);
		case ANTIRADAR_MISSILE:
			return new AntiRadarMissileData(key, json);
		case TORPEDO:
			return new TorpedoData(key, json);
		case BUNKER_BUSTER:
			return new BunkerBusterData(key, json);
		case NONE:
			return null;
		}
		return null;
	}
	
}
