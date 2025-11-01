package com.onewhohears.dscombat.integration.jei;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.crafting.VehicleRecipe;
import com.onewhohears.dscombat.crafting.WeaponPartRecipe;
import com.onewhohears.dscombat.crafting.WeaponRecipe;
import com.onewhohears.dscombat.init.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
	
	private static final ResourceLocation PLUGIN_UID = new ResourceLocation(DSCombatMod.MODID, "jei_plugin");
	
	@Override
	public @NotNull ResourceLocation getPluginUid() {
		return PLUGIN_UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
		registration.addRecipeCategories(new VehicleRecipeCategory(guiHelper));
		registration.addRecipeCategories(new WeaponRecipeCategory(guiHelper));
		registration.addRecipeCategories(new WeaponPartRecipeCategory(guiHelper));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager rm = Minecraft.getInstance().level.getRecipeManager();
		List<VehicleRecipe> vehicleRecipes = rm.getAllRecipesFor(VehicleRecipe.Type.INSTANCE);
        registration.addRecipes(VehicleRecipeCategory.TYPE, vehicleRecipes);
        List<WeaponRecipe> weaponRecipes = rm.getAllRecipesFor(WeaponRecipe.Type.INSTANCE);
        registration.addRecipes(WeaponRecipeCategory.TYPE, weaponRecipes);
		List<WeaponPartRecipe> weaponPartRecipes = rm.getAllRecipesFor(WeaponPartRecipe.Type.INSTANCE);
		registration.addRecipes(WeaponPartRecipeCategory.TYPE, weaponPartRecipes);
	}

	@Override
	public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK,
				ModItems.VEHICLE.get(), DSCSubTypes.getVehicleSubType());
		registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK,
				ModItems.TURRET.get(), DSCSubTypes.getPartSubType());
		registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK,
				ModItems.EXTERNAL_WEAPON_PART.get(), DSCSubTypes.getPartSubType());
		registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK,
				ModItems.AMMO.get(), DSCSubTypes.getAmmoSubType());
		registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK,
				ModItems.BULLET.get(), DSCSubTypes.getAmmoSubType());
		registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK,
				ModItems.BOMB.get(), DSCSubTypes.getAmmoSubType());
		registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK,
				ModItems.MISSILE.get(), DSCSubTypes.getAmmoSubType());
	}

}
