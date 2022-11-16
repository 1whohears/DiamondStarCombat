package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.SeatData;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemSeat extends ItemPart {

	public ItemSeat(float weight) {
		super(1, weight);
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			ItemStack seat1 = new ItemStack(this);
			seat1.setTag(new SeatData(weight, getIdPart()).write());
			items.add(seat1);
		}
	}

}
