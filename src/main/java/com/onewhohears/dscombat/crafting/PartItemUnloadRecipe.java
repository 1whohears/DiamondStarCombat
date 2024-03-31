package com.onewhohears.dscombat.crafting;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

import com.onewhohears.dscombat.data.parts.LoadableRecipePartData;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

public abstract class PartItemUnloadRecipe<I extends LoadableRecipePartData> extends PartItemLoadRecipe<I> {
	
	protected PartItemUnloadRecipe(ResourceLocation id) {
		super(id);
	}
	
	@Override
	public boolean matches(CraftingContainer container, Level level) {
		return !hasOutlier(container);
	}
	
	@Override
	public ItemStack assemble(CraftingContainer container) {
		ItemStack part = getPartItem(container);
		I lpd = getLoadablePartDataFromItem(part);
		if (lpd == null) return ItemStack.EMPTY;
		String continuity = lpd.getContinuity();
		if (checkAmmoContinuity() && !lpd.isContinuityEmpty() && !isContinuityValid(continuity)) 
			return ItemStack.EMPTY;
		ItemStack ammo = getNewAmmoItem(continuity);
		if (ammo.isEmpty()) return ammo;
		ammo.setCount((int)lpd.getCurrentAmmo());
		return ammo;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		AtomicInteger index = new AtomicInteger();
		ItemStack part = getPartItem(container, index);
		I lpd = getLoadablePartDataFromItem(part);
		if (lpd == null) return super.getRemainingItems(container);
		lpd.setCurrentAmmo(0);
		ItemStack newPart = lpd.getNewItemStack();
		NonNullList<ItemStack> list = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
		for(int i = 0; i < list.size(); ++i) {
			if (index.get() == i) {
				list.set(i, newPart);
				continue;
			}
			ItemStack stack = container.getItem(i);
			list.set(i, ForgeHooks.getCraftingRemainingItem(stack));
		}
		return list;
	}
	
	@Override
	public boolean isOutlier(ItemStack stack) {
		return !isLoadablePartItem(stack);
	}
	
	@Nonnull public abstract ItemStack getNewAmmoItem(String continuity);
	
}
