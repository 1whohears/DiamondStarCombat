package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.world.item.Item;

public abstract class ItemPart extends Item {
	
	protected final int num;
	
	protected ItemPart(int stackSize, int num) {
		super(getDefaultProperties(stackSize));
		this.num = num;
	}
	
	public static Properties getDefaultProperties(int stackSize) {
		return new Item.Properties().tab(ModItems.PARTS).stacksTo(stackSize);
	}

}
