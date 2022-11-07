package com.onewhohears.dscombat.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class FuelTankLoadRecipe extends CustomRecipe {

	public FuelTankLoadRecipe(ResourceLocation pId) {
		super(pId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matches(CraftingContainer pContainer, Level pLevel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack assemble(CraftingContainer pContainer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

}
