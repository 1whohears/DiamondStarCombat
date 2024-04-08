package com.onewhohears.dscombat.data.aircraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.crafting.AircraftRecipe;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetReloadListener;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

public class AircraftPresets extends JsonPresetReloadListener<AircraftPreset> {
	
	private static AircraftPresets instance;
	
	public static AircraftPresets get() {
		if (instance == null) instance = new AircraftPresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private AircraftPreset[] allPresets;
	private AircraftRecipe[] tanks, helis, planes, boats;
	
	public AircraftPresets() {
		super("aircraft");
	}
	
	public AircraftPreset[] getAllPresets() {
		if (allPresets == null) {
			allPresets = presetMap.values().toArray(new AircraftPreset[presetMap.size()]);
		}
		return allPresets;
	}
	
	public AircraftRecipe[] getTankRecipes(RecipeManager recipeManager) {
		if (tanks == null) {
			List<AircraftRecipe> list = new ArrayList<>();
			List<AircraftRecipe> aircraftRecipes = recipeManager.getAllRecipesFor(AircraftRecipe.Type.INSTANCE);
			for (AircraftRecipe recipe : aircraftRecipes) if (recipe.getAircraftType().isTank()) list.add(recipe);
			tanks = list.toArray(new AircraftRecipe[list.size()]);
			sort(tanks);
		}
		return tanks;
	}
	
	public AircraftRecipe[] getHeliRecipes(RecipeManager recipeManager) {
		if (helis == null) {
			List<AircraftRecipe> list = new ArrayList<>();
			List<AircraftRecipe> aircraftRecipes = recipeManager.getAllRecipesFor(AircraftRecipe.Type.INSTANCE);
			for (AircraftRecipe recipe : aircraftRecipes) if (recipe.getAircraftType().isHeli()) list.add(recipe);
			helis = list.toArray(new AircraftRecipe[list.size()]);
			sort(helis);
		}
		return helis;
	}
	
	public AircraftRecipe[] getPlaneRecipes(RecipeManager recipeManager) {
		if (planes == null) {
			List<AircraftRecipe> list = new ArrayList<>();
			List<AircraftRecipe> aircraftRecipes = recipeManager.getAllRecipesFor(AircraftRecipe.Type.INSTANCE);
			for (AircraftRecipe recipe : aircraftRecipes) if (recipe.getAircraftType().isPlane()) list.add(recipe);
			planes = list.toArray(new AircraftRecipe[list.size()]);
			sort(planes);
		}
		return planes;
	}

	public AircraftRecipe[] getBoatRecipes(RecipeManager recipeManager) {
		if (boats == null) {
			List<AircraftRecipe> list = new ArrayList<>();
			List<AircraftRecipe> aircraftRecipes = recipeManager.getAllRecipesFor(AircraftRecipe.Type.INSTANCE);
			for (AircraftRecipe recipe : aircraftRecipes) if (recipe.getAircraftType().isBoat()) list.add(recipe);
			boats = list.toArray(new AircraftRecipe[list.size()]);
			sort(boats);
		}
		return boats;
	}
	
	public void sort(AircraftRecipe[] recipes) {
		Arrays.sort(recipes, (a, b) -> a.compare(b));
	}

	@Override
	public AircraftPreset getFromJson(ResourceLocation key, JsonObject json) {
		return new AircraftPreset(key, json);
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
