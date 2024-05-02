package com.onewhohears.dscombat.data.vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.crafting.VehicleRecipe;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetReloadListener;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

public class VehiclePresets extends JsonPresetReloadListener<VehiclePreset> {
	
	private static VehiclePresets instance;
	
	public static VehiclePresets get() {
		if (instance == null) instance = new VehiclePresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private VehiclePreset[] allPresets;
	private VehicleRecipe[] tanks, helis, planes, boats;
	
	public VehiclePresets() {
		super("vehicle");
	}
	
	public VehiclePreset[] getAllPresets() {
		if (allPresets == null) {
			allPresets = presetMap.values().toArray(new VehiclePreset[presetMap.size()]);
		}
		return allPresets;
	}
	
	public VehicleRecipe[] getTankRecipes(RecipeManager recipeManager) {
		if (tanks == null) {
			List<VehicleRecipe> list = new ArrayList<>();
			List<VehicleRecipe> vehicleRecipes = recipeManager.getAllRecipesFor(VehicleRecipe.Type.INSTANCE);
			for (VehicleRecipe recipe : vehicleRecipes) if (recipe.getAircraftType().isTank()) list.add(recipe);
			tanks = list.toArray(new VehicleRecipe[list.size()]);
			sort(tanks);
		}
		return tanks;
	}
	
	public VehicleRecipe[] getHeliRecipes(RecipeManager recipeManager) {
		if (helis == null) {
			List<VehicleRecipe> list = new ArrayList<>();
			List<VehicleRecipe> vehicleRecipes = recipeManager.getAllRecipesFor(VehicleRecipe.Type.INSTANCE);
			for (VehicleRecipe recipe : vehicleRecipes) if (recipe.getAircraftType().isHeli()) list.add(recipe);
			helis = list.toArray(new VehicleRecipe[list.size()]);
			sort(helis);
		}
		return helis;
	}
	
	public VehicleRecipe[] getPlaneRecipes(RecipeManager recipeManager) {
		if (planes == null) {
			List<VehicleRecipe> list = new ArrayList<>();
			List<VehicleRecipe> vehicleRecipes = recipeManager.getAllRecipesFor(VehicleRecipe.Type.INSTANCE);
			for (VehicleRecipe recipe : vehicleRecipes) if (recipe.getAircraftType().isPlane()) list.add(recipe);
			planes = list.toArray(new VehicleRecipe[list.size()]);
			sort(planes);
		}
		return planes;
	}

	public VehicleRecipe[] getBoatRecipes(RecipeManager recipeManager) {
		if (boats == null) {
			List<VehicleRecipe> list = new ArrayList<>();
			List<VehicleRecipe> vehicleRecipes = recipeManager.getAllRecipesFor(VehicleRecipe.Type.INSTANCE);
			for (VehicleRecipe recipe : vehicleRecipes) if (recipe.getAircraftType().isBoat()) list.add(recipe);
			boats = list.toArray(new VehicleRecipe[list.size()]);
			sort(boats);
		}
		return boats;
	}
	
	public void sort(VehicleRecipe[] recipes) {
		Arrays.sort(recipes, (a, b) -> a.compare(b));
	}

	@Override
	public VehiclePreset getFromJson(ResourceLocation key, JsonObject json) {
		return new VehiclePreset(key, json);
	}

	@Override
	protected void resetCache() {
		allPresets = null;
		tanks = null;
		helis = null;
		planes = null;
		boats = null;
	}
	
}
