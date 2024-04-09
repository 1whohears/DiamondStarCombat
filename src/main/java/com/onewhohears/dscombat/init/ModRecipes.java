package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.crafting.AircraftRecipe;
import com.onewhohears.dscombat.crafting.FlareDispenserLoadRecipe;
import com.onewhohears.dscombat.crafting.FuelTankLoadRecipe;
import com.onewhohears.dscombat.crafting.OilBucketRecipe;
import com.onewhohears.dscombat.crafting.TurretLoadRecipe;
import com.onewhohears.dscombat.crafting.WeaponPartLoadRecipe;
import com.onewhohears.dscombat.crafting.WeaponPartUnloadRecipe;
import com.onewhohears.dscombat.crafting.WeaponRecipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
	
	public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DSCombatMod.MODID);
	
	public static void register(IEventBus eventBus) {
		RECIPES.register(eventBus);
	}
	
	public static final RegistryObject<SimpleRecipeSerializer<WeaponPartLoadRecipe>> WEAPON_PART_LOAD = RECIPES.register("weapon_part_load_recipe", 
			() -> new SimpleRecipeSerializer<>(WeaponPartLoadRecipe::new));
	public static final RegistryObject<SimpleRecipeSerializer<WeaponPartUnloadRecipe>> WEAPON_PART_UNLOAD = RECIPES.register("weapon_part_unload_recipe", 
			() -> new SimpleRecipeSerializer<>(WeaponPartUnloadRecipe::new));
	public static final RegistryObject<SimpleRecipeSerializer<FuelTankLoadRecipe>> FUEL_TANK_LOAD = RECIPES.register("fuel_tank_load_recipe", 
			() -> new SimpleRecipeSerializer<>(FuelTankLoadRecipe::new));
	public static final RegistryObject<SimpleRecipeSerializer<FlareDispenserLoadRecipe>> FLARE_LOAD = RECIPES.register("flare_load_recipe", 
			() -> new SimpleRecipeSerializer<>(FlareDispenserLoadRecipe::new));
	public static final RegistryObject<SimpleRecipeSerializer<TurretLoadRecipe>> TURRET_LOAD = RECIPES.register("turret_load_recipe", 
			() -> new SimpleRecipeSerializer<>(TurretLoadRecipe::new));
	public static final RegistryObject<SimpleRecipeSerializer<OilBucketRecipe>> OIL_BUCKET = RECIPES.register("oil_bucket_recipe", 
			() -> new SimpleRecipeSerializer<>(OilBucketRecipe::new));
	
	public static final RegistryObject<RecipeSerializer<AircraftRecipe>> AIRCRAFT_RECIPE_SERIALIZER = 
			RECIPES.register(AircraftRecipe.Serializer.ID.getPath(), () -> AircraftRecipe.Serializer.INSTANCE);
	public static final RegistryObject<RecipeSerializer<WeaponRecipe>> WEAPON_RECIPE_SERIALIZER = 
			RECIPES.register(WeaponRecipe.Serializer.ID.getPath(), () -> WeaponRecipe.Serializer.INSTANCE);
	
}
