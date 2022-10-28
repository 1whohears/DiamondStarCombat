package com.onewhohears.dscombat.crafting;

import com.onewhohears.dscombat.init.ModRecipeSerializers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class WeaponRackAmmoRecipe extends CustomRecipe {

	public WeaponRackAmmoRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.WEAPON_RACK.get();
	}

}
