package com.onewhohears.dscombat.crafting;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.ModRecipeSerializers;
import com.onewhohears.dscombat.item.ItemAmmo;
import com.onewhohears.dscombat.item.ItemWeaponPart;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

public class WeaponPartLoadRecipe extends CustomRecipe {

	public WeaponPartLoadRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		//debugContainer(container, "MATCHES?"); // opens on server side
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack  = container.getItem(i);
			if (stack.isEmpty()) continue;
			Item item = stack.getItem();
			if (!(item instanceof ItemWeaponPart || item instanceof ItemAmmo)) return false;
		}
		ItemStack part = getPart(container);
		List<ItemStack> ammo = getAmmo(container);
		//System.out.println("part = "+part);
		//System.out.println("ammo = "+ammo);
		return isIdSame(part, ammo);
	}
	
	private boolean isIdSame(ItemStack part, List<ItemStack> ammo) {
		if (part == null || ammo == null) return false;
		if (ammo.size() < 1) return false;
		CompoundTag pTag = part.getOrCreateTag();
		String partId = pTag.getString("weaponId");
		String ammoId = ammo.get(0).getItem().getDescriptionId().split("\\.")[2];
		if (partId.isEmpty()) {
			ListTag list = pTag.getList("compatible", 8);
			for (int i = 0; i < list.size(); ++i) if (list.getString(i).equals(ammoId)) return true;
			return false;
		}
		//System.out.println("partId = "+partId+" ammoId = "+ammoId);
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
	
	private List<ItemStack> getAmmo(CraftingContainer container) {
		String id = null;
		List<ItemStack> ammo = new ArrayList<ItemStack>();
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack  = container.getItem(i);
			if (stack.isEmpty()) continue;
			Item item = stack.getItem();
			if (item instanceof ItemAmmo) {
				if (id == null) id = item.getDescriptionId().split("\\.")[2];
				else if (!item.getDescriptionId().split("\\.")[2].equals(id)) return null;
				ammo.add(stack);
			}
		}
		return ammo;
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		ItemStack part = getPart(container);
		List<ItemStack> ammo = getAmmo(container);
		if (!isIdSame(part, ammo)) return ItemStack.EMPTY;
		int ca = 0, ma;
		String weaponId = ammo.get(0).getItem().getDescriptionId().split("\\.")[2];
		if (part.getOrCreateTag().getString("weaponId").isEmpty()) {
			ma = WeaponPresets.getById(weaponId).getMaxAmmo();
		} else {
			ca = part.getOrCreateTag().getInt("ammo");
			ma = part.getOrCreateTag().getInt("max");
		}
		int na = ca;
		for (int i = 0; i < ammo.size(); ++i) na += ammo.get(i).getCount();
		if (na > ma) na = ma;
		ItemStack r = part.copy();
		CompoundTag tag = r.getOrCreateTag();
		tag.putString("weaponId", weaponId);
		tag.putInt("ammo", na);
		tag.putInt("max", ma);
		return r;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		//debugContainer(container, "REMAINING ITEMS");
		ItemStack part = getPart(container);
		List<ItemStack> ammo = getAmmo(container);
		int ca = 0, ma;
		if (part.getOrCreateTag().getString("weaponId").isEmpty()) {
			String weaponId = ammo.get(0).getItem().getDescriptionId().split("\\.")[2];
			ma = WeaponPresets.getById(weaponId).getMaxAmmo();
		} else {
			ca = part.getOrCreateTag().getInt("ammo");
			ma = part.getOrCreateTag().getInt("max");
		}
		//System.out.println("ammo item = "+ammo.getCount());
		//System.out.println("ca = "+ca+" ma = "+ma+" na = "+na);
		//System.out.println("diff = "+diff);
		for (int i = 0; i < ammo.size(); ++i) {
			int c = ammo.get(i).getCount();
			int t = ca+c;
			if (t <= ma) {
				ammo.get(i).setCount(0);
				ca += c;
			} else if (t > ma && ca != ma) {
				ammo.get(i).setCount(t-ma+1);
				ca = ma;
			} else ammo.get(i).setCount(c+1);
		}
		NonNullList<ItemStack> list = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
		for(int i = 0; i < list.size(); ++i) {
			ItemStack stack = container.getItem(i);
			list.set(i, ForgeHooks.getCraftingRemainingItem(stack));
		}
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
		return ModRecipeSerializers.WEAPON_PART_LOAD.get();
	}
	
	/*private void debugContainer(Container c, String debug) {
		String p = debug;
		for (int i = 0; i < c.getContainerSize(); ++i) {
			p += "["+c.getItem(i)+"]";
		}
		System.out.println(p);
	}*/

}
