package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.SeatData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemSeat extends ItemPart {

	public ItemSeat(float weight, SlotType[] compatibleSlots) {
		super(1, weight, compatibleSlots);
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			ItemStack seat1 = new ItemStack(this);
			seat1.setTag(new SeatData(weight, getIdPart(), compatibleSlots).write());
			items.add(seat1);
		}
	}

}
