package com.onewhohears.dscombat.crafting;

import com.onewhohears.dscombat.init.ModRecipeSerializers;
import com.onewhohears.dscombat.item.ItemAmmo;
import com.onewhohears.dscombat.item.ItemWeaponPart;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class WeaponPartAmmoRecipe extends CustomRecipe {

	public WeaponPartAmmoRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		debugContainer(container, "MATCHES?"); // opens on server side
		ItemStack part = getPart(container);
		ItemStack ammo = getAmmo(container);
		System.out.println("part = "+part);
		System.out.println("ammo = "+ammo);
		return isIdSame(part, ammo);
	}
	
	private boolean isIdSame(ItemStack part, ItemStack ammo) {
		if (part == null || ammo == null) return false;
		String partId = part.getOrCreateTag().getString("weaponId");
		String ammoId = ammo.getItem().getDescriptionId().split("\\.")[2];
		System.out.println("partId = "+partId+" ammoId = "+ammoId);
		return partId.equals(ammoId);
	}
	
	private ItemStack getPart(CraftingContainer container) {
		ItemStack part = null;
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack  = container.getItem(i);
			if (stack.isEmpty()) continue;
			Item item = stack.getItem();
			if (item instanceof ItemWeaponPart) {
				if (part != null) return null;
				part = stack;
			}
		}
		return part;
	}
	
	private ItemStack getAmmo(CraftingContainer container) {
		ItemStack ammo = null;
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack  = container.getItem(i);
			if (stack.isEmpty()) continue;
			Item item = stack.getItem();
			if (item instanceof ItemAmmo) {
				if (ammo != null) return null;
				ammo = stack;
			}
		}
		return ammo;
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		ItemStack part = getPart(container);
		ItemStack ammo = getAmmo(container);
		ItemStack r = part.copy();
		if (!isIdSame(part, ammo)) return ItemStack.EMPTY;
		int ca = part.getOrCreateTag().getInt("ammo");
		int ma = part.getOrCreateTag().getInt("max");
		int na = ammo.getCount()+ca;
		if (na > ma) na = ma;
		r.getOrCreateTag().putInt("ammo", na);
		return r;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		// TODO why does this cringe not work
		debugContainer(container, "REMAINING ITEMS");
		ItemStack part = getPart(container);
		ItemStack ammo = getAmmo(container);
		int ca = part.getOrCreateTag().getInt("ammo");
		int ma = part.getOrCreateTag().getInt("max");
		int na = ammo.getCount()+ca;
		int diff = na - ma;
		System.out.println("ammo item = "+ammo.getCount());
		System.out.println("ca = "+ca+" ma = "+ma+" na = "+na);
		System.out.println("diff = "+diff);
		if (diff > 0) ammo.setCount(diff);
		else ammo.setCount(0);
		NonNullList<ItemStack> list = super.getRemainingItems(container);
		for(int i = 0; i < list.size(); ++i) if (!list.get(i).isEmpty()) list.set(i, container.getItem(i));
		return list;
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
		return ModRecipeSerializers.WEAPON_PART.get();
	}
	
	private void debugContainer(Container c, String debug) {
		String p = debug;
		for (int i = 0; i < c.getContainerSize(); ++i) {
			p += "["+c.getItem(i)+"]";
		}
		System.out.println(p);
	}

}
