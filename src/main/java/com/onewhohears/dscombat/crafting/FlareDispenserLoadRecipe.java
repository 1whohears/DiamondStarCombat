package com.onewhohears.dscombat.crafting;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.init.ModRecipeSerializers;
import com.onewhohears.dscombat.item.ItemFlareDispenser;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

public class FlareDispenserLoadRecipe extends CustomRecipe {

	public FlareDispenserLoadRecipe(ResourceLocation pId) {
		super(pId);
	}

	@Override
	public boolean matches(CraftingContainer container, Level pLevel) {
		ItemStack flareDis = getFlareDis(container);
		List<ItemStack> flares = getFlares(container);
		return hasStuff(flareDis, flares);
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		ItemStack flareDis = getFlareDis(container);
		List<ItemStack> flares = getFlares(container);
		if (!hasStuff(flareDis, flares)) return ItemStack.EMPTY;
		int cf = flareDis.getOrCreateTag().getInt("flares");
		int mf = flareDis.getOrCreateTag().getInt("max");
		for (int i = 0; i < flares.size(); ++i) {
			int c = flares.get(i).getCount();
			cf += c;
		}
		if (cf > mf) cf = mf;
		ItemStack r = flareDis.copy();
		r.getOrCreateTag().putFloat("flares", cf);
		return r;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		ItemStack flareDis = getFlareDis(container);
		List<ItemStack> flares = getFlares(container);
		int cf = flareDis.getOrCreateTag().getInt("flares");
		int mf = flareDis.getOrCreateTag().getInt("max");
		for (int i = 0; i < flares.size(); ++i) {
			ItemStack stack = flares.get(i);
			int c = flares.get(i).getCount();
			int t = cf+c;
			if (t <= mf) {
				stack.setCount(0);
				cf += c;
			} else if (t > mf && cf != mf) {
				stack.setCount(t-mf+1);
				cf = mf;
			} else stack.setCount(c+1);
		}
		NonNullList<ItemStack> list = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
		for(int i = 0; i < list.size(); ++i) {
			ItemStack stack = container.getItem(i);
			list.set(i, ForgeHooks.getCraftingRemainingItem(stack));
		}
		return list;
	}
	
	private boolean hasStuff(ItemStack flareDis, List<ItemStack> flares) {
		return flareDis != null && flares.size() > 0;
	}
	
	private ItemStack getFlareDis(CraftingContainer container) {
		ItemStack tank = null;
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack  = container.getItem(i);
			if (stack.isEmpty()) continue;
			Item item = stack.getItem();
			if (item instanceof ItemFlareDispenser) {
				if (tank != null) return null;
				tank = stack;
			}
		}
		return tank;
	}
	
	private List<ItemStack> getFlares(CraftingContainer container) {
		String flareId = Items.BLAZE_POWDER.getDescriptionId();
		List<ItemStack> flares = new ArrayList<ItemStack>();
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack  = container.getItem(i);
			if (stack.isEmpty()) continue;
			Item item = stack.getItem();
			if (item.getDescriptionId().equals(flareId)) 
				flares.add(stack);
		}
		return flares;
	}
	
	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.FLARE_LOAD.get();
	}

}
