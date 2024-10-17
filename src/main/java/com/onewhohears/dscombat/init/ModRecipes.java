package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.crafting.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonObject;

public class ModRecipes {

	public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DSCombatMod.MODID);

	public static void register(IEventBus eventBus) {
		RECIPES.register(eventBus);
	}

	// Define custom serializers for each recipe
	public static final RegistryObject<RecipeSerializer<WeaponPartLoadRecipe>> WEAPON_PART_LOAD = RECIPES.register("weapon_part_load_recipe",
			() -> new RecipeSerializer<WeaponPartLoadRecipe>() {
				@Override
				public WeaponPartLoadRecipe fromJson(ResourceLocation id, JsonObject json) {
					return new WeaponPartLoadRecipe(id);
				}

				@Override
				public WeaponPartLoadRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
					return new WeaponPartLoadRecipe(id);
				}

				@Override
				public void toNetwork(FriendlyByteBuf buffer, WeaponPartLoadRecipe recipe) {
					// Implement custom serialization logic if needed
				}
			});

	public static final RegistryObject<RecipeSerializer<WeaponPartUnloadRecipe>> WEAPON_PART_UNLOAD = RECIPES.register("weapon_part_unload_recipe",
			() -> new RecipeSerializer<WeaponPartUnloadRecipe>() {
				@Override
				public WeaponPartUnloadRecipe fromJson(ResourceLocation id, JsonObject json) {
					return new WeaponPartUnloadRecipe(id);
				}

				@Override
				public WeaponPartUnloadRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
					return new WeaponPartUnloadRecipe(id);
				}

				@Override
				public void toNetwork(FriendlyByteBuf buffer, WeaponPartUnloadRecipe recipe) {
					// Implement custom serialization logic if needed
				}
			});

	public static final RegistryObject<RecipeSerializer<FuelTankLoadRecipe>> FUEL_TANK_LOAD = RECIPES.register("fuel_tank_load_recipe",
			() -> new RecipeSerializer<FuelTankLoadRecipe>() {
				@Override
				public FuelTankLoadRecipe fromJson(ResourceLocation id, JsonObject json) {
					return new FuelTankLoadRecipe(id);
				}

				@Override
				public FuelTankLoadRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
					return new FuelTankLoadRecipe(id);
				}

				@Override
				public void toNetwork(FriendlyByteBuf buffer, FuelTankLoadRecipe recipe) {
					// Implement custom serialization logic if needed
				}
			});

	public static final RegistryObject<RecipeSerializer<FlareDispenserLoadRecipe>> FLARE_LOAD = RECIPES.register("flare_load_recipe",
			() -> new RecipeSerializer<FlareDispenserLoadRecipe>() {
				@Override
				public FlareDispenserLoadRecipe fromJson(ResourceLocation id, JsonObject json) {
					return new FlareDispenserLoadRecipe(id);
				}

				@Override
				public FlareDispenserLoadRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
					return new FlareDispenserLoadRecipe(id);
				}

				@Override
				public void toNetwork(FriendlyByteBuf buffer, FlareDispenserLoadRecipe recipe) {
					// Implement custom serialization logic if needed
				}
			});

	public static final RegistryObject<RecipeSerializer<TurretLoadRecipe>> TURRET_LOAD = RECIPES.register("turret_load_recipe",
			() -> new RecipeSerializer<TurretLoadRecipe>() {
				@Override
				public TurretLoadRecipe fromJson(ResourceLocation id, JsonObject json) {
					return new TurretLoadRecipe(id);
				}

				@Override
				public TurretLoadRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
					return new TurretLoadRecipe(id);
				}

				@Override
				public void toNetwork(FriendlyByteBuf buffer, TurretLoadRecipe recipe) {
					// Implement custom serialization logic if needed
				}
			});

	public static final RegistryObject<RecipeSerializer<TurretUnloadRecipe>> TURRET_UNLOAD = RECIPES.register("turret_unload_recipe",
			() -> new RecipeSerializer<TurretUnloadRecipe>() {
				@Override
				public TurretUnloadRecipe fromJson(ResourceLocation id, JsonObject json) {
					return new TurretUnloadRecipe(id);
				}

				@Override
				public TurretUnloadRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
					return new TurretUnloadRecipe(id);
				}

				@Override
				public void toNetwork(FriendlyByteBuf buffer, TurretUnloadRecipe recipe) {
					// Implement custom serialization logic if needed
				}
			});

	public static final RegistryObject<RecipeSerializer<VehiclePartRepairRecipe>> VEHICLE_PART_REPAIR = RECIPES.register("vehicle_part_repair",
			() -> new RecipeSerializer<VehiclePartRepairRecipe>() {
				@Override
				public VehiclePartRepairRecipe fromJson(ResourceLocation id, JsonObject json) {
					return new VehiclePartRepairRecipe(id);
				}

				@Override
				public VehiclePartRepairRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
					return new VehiclePartRepairRecipe(id);
				}

				@Override
				public void toNetwork(FriendlyByteBuf buffer, VehiclePartRepairRecipe recipe) {
					// Implement custom serialization logic if needed
				}
			});

	// Other custom recipes
	public static final RegistryObject<RecipeSerializer<VehicleRecipe>> AIRCRAFT_RECIPE_SERIALIZER =
			RECIPES.register(VehicleRecipe.Serializer.ID.getPath(), () -> VehicleRecipe.Serializer.INSTANCE);
	public static final RegistryObject<RecipeSerializer<WeaponRecipe>> WEAPON_RECIPE_SERIALIZER =
			RECIPES.register(WeaponRecipe.Serializer.ID.getPath(), () -> WeaponRecipe.Serializer.INSTANCE);
	public static final RegistryObject<RecipeSerializer<BucketConvertRecipe>> BUCKET_CONVERT_RECIPE_SERIALIZER =
			RECIPES.register(BucketConvertRecipe.Serializer.ID.getPath(), () -> BucketConvertRecipe.Serializer.INSTANCE);
}
