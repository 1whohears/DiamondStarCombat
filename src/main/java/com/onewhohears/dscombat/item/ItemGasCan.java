package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.world.item.Item;

public class ItemGasCan extends Item {

	public ItemGasCan(int maxFuel) {
		super(new Item.Properties().tab(ModItems.PARTS).stacksTo(1).durability(maxFuel));
		// TODO right click aircraft to give it this item's fuel
	}

}
