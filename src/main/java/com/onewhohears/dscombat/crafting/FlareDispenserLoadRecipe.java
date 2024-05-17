package com.onewhohears.dscombat.crafting;

import com.onewhohears.dscombat.data.parts.instance.FlareDispenserInstance;
import com.onewhohears.dscombat.init.ModRecipes;
import com.onewhohears.dscombat.item.ItemFlareDispenser;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FlareDispenserLoadRecipe extends PartItemLoadRecipe<FlareDispenserInstance<?>> {

	public FlareDispenserLoadRecipe(ResourceLocation pId) {
		super(pId);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.FLARE_LOAD.get();
	}

	@Override
	public boolean isLoadablePartItem(ItemStack stack) {
		return stack.getItem() instanceof ItemFlareDispenser;
	}

	@Override
	public boolean isItemAmmo(ItemStack stack) {
		return stack.is(Items.BLAZE_POWDER);
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
	public int getContinuityMaxAmmo(FlareDispenserInstance<?> lpd, String continuity) {
		return 0;
	}

}
