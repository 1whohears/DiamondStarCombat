package com.onewhohears.dscombat.data.aircraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.onewhohears.dscombat.crafting.AircraftRecipe;
import com.onewhohears.dscombat.data.aircraft.stats.VehicleStats;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetReloadListener;

import net.minecraft.world.item.crafting.RecipeManager;

public class AircraftPresets extends JsonPresetReloadListener<VehicleStats> {
	
	private static AircraftPresets instance;
	
	public static AircraftPresets get() {
		if (instance == null) instance = new AircraftPresets();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private VehicleStats[] allPresets;
	private AircraftRecipe[] tanks, helis, planes, boats;
	
	public AircraftPresets() {
		super("vehicle");
	}
	
	@Override
	protected void registerPresetTypes() {
		addPresetType(VehicleType.PLANE);
		addPresetType(VehicleType.HELICOPTER);
		addPresetType(VehicleType.CAR);
		addPresetType(VehicleType.BOAT);
		addPresetType(VehicleType.SUBMARINE);
	}
	
	public VehicleStats[] getAll() {
		if (allPresets == null) {
			allPresets = presetMap.values().toArray(new VehicleStats[presetMap.size()]);
		}
		return allPresets;
	}
	
	public AircraftRecipe[] getTankRecipes(RecipeManager recipeManager) {
		if (tanks == null) {
			List<AircraftRecipe> list = new ArrayList<>();
			List<AircraftRecipe> aircraftRecipes = recipeManager.getAllRecipesFor(AircraftRecipe.Type.INSTANCE);
			for (AircraftRecipe recipe : aircraftRecipes) if (recipe.getVehicleStats().isTank()) list.add(recipe);
			tanks = list.toArray(new AircraftRecipe[list.size()]);
			sort(tanks);
		}
		return tanks;
	}
	
	public AircraftRecipe[] getHeliRecipes(RecipeManager recipeManager) {
		if (helis == null) {
			List<AircraftRecipe> list = new ArrayList<>();
			List<AircraftRecipe> aircraftRecipes = recipeManager.getAllRecipesFor(AircraftRecipe.Type.INSTANCE);
			for (AircraftRecipe recipe : aircraftRecipes) if (recipe.getVehicleStats().isHeli()) list.add(recipe);
			helis = list.toArray(new AircraftRecipe[list.size()]);
			sort(helis);
		}
		return helis;
	}
	
	public AircraftRecipe[] getPlaneRecipes(RecipeManager recipeManager) {
		if (planes == null) {
			List<AircraftRecipe> list = new ArrayList<>();
			List<AircraftRecipe> aircraftRecipes = recipeManager.getAllRecipesFor(AircraftRecipe.Type.INSTANCE);
			for (AircraftRecipe recipe : aircraftRecipes) if (recipe.getVehicleStats().isPlane()) list.add(recipe);
			planes = list.toArray(new AircraftRecipe[list.size()]);
			sort(planes);
		}
		return planes;
	}

	public AircraftRecipe[] getBoatRecipes(RecipeManager recipeManager) {
		if (boats == null) {
			List<AircraftRecipe> list = new ArrayList<>();
			List<AircraftRecipe> aircraftRecipes = recipeManager.getAllRecipesFor(AircraftRecipe.Type.INSTANCE);
			for (AircraftRecipe recipe : aircraftRecipes) if (recipe.getVehicleStats().isBoat()) list.add(recipe);
			boats = list.toArray(new AircraftRecipe[list.size()]);
			sort(boats);
		}
		return boats;
	}
	
	public void sort(AircraftRecipe[] recipes) {
		Arrays.sort(recipes, (a, b) -> a.compare(b));
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
