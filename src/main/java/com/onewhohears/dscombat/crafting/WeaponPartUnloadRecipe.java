package com.onewhohears.dscombat.crafting;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.init.ModRecipeSerializers;
import com.onewhohears.dscombat.item.ItemWeaponPart;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class WeaponPartUnloadRecipe extends CustomRecipe {

	public WeaponPartUnloadRecipe(ResourceLocation id) {
		super(id);
	}
	// FIXME 0.3 verify this recipe works
	@Override
	public boolean matches(CraftingContainer container, Level level) {
		ItemStack s = null;
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack  = container.getItem(i);
			if (stack.isEmpty()) continue;
			Item item = stack.getItem();
			if (!(item instanceof ItemWeaponPart)) return false;
			if (s != null) return false;
			s = stack;
		}
		return s != null;
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		ItemStack part = null;
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack s  = container.getItem(i);
			if (s.isEmpty()) continue;
			Item item = s.getItem();
			if (!(item instanceof ItemWeaponPart)) return ItemStack.EMPTY;
			if (part != null) return ItemStack.EMPTY;
			part = s;
		}
		if (part == null) return ItemStack.EMPTY;
		String partId = part.getOrCreateTag().getString("weaponId");
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(DSCombatMod.MODID, partId));
		if (item == null) return ItemStack.EMPTY;
		ItemStack ammo = new ItemStack(item);
		ammo.setCount(part.getOrCreateTag().getInt("ammo"));
		return ammo;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		ItemStack part = null;
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack s  = container.getItem(i);
			if (s.isEmpty()) continue;
			Item item = s.getItem();
			if (item instanceof ItemWeaponPart) {
				part = s;
				break;
			}
		}
		if (part != null) {
			CompoundTag tag = part.getOrCreateTag();
			tag.putString("weaponId", "");
			tag.putInt("ammo", 0);
			tag.putInt("max", 0);
			part.setCount(2);
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
		return ModRecipeSerializers.WEAPON_PART_UNLOAD.get();
	}

}
