package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemAmmo extends Item {

	public ItemAmmo(int size) {
		super(getDefaultProperties(size));
	}
	
	public static Properties getDefaultProperties(int stackSize) {
		return new Item.Properties().tab(ModItems.PARTS).stacksTo(stackSize);
	}
	
	@Override
	public Component getName(ItemStack stack) {
		return UtilMCText.simpleText(getDescriptionId())
				.append(" ")
				.append(UtilMCText.simpleText(DSCombatMod.MODID+".ammo"));
	}
	
	@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack) {	
		return ItemStack.EMPTY;
	}

}
