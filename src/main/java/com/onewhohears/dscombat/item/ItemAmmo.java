package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemAmmo extends Item {

	public ItemAmmo(int size) {
		super(getDefaultProperties(size));
	}
	
	public static Properties getDefaultProperties(int stackSize) {
		return new Item.Properties().tab(ModItems.PARTS).stacksTo(stackSize);
	}
	
	/*@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return UtilMCText.simpleText(DSCombatMod.MODID+"."+tag.getString("weaponId"));
	}*/
	
	@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack) {	
		return ItemStack.EMPTY;
	}

}
