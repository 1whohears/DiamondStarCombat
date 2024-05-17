package com.onewhohears.dscombat.crafting;

import com.onewhohears.dscombat.data.parts.instance.FuelTankInstance;
import com.onewhohears.dscombat.init.ModRecipes;
import com.onewhohears.dscombat.item.ItemFuelTank;
import com.onewhohears.dscombat.item.ItemGasCan;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FuelTankLoadRecipe extends PartItemLoadRecipe<FuelTankInstance<?>> {

	public FuelTankLoadRecipe(ResourceLocation pId) {
		super(pId);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.FUEL_TANK_LOAD.get();
	}
	
	@Override
	public AmmoLoadType getAmmoLoadType() {
		return AmmoLoadType.ITEM_DURABILITY;
	}

	@Override
	public boolean isLoadablePartItem(ItemStack stack) {
		return stack.getItem() instanceof ItemFuelTank;
	}

	@Override
	public boolean isItemAmmo(ItemStack stack) {
		return stack.getItem() instanceof ItemGasCan;
	}

	@Override
	public boolean checkAmmoContinuity() {
		return false;
	}

	@Override
	public String getItemAmmoContinuity(ItemStack stack) {
		return "";
	}

	@Override
	public boolean isContinuityValid(String continuity) {
		return true;
	}

	@Override
	public int getContinuityMaxAmmo(FuelTankInstance<?> lpd, String continuity) {
		return 0;
	}

}
