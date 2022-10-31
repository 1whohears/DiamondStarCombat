package com.onewhohears.dscombat.crafting;

import com.onewhohears.dscombat.init.ModRecipeSerializers;
import com.onewhohears.dscombat.item.ItemAmmo;
import com.onewhohears.dscombat.item.ItemWeaponPart;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
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
		System.out.println("MATCHES?");
		System.out.println(container);
		ItemStack part = getPart(container);
		ItemStack ammo = getAmmo(container);
		System.out.println("part = "+part);
		System.out.println("ammo = "+ammo);
		return isIdSame(part, ammo);
	}
	
	private boolean isIdSame(ItemStack part, ItemStack ammo) {
		if (part == null || ammo == null) return false;
		String partId = part.getOrCreateTag().getString("weaponId");
		String ammoId = ammo.getItem().getDescriptionId();
		System.out.println("partId = "+partId+" ammoId = "+ammoId);
		return partId.equals(ammoId);
	}
	
	private ItemStack getPart(CraftingContainer container) {
		ItemStack part = null;
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack  = container.getItem(i);
			if (stack.isEmpty()) continue;
			Item item = stack.getItem();
			if (item instanceof ItemWeaponPart p) {
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
			if (item instanceof ItemAmmo ) {
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
		if (!isIdSame(part, ammo)) return ItemStack.EMPTY;
		int ca = part.getOrCreateTag().getInt("ammo");
		int ma = part.getOrCreateTag().getInt("max");
		int na = ammo.getCount()+ca;
		if (na > ma) na = ma;
		part.getOrCreateTag().putInt("ammo", na);
		return part;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inventory) {
		NonNullList<ItemStack> remainingItems = NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);
		for(int i = 0; i < remainingItems.size(); ++i) {
			ItemStack stack = inventory.getItem(i);
			remainingItems.set(i, net.minecraftforge.common.ForgeHooks.getCraftingRemainingItem(stack));
		}
		return remainingItems;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.WEAPON_PART.get();
	}

}
