package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;

import net.minecraftforge.registries.ForgeRegistries;

import com.onewhohears.dscombat.data.parts.SeatData;

public class ItemSeat extends ItemPart {

	public ItemSeat(float weight, SlotType[] compatibleSlots) {
		super(1, weight, compatibleSlots);
	}

	@Override
	public PartData getPartData() {
		return new SeatData(weight, ForgeRegistries.ITEMS.getKey(this), compatibleSlots);
	}

}
