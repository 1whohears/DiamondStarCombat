package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.world.item.Item;

public abstract class ItemPart extends Item {
	
	public final float weight;
	public final SlotType[] compatibleSlots;
	
	protected ItemPart(int stackSize, float weight, SlotType[] compatibleSlots) {
		this(partProps(stackSize), weight, compatibleSlots);
	}
	
	protected ItemPart(Properties props, float weight, SlotType[] compatibleSlots) {
		super(props);
		this.weight = weight;
		this.compatibleSlots = compatibleSlots;
	}
	
	public static Properties partProps(int stackSize) {
		return new Item.Properties().tab(ModItems.PARTS).stacksTo(stackSize);
	}
	
	public String getIdPart() {
		return getDescriptionId().split("\\.")[2];
	}

}
