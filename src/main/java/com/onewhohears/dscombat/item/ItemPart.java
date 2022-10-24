package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.world.item.Item;

public abstract class ItemPart extends Item {
	
	protected ItemPart(int stackSize) {
		super(getDefaultProperties(stackSize));
	}
	
	public static Properties getDefaultProperties(int stackSize) {
		return new Item.Properties().tab(ModItems.PARTS).stacksTo(stackSize);
	}

}
