package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.world.item.Item;

public abstract class ItemPart extends Item {
	
	public final float weight;
	
	protected ItemPart(int stackSize, float weight) {
		this(partProps(stackSize), weight);
	}
	
	protected ItemPart(Properties props, float weight) {
		super(props);
		this.weight = weight;
	}
	
	public static Properties partProps(int stackSize) {
		return new Item.Properties().tab(ModItems.PARTS).stacksTo(stackSize);
	}
	
	public String getIdPart() {
		return getDescriptionId().split("\\.")[2];
	}

}
