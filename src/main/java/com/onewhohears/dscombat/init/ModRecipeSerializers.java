package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.crafting.WeaponRackAmmoRecipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
	
	public static final DeferredRegister<RecipeSerializer<?>> REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DSCombatMod.MODID);
	
	public static void register(IEventBus eventBus) {
		REGISTER.register(eventBus);
	}
	
	public static final RegistryObject<SimpleRecipeSerializer<WeaponRackAmmoRecipe>> WEAPON_RACK = REGISTER.register("weapon_rack", () -> new SimpleRecipeSerializer<>(WeaponRackAmmoRecipe::new));
	
}
