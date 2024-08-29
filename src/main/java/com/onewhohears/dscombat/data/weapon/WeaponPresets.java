package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.crafting.WeaponRecipe;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetReloadListener;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.RecipeManager;

public class WeaponPresets extends JsonPresetReloadListener<WeaponStats> {
	
	private static WeaponPresets instance;
	
	public static WeaponPresets get() {
		if (instance == null) instance = new WeaponPresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private final Map<String, List<String>> compatiblePartMap = new HashMap<>();
	private WeaponRecipe[] weaponRecipes;
	
	public WeaponPresets() {
		super("weapons");
	}
	
	@Override
	public void registerDefaultPresetTypes() {
		addPresetType(WeaponType.NONE);
		addPresetType(WeaponType.BOMB);
		addPresetType(WeaponType.BUNKER_BUSTER);
		addPresetType(WeaponType.BULLET);
		addPresetType(WeaponType.POS_MISSILE);
		addPresetType(WeaponType.TRACK_MISSILE);
		addPresetType(WeaponType.TORPEDO);
		addPresetType(WeaponType.IR_MISSILE);
		addPresetType(WeaponType.ANTI_RADAR_MISSILE);
	}
	
	@Override
	@Nullable
	public WeaponStats get(String id) {
		String updatedId = getUpdatedId(id);
		if (updatedId == null) return null;
		if (!has(updatedId)) return null;
        return presetMap.get(updatedId);
    }
	
	public String getUpdatedId(String id) {
		if (id == null) return null;
		if (presetMap.containsKey(id)) return id;
		if (id.equals("torpedo1")) return "mk13";
		if (id.equals("rifel1")) return "agm88g";
		return null;
	}
	
	@Override
	public WeaponStats getFromNbt(CompoundTag nbt) {
		if (nbt == null) return null;
		WeaponStats w = super.getFromNbt(nbt);
		if (w != null) return w;
		if (!nbt.contains("weaponId")) return null;
		String presetId = nbt.getString("weaponId");
		return get(presetId);
	}
	
	@Override
	public boolean has(String id) {
		return getUpdatedId(id) != null;
	}

	@Override
	public WeaponStats[] getNewArray(int i) {
		return new WeaponStats[i];
	}

	public WeaponRecipe[] getWeaponRecipes(RecipeManager recipeManager) {
		if (weaponRecipes == null) {
            List<WeaponRecipe> wRecipes = recipeManager.getAllRecipesFor(WeaponRecipe.Type.INSTANCE);
            List<WeaponRecipe> list = new ArrayList<>(wRecipes);
			weaponRecipes = list.toArray(new WeaponRecipe[list.size()]);
			sort(weaponRecipes);
		}
		return weaponRecipes;
	}
	
	public void sort(WeaponRecipe[] recipes) {
		Arrays.sort(recipes, WeaponRecipe::compare);
	}
	
	public int getWeaponRecipeNum() {
		if (weaponRecipes == null) return 0;
		return weaponRecipes.length;
	}
	
	public List<String> getCompatibleWeapons(String weaponPartId) {
		List<String> list = compatiblePartMap.get(weaponPartId);
		if (list == null) return new ArrayList<>();
		return list;
	}
	
	@Override
	protected void resetCache() {
		weaponRecipes = null;
		refreshCompatibility();
	}
	
	protected void refreshCompatibility() {
		compatiblePartMap.clear();
		for (int i = 0; i < getAll().length; ++i) {
			String[] partIds = getAll()[i].getCompatibleWeaponParts();
            for (String partId : partIds) {
                List<String> list = compatiblePartMap.computeIfAbsent(partId, k -> new ArrayList<>());
                list.add(getAll()[i].getId());
            }
		}
        LOGGER.debug("WEAPON CAPATIBILITY: {}", compatiblePartMap.toString());
	}
	
}
