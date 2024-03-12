package com.onewhohears.dscombat.integration.jei;

import java.util.List;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.crafting.AircraftRecipe;
import com.onewhohears.dscombat.crafting.WeaponRecipe;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
	
	private static final ResourceLocation PLUGIN_UID = new ResourceLocation(DSCombatMod.MODID, "jei_plugin");
	
	@Override
	public ResourceLocation getPluginUid() {
		return PLUGIN_UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new AircraftRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new WeaponRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager rm = Minecraft.getInstance().level.getRecipeManager();
		List<AircraftRecipe> aircraftRecipes = rm.getAllRecipesFor(AircraftRecipe.Type.INSTANCE);
        registration.addRecipes(AircraftRecipeCategory.TYPE, aircraftRecipes);
        List<WeaponRecipe> weaponRecipes = rm.getAllRecipesFor(WeaponRecipe.Type.INSTANCE);
        registration.addRecipes(WeaponRecipeCategory.TYPE, weaponRecipes);
	}

}
