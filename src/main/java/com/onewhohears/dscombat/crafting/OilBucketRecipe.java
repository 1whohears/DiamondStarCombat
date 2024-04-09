package com.onewhohears.dscombat.crafting;

import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModRecipes;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

public class OilBucketRecipe extends CustomRecipe {

	public OilBucketRecipe(ResourceLocation pId) {
		super(pId);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		for (Ingredient ing : getIngredients()) {
			boolean found = false;
			for (int i = 0; i < container.getContainerSize(); ++i) 
				if (ing.test(container.getItem(i))) {
					found = true;
					break;
				}
			if (!found) return false;
		}
		return true;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> ings = NonNullList.create();
		ings.add(Ingredient.of(Items.BUCKET));
		ings.add(Ingredient.of(Items.FLINT_AND_STEEL));
		ings.add(Ingredient.of(ModItems.COMPRESSED_FOSSIL.get()));
		return ings;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
		for(int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack item = container.getItem(i);
			if (item.is(Items.FLINT_AND_STEEL)) {
				ItemStack item2 = item.copy();
				item2.setDamageValue(item.getDamageValue()+1);
				nonnulllist.set(i, item2);
			} else if (item.hasCraftingRemainingItem()) {
				nonnulllist.set(i, ForgeHooks.getCraftingRemainingItem(item));
			}
		}
		return nonnulllist;
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		return ModItems.OIL_BUCKET.get().getDefaultInstance();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}
	
	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.OIL_BUCKET.get();
	}

}
