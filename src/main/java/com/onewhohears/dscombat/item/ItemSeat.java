package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.SeatData;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraftforge.registries.ForgeRegistries;

public class ItemSeat extends ItemPart {
	
	private static SeatData defaultSeat;
	
	public static SeatData getDefaultSeat() {
		if (defaultSeat == null) defaultSeat = (SeatData)((ItemSeat)ModItems.SEAT.get()).getPartData();
		return defaultSeat;
	}
	
	public ItemSeat(float weight, SlotType[] compatibleSlots) {
		super(1, weight, compatibleSlots);
	}

	@Override
	public PartData getPartData() {
		return new SeatData(weight, ForgeRegistries.ITEMS.getKey(this), compatibleSlots);
	}

}
