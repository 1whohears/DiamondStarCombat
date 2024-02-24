package com.onewhohears.dscombat.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.LoadableRecipePartData;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

public abstract class PartItemLoadRecipe<I extends LoadableRecipePartData> extends CustomRecipe {

	protected PartItemLoadRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		if (hasOutlier(container)) return false;
		ItemStack part = getPartItem(container);
		List<ItemStack> ammo = getAmmoItems(container);
		I lpd = getLoadablePartDataFromItem(part);
		return canItemsCombine(lpd, ammo);
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		ItemStack part = getPartItem(container);
		List<ItemStack> ammo = getAmmoItems(container);
		I lpd = getLoadablePartDataFromItem(part);
		if (!canItemsCombine(lpd, ammo)) return ItemStack.EMPTY;
		if (checkAmmoContinuity()) {
			String ammoCont = getItemAmmoContinuity(ammo.get(0));
			if (!isContinuityValid(ammoCont)) return ItemStack.EMPTY;
			emptyContinuityCheck(lpd, ammoCont);
		}
		float newAmmo = lpd.getCurrentAmmo();
		for (int i = 0; i < ammo.size(); ++i) newAmmo += getAmmoNumFromItem(ammo.get(i));
		if (newAmmo > lpd.getMaxAmmo()) newAmmo = lpd.getMaxAmmo();
		lpd.setCurrentAmmo(newAmmo);
		return lpd.getNewItemStack();
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		ItemStack part = getPartItem(container);
		List<ItemStack> ammo = getAmmoItems(container);
		I lpd = getLoadablePartDataFromItem(part);
		if (!canItemsCombine(lpd, ammo)) return super.getRemainingItems(container);
		if (checkAmmoContinuity()) {
			String ammoCont = getItemAmmoContinuity(ammo.get(0));
			if (!isContinuityValid(ammoCont)) return super.getRemainingItems(container);
			emptyContinuityCheck(lpd, ammoCont);
		}
		float ca = lpd.getCurrentAmmo();
		float ma = lpd.getMaxAmmo();
		for (int i = 0; i < ammo.size(); ++i) {
			float c = getAmmoNumFromItem(ammo.get(i));
			float t = ca+c;
			if (t <= ma) {
				setAmmoNumForItem(ammo.get(i), 0);
				ca += c;
			} else if (t > ma && ca != ma) {
				setAmmoNumForItem(ammo.get(i), t-ma);
				ca = ma;
			} else setAmmoNumForItem(ammo.get(i), c);
		}
		NonNullList<ItemStack> list = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
		for(int i = 0; i < list.size(); ++i) {
			ItemStack stack = container.getItem(i);
			list.set(i, ForgeHooks.getCraftingRemainingItem(stack));
		}
		return list;
	}
	
	public float getAmmoNumFromItem(ItemStack ammo) {
		switch (getAmmoLoadType()) {
		case ITEM_COUNT:
			return ammo.getCount();
		case ITEM_DURABILITY:
			return ammo.getMaxDamage() - ammo.getDamageValue();
		}
		return 0;
	}
	
	public void setAmmoNumForItem(ItemStack ammo, float num) {
		switch (getAmmoLoadType()) {
		case ITEM_COUNT:
			ammo.setCount((int)num+1);
			break;
		case ITEM_DURABILITY:
			ammo.setDamageValue(ammo.getMaxDamage() - (int)num);
			ammo.setCount(2);
			break;
		}
	}
	
	private void emptyContinuityCheck(I lpd, String ammoCont) {
		if (lpd.updateContinuityIfEmpty() && lpd.isContinuityEmpty()) {
			lpd.setContinuity(ammoCont);
			lpd.setCurrentAmmo(0);
			lpd.setMaxAmmo(getContinuityMaxAmmo(ammoCont));
		}
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}
	
	@Override
	public boolean isSpecial() {
		return true;
	}
	
	public boolean hasOutlier(CraftingContainer container) {
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack  = container.getItem(i);
			if (stack.isEmpty()) continue;
			if (isOutlier(stack)) return true;
		}
		return false;
	}
	
	public boolean isOutlier(ItemStack stack) {
		return !isLoadablePartItem(stack) && !isItemAmmo(stack);
	}
	
	@Nullable
	public ItemStack getPartItem(CraftingContainer container, AtomicInteger index) {
		ItemStack part = null;
		index.set(-1);
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack  = container.getItem(i);
			if (stack.isEmpty()) continue;
			if (isLoadablePartItem(stack)) {
				if (part != null) return null;
				part = stack;
				index.set(i);
			}
		}
		return part;
	}
	
	@Nullable
	public ItemStack getPartItem(CraftingContainer container) {
		return getPartItem(container, new AtomicInteger());
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	protected I getLoadablePartDataFromItem(ItemStack part) {
		if (part == null) return null;
		PartData pd = UtilParse.parsePartFromItem(part);
		if (pd == null) return null;
		return (I) pd;
	}
	
	@Nullable
	public List<ItemStack> getAmmoItems(CraftingContainer container) {
		String continuity = null;
		List<ItemStack> ammo = new ArrayList<ItemStack>();
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack  = container.getItem(i);
			if (stack.isEmpty()) continue;
			if (!isItemAmmo(stack)) continue;
			if (checkAmmoContinuity()) {
				String stackCont = getItemAmmoContinuity(stack);
				if (continuity == null) continuity = stackCont;
				else if (!stackCont.equals(continuity)) return null;
			}
			ammo.add(stack);
		}
		return ammo;
	}
	
	public abstract boolean isLoadablePartItem(ItemStack stack);
	public abstract boolean isItemAmmo(ItemStack stack);
	public abstract boolean checkAmmoContinuity();
	public abstract String getItemAmmoContinuity(ItemStack stack);
	public abstract boolean isContinuityValid(String continuity);
	public abstract int getContinuityMaxAmmo(String continuity);
	
	public boolean canItemsCombine(I lpd, List<ItemStack> ammo) {
		if (lpd == null || ammo == null || ammo.size() < 1) return false;
		if (checkAmmoContinuity()) {
			String ammoCont = getItemAmmoContinuity(ammo.get(0));
			if (!isContinuityValid(ammoCont)) return false;
			if (!lpd.isCompatibleWithAmmoContinuity(ammoCont)) return false;
			if (lpd.updateContinuityIfEmpty() && lpd.isContinuityEmpty()) return true;
			else if (lpd.getContinuity().equals(ammoCont)) return true;
			return false;
		}
		return true;
	}
	
	public AmmoLoadType getAmmoLoadType() {
		return AmmoLoadType.ITEM_COUNT;
	}
	
	public static enum AmmoLoadType {
		ITEM_COUNT,
		ITEM_DURABILITY;
	}

}
