package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.SeatData;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemSeat extends ItemPart {

	public ItemSeat() {
		super(1, 0);
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			ItemStack seat1 = new ItemStack(ModItems.SEAT.get());
			seat1.setTag(new SeatData(0.001f, ModItems.SEAT.getId()).write());
			items.add(seat1);
		}
	}

}
