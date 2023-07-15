package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemAmmo extends Item {
	
	private final String defaultWeaponId;
	
	public ItemAmmo(int size, String defaultWeaponId) {
		super(weaponProps(size));
		this.defaultWeaponId = defaultWeaponId;
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() != ModItems.WEAPONS.getId()) return;
		String itemId = ForgeRegistries.ITEMS.getKey(this).toString();
		for (int i = 0; i < WeaponPresets.get().getPresetNum(); ++i) {
			WeaponData w = WeaponPresets.get().getAllPresets()[i];
			if (!w.getItemKey().equals(itemId)) continue;
			ItemStack test = new ItemStack(this);
			CompoundTag tag = new CompoundTag();
			tag.putString("weapon", w.getId());
			test.setTag(tag);
			items.add(test);
		}
	}
	
	@Override
	public Component getName(ItemStack stack) {
		String id = getWeaponId(stack);
		WeaponData wd = WeaponPresets.get().getPreset(id);
		if (wd == null) return new TranslatableComponent(getDescriptionId())
				.append(new TranslatableComponent("error.dscombat.unknown_preset"));
		return wd.getDisplayNameComponent().append(" ").append(new TranslatableComponent("info.dscombat.ammo"));
	}
	
	public static String getWeaponId(ItemStack stack) {
		if (stack.getItem() instanceof ItemAmmo ia) {
			if (!stack.getOrCreateTag().contains("weapon")) return ia.defaultWeaponId;
			return stack.getOrCreateTag().getString("weapon");
		}
		return "";
	}
	
	public static Properties weaponProps(int stackSize) {
		return new Item.Properties().tab(ModItems.WEAPONS).stacksTo(stackSize);
	}

}
