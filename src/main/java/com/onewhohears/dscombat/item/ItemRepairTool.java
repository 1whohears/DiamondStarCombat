package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.world.item.Item;

public class ItemRepairTool extends Item {
	
	public final float repair;
	
	public ItemRepairTool(int durability, float repair) {
		super(new Item.Properties().tab(ModItems.PARTS).stacksTo(1).durability(durability));
		this.repair = repair;
	}

}
