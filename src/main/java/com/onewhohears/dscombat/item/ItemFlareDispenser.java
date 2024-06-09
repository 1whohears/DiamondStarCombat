package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.stats.FlareDispenserStats;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class ItemFlareDispenser extends ItemPart {
	
	public ItemFlareDispenser(int stackSize) {
		super(stackSize);
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		FlareDispenserStats stats = (FlareDispenserStats)getPartStats(stack);
		MutableComponent name = ((MutableComponent)super.getName(stack))
			.append(" "+tag.getInt("flares")+"/"+stats.getMaxFlares());
		return name;	
	}

}
