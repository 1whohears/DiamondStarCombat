package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.SeatData;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilItem;

public class ItemSeat extends ItemPart {
	
	private static SeatData defaultSeat;
	
	public static SeatData getDefaultSeat() {
		if (defaultSeat == null) defaultSeat = (SeatData)((ItemSeat)ModItems.SEAT.get()).getPartData();
		return defaultSeat;
	}
	
	public ItemSeat(float weight, SlotType[] compatibleSlots) {
		super(16, weight, compatibleSlots);
	}

	@Override
	public PartData getPartData() {
		return new SeatData(weight, UtilItem.getItemKey(this), compatibleSlots);
	}

}
