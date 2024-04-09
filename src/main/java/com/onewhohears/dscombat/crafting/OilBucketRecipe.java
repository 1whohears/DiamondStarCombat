package com.onewhohears.dscombat.crafting;

import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModRecipes;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.dscombat.util.UtilItem;

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
	
	private final NonNullList<Ingredient> ingredients = NonNullList.create();
	
	public OilBucketRecipe(ResourceLocation pId) {
		super(pId);
		ingredients.add(Ingredient.of(Items.BUCKET));
		ingredients.add(Ingredient.of(ModTags.Items.FOSSIL_OIL_CONVERTER));
		ingredients.add(Ingredient.of(ModItems.COMPRESSED_FOSSIL.get()));
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		return UtilItem.testRecipe(getIngredients(), container);
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
		for(int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack item = container.getItem(i);
			if (item.is(ModTags.Items.FOSSIL_OIL_CONVERTER)) {
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
	
	@Override
	public ItemStack getResultItem() {
		return ModItems.OIL_BUCKET.get().getDefaultInstance();
	}

}
