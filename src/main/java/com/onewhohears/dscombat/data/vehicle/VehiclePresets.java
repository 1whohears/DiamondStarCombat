package com.onewhohears.dscombat.data.vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.onewhohears.dscombat.crafting.VehicleRecipe;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetReloadListener;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;

import net.minecraft.world.item.crafting.RecipeManager;

public class VehiclePresets extends JsonPresetReloadListener<VehicleStats> {
	
	private static VehiclePresets instance;
	
	public static VehiclePresets get() {
		if (instance == null) instance = new VehiclePresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}

	private VehicleRecipe[] tanks, helis, planes, boats;
	
	public VehiclePresets() {
		super("vehicle");
	}
	
	@Override
	public void registerDefaultPresetTypes() {
		addPresetType(VehicleType.PLANE);
		addPresetType(VehicleType.HELICOPTER);
		addPresetType(VehicleType.CAR);
		addPresetType(VehicleType.BOAT);
		addPresetType(VehicleType.SUBMARINE);
	}
	
	public VehicleRecipe[] getTankRecipes(RecipeManager recipeManager) {
		if (tanks == null) {
			List<VehicleRecipe> list = new ArrayList<>();
			List<VehicleRecipe> aircraftRecipes = recipeManager.getAllRecipesFor(VehicleRecipe.Type.INSTANCE);
			for (VehicleRecipe recipe : aircraftRecipes) if (recipe.getVehicleStats().isTank()) list.add(recipe);
			tanks = list.toArray(new VehicleRecipe[list.size()]);
			sort(tanks);
		}
		return tanks;
	}
	
	public VehicleRecipe[] getHeliRecipes(RecipeManager recipeManager) {
		if (helis == null) {
			List<VehicleRecipe> list = new ArrayList<>();
			List<VehicleRecipe> aircraftRecipes = recipeManager.getAllRecipesFor(VehicleRecipe.Type.INSTANCE);
			for (VehicleRecipe recipe : aircraftRecipes) if (recipe.getVehicleStats().isHeli()) list.add(recipe);
			helis = list.toArray(new VehicleRecipe[list.size()]);
			sort(helis);
		}
		return helis;
	}
	
	public VehicleRecipe[] getPlaneRecipes(RecipeManager recipeManager) {
		if (planes == null) {
			List<VehicleRecipe> list = new ArrayList<>();
			List<VehicleRecipe> aircraftRecipes = recipeManager.getAllRecipesFor(VehicleRecipe.Type.INSTANCE);
			for (VehicleRecipe recipe : aircraftRecipes) if (recipe.getVehicleStats().isPlane()) list.add(recipe);
			planes = list.toArray(new VehicleRecipe[list.size()]);
			sort(planes);
		}
		return planes;
	}

	public VehicleRecipe[] getBoatRecipes(RecipeManager recipeManager) {
		if (boats == null) {
			List<VehicleRecipe> list = new ArrayList<>();
			List<VehicleRecipe> aircraftRecipes = recipeManager.getAllRecipesFor(VehicleRecipe.Type.INSTANCE);
			for (VehicleRecipe recipe : aircraftRecipes) if (recipe.getVehicleStats().isBoat()) list.add(recipe);
			boats = list.toArray(new VehicleRecipe[list.size()]);
			sort(boats);
		}
		return boats;
	}
	
	public void sort(VehicleRecipe[] recipes) {
		Arrays.sort(recipes, VehicleRecipe::compare);
	}

	@Override
	public VehicleStats[] getNewArray(int i) {
		return new VehicleStats[i];
	}

	@Override
	protected void resetCache() {
		tanks = null;
		helis = null;
		planes = null;
		boats = null;
	}
	
}
