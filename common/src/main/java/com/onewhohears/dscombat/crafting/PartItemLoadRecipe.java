package com.onewhohears.dscombat.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.Nullable;

import com.onewhohears.dscombat.data.parts.instance.ReloadablePartInstance;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;

import com.onewhohears.dscombat.util.UtilPresetParse;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

public abstract class PartItemLoadRecipe<I extends ReloadablePartInstance> extends CustomRecipe {

	protected PartItemLoadRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		if (hasOutlier(container)) return false;
		ItemStack part = getPartItem(container);
		if (part == null) return false;
		if (part.getCount() > 1) return false;
		List<ItemStack> ammo = getAmmoItems(container);
		I lpd = getLoadablePartDataFromItem(part);
		return canItemsCombine(lpd, ammo);
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		ItemStack part = getPartItem(container);
		I lpd = getLoadablePartDataFromItem(part);
		if (fillPart(container, lpd)) return lpd.getNewItemStack();
		else return ItemStack.EMPTY;
	}

	public boolean fillPart(Container container, ReloadablePartInstance lpd) {
		List<ItemStack> ammo = getAmmoItemsContainer(container, null);
		if (!canItemsCombine(lpd, ammo)) return false;
		if (checkAmmoContinuity()) {
			String ammoCont = getItemAmmoContinuity(ammo.get(0));
			if (!isContinuityValid(ammoCont)) return false;
			emptyContinuityCheck(lpd, ammoCont);
		}
		float newAmmo = lpd.getCurrentAmmo();
		for (int i = 0; i < ammo.size(); ++i) newAmmo += getAmmoNumFromItem(ammo.get(i));
		if (newAmmo > lpd.getMaxAmmo()) newAmmo = lpd.getMaxAmmo();
		lpd.setCurrentAmmo(newAmmo);
		return true;
	}

	@Override
	public @NotNull NonNullList<ItemStack> getRemainingItems(@NotNull CraftingContainer container) {
		ItemStack part = getPartItem(container);
		I lpd = getLoadablePartDataFromItem(part);
		return getRemainingItems(container, lpd);
	}

	public NonNullList<ItemStack> getRemainingItems(Container container, I lpd) {
		if (!consumeAmmoItems(container, lpd)) return getRemainingItemsDefault(container);
		NonNullList<ItemStack> list = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
		for(int i = 0; i < list.size(); ++i) {
			ItemStack stack = container.getItem(i);
			list.set(i, ForgeHooks.getCraftingRemainingItem(stack));
		}
		return list;
	}

	public boolean consumeAmmoItems(Container container, ReloadablePartInstance lpd) {
		List<ItemStack> ammo = getAmmoItemsContainer(container, null);
		if (!canItemsCombine(lpd, ammo)) return false;
		if (checkAmmoContinuity()) {
			String ammoCont = getItemAmmoContinuity(ammo.get(0));
			if (!isContinuityValid(ammoCont)) return false;
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
		return true;
	}

	public void reloadFromInventory(Inventory inventory, ReloadablePartInstance lpd) {
		String continuity = lpd.getContinuity();
		if (lpd.updateContinuityIfEmpty() && lpd.isContinuityEmpty())
			continuity = null;
		float newAmmo = lpd.getCurrentAmmo();
		if (newAmmo >= lpd.getMaxAmmo()) return;
		for (int i = 0; i < inventory.getContainerSize(); ++i) {
			ItemStack stack  = inventory.getItem(i).copy();
			if (stack.isEmpty()) continue;
			if (!isItemAmmo(stack)) continue;
			if (checkAmmoContinuity()) {
				String stackCont = getItemAmmoContinuity(stack);
				if (continuity == null && isContinuityValid(stackCont)
						&& lpd.isCompatibleWithAmmoContinuity(stackCont)) continuity = stackCont;
				else if (!stackCont.equals(continuity)) continue;
			}
			float ammo = getAmmoNumFromItem(stack);
			newAmmo += ammo;
			float remain = newAmmo - lpd.getMaxAmmo();
			if (remain <= 0) inventory.setItem(i, ItemStack.EMPTY);
			else {
				setAmmoNumForItem(stack, remain, false);
				inventory.setItem(i, stack);
			}
			if (newAmmo >= lpd.getMaxAmmo()) {
				newAmmo = lpd.getMaxAmmo();
				break;
			}
		}
		if (continuity != null && !continuity.equals(lpd.getContinuity())) lpd.setContinuity(continuity);
		lpd.setCurrentAmmo(newAmmo);
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
		setAmmoNumForItem(ammo, num, true);
	}

	public void setAmmoNumForItem(ItemStack ammo, float num, boolean add1) {
		switch (getAmmoLoadType()) {
		case ITEM_COUNT:
			ammo.setCount((int)num+(add1?1:0));
			break;
		case ITEM_DURABILITY:
			ammo.setDamageValue(ammo.getMaxDamage() - (int)num);
			if (add1) ammo.setCount(2);
			break;
		}
	}
	
	private void emptyContinuityCheck(ReloadablePartInstance lpd, String ammoCont) {
		if (lpd.updateContinuityIfEmpty() && lpd.isContinuityEmpty()) {
			lpd.setContinuity(ammoCont);
			lpd.setCurrentAmmo(0);
			lpd.setMaxAmmo(getContinuityMaxAmmo((I) lpd, ammoCont));
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
		PartInstance<?> pd = UtilPresetParse.parsePartFromItem(part);
		if (pd == null) return null;
		return (I) pd;
	}
	
	@Nullable
	public List<ItemStack> getAmmoItems(CraftingContainer container) {
		return getAmmoItemsContainer(container, null);
	}

	@Nullable
	public List<ItemStack> getAmmoItemsContainer(Container container, String continuity) {
		List<ItemStack> ammo = new ArrayList<>();
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack  = container.getItem(i);
			if (stack.isEmpty()) continue;
			if (!isItemAmmo(stack)) continue;
			if (checkAmmoContinuity()) {
				String stackCont = getItemAmmoContinuity(stack);
				if (continuity == null) continuity = stackCont;
				else if (!stackCont.equals(continuity)) continue;
			}
			ammo.add(stack);
		}
		return ammo;
	}

	public float getAmmoNum(Container container, String continuity) {
		float num = 0;
		List<ItemStack> ammos = getAmmoItemsContainer(container, continuity);
		if (ammos == null) return 0;
		for (ItemStack ammo : ammos) num += getAmmoNumFromItem(ammo);
		return num;
	}
	
	public abstract boolean isLoadablePartItem(ItemStack stack);
	public abstract boolean isItemAmmo(ItemStack stack);
	public abstract boolean checkAmmoContinuity();
	public abstract String getItemAmmoContinuity(ItemStack stack);
	public abstract boolean isContinuityValid(String continuity);
	public abstract int getContinuityMaxAmmo(I lpd, String continuity);
	
	public boolean canItemsCombine(ReloadablePartInstance lpd, List<ItemStack> ammo) {
		if (lpd == null || ammo == null || ammo.isEmpty()) return false;
		if (checkAmmoContinuity()) {
			String ammoCont = getItemAmmoContinuity(ammo.get(0));
			if (!isContinuityValid(ammoCont)) return false;
			if (!lpd.isCompatibleWithAmmoContinuity(ammoCont)) return false;
			if (lpd.updateContinuityIfEmpty() && lpd.isContinuityEmpty()) return true;
			else if (lpd.getContinuity().equals(ammoCont)) return true;
			return false;
		}
		System.out.println("can combine");
		return true;
	}
	
	public AmmoLoadType getAmmoLoadType() {
		return AmmoLoadType.ITEM_COUNT;
	}
	
	public enum AmmoLoadType {
		ITEM_COUNT,
		ITEM_DURABILITY;
	}

	protected NonNullList<ItemStack> getRemainingItemsDefault(Container pContainer) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(pContainer.getContainerSize(), ItemStack.EMPTY);
		for(int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack item = pContainer.getItem(i);
			if (item.hasCraftingRemainingItem()) {
				nonnulllist.set(i, item.getCraftingRemainingItem());
			}
		}
		return nonnulllist;
	}

}
