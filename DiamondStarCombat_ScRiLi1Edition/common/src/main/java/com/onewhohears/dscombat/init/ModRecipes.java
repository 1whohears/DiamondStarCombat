package com.onewhohears.dscombat.init;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.crafting.*;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ModRecipes {
	
	public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(
            DSCombatMod.MODID, Registries.RECIPE_SERIALIZER);
	
	public static final RegistrySupplier<SimpleRecipeSerializer<WeaponPartLoadRecipe>> WEAPON_PART_LOAD = RECIPES.register("weapon_part_load_recipe",
            () -> new SimpleRecipeSerializer<>(WeaponPartLoadRecipe::new));
	public static final RegistrySupplier<SimpleRecipeSerializer<WeaponPartUnloadRecipe>> WEAPON_PART_UNLOAD = RECIPES.register("weapon_part_unload_recipe", 
			() -> new SimpleRecipeSerializer<>(WeaponPartUnloadRecipe::new));
	public static final RegistrySupplier<SimpleRecipeSerializer<FuelTankLoadRecipe>> FUEL_TANK_LOAD = RECIPES.register("fuel_tank_load_recipe", 
			() -> new SimpleRecipeSerializer<>(FuelTankLoadRecipe::new));
	public static final RegistrySupplier<SimpleRecipeSerializer<FlareDispenserLoadRecipe>> FLARE_LOAD = RECIPES.register("flare_load_recipe", 
			() -> new SimpleRecipeSerializer<>(FlareDispenserLoadRecipe::new));
	public static final RegistrySupplier<SimpleRecipeSerializer<TurretLoadRecipe>> TURRET_LOAD = RECIPES.register("turret_load_recipe", 
			() -> new SimpleRecipeSerializer<>(TurretLoadRecipe::new));
	public static final RegistrySupplier<SimpleRecipeSerializer<TurretUnloadRecipe>> TURRET_UNLOAD = RECIPES.register("turret_unload_recipe", 
			() -> new SimpleRecipeSerializer<>(TurretUnloadRecipe::new));
	public static final RegistrySupplier<SimpleRecipeSerializer<VehiclePartRepairRecipe>> VEHICLE_PART_REPAIR = RECIPES.register("vehicle_part_repair", 
			() -> new SimpleRecipeSerializer<>(VehiclePartRepairRecipe::new));
	
	public static final RegistrySupplier<RecipeSerializer<VehicleRecipe>> AIRCRAFT_RECIPE_SERIALIZER =
			RECIPES.register(VehicleRecipe.Serializer.ID.getPath(), () -> VehicleRecipe.Serializer.INSTANCE);
	public static final RegistrySupplier<RecipeSerializer<WeaponRecipe>> WEAPON_RECIPE_SERIALIZER = 
			RECIPES.register(WeaponRecipe.Serializer.ID.getPath(), () -> WeaponRecipe.Serializer.INSTANCE);
	public static final RegistrySupplier<RecipeSerializer<WeaponPartRecipe>> WEAPON_PART_RECIPE_SERIALIZER =
			RECIPES.register(WeaponPartRecipe.Serializer.ID.getPath(), () -> WeaponPartRecipe.Serializer.INSTANCE);
	public static final RegistrySupplier<RecipeSerializer<BucketConvertRecipe>> BUCKET_CONVERT_RECIPE_SERIALIZER = 
			RECIPES.register(BucketConvertRecipe.Serializer.ID.getPath(), () -> BucketConvertRecipe.Serializer.INSTANCE);

    public static void register() {
        RECIPES.register();
    }

    public static class SimpleRecipeSerializer<R extends Recipe<?>> implements RecipeSerializer<R> {
        private final Function<ResourceLocation, R> constructor;
        public SimpleRecipeSerializer(Function<ResourceLocation, R> constructor) {
            this.constructor = constructor;
        }
        @Override
        public @NotNull R fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            return constructor.apply(resourceLocation);
        }
        @Override
        public @NotNull R fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buffer) {
            return constructor.apply(resourceLocation);
        }
        @Override
        public void toNetwork(FriendlyByteBuf buffer, R recipe) {

        }
    }
	
}
