package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemAmmo extends Item {

	public ItemAmmo(int size) {
		super(weaponProps(size));
	}
	
	public static Properties weaponProps(int stackSize) {
		return new Item.Properties().tab(ModItems.WEAPONS).stacksTo(stackSize);
	}
	
	@Override
	public Component getName(ItemStack stack) {
		return new TranslatableComponent(getDescriptionId())
				.append(" ")
				.append(new TranslatableComponent(DSCombatMod.MODID+".ammo"));
	}
	
	public String getAmmoId() {
		return getDescriptionId().split("\\.")[2];
	}

}
