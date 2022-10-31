package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.crafting.WeaponPartAmmoRecipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
	
	public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DSCombatMod.MODID);
	
	public static void register(IEventBus eventBus) {
		RECIPES.register(eventBus);
	}
	
	public static final RegistryObject<SimpleRecipeSerializer<WeaponPartAmmoRecipe>> WEAPON_PART = RECIPES.register("weapon_part_ammo_recipe", () -> new SimpleRecipeSerializer<>(WeaponPartAmmoRecipe::new));
	
}
