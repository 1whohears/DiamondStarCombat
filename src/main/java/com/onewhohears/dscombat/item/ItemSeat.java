package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.SeatData;

import net.minecraft.nbt.CompoundTag;

public class ItemSeat extends ItemPart {

	public ItemSeat(float weight, SlotType[] compatibleSlots) {
		super(1, weight, compatibleSlots);
	}

	@Override
	public CompoundTag getNbt() {
		return new SeatData(weight, getIdPart(), compatibleSlots).write();
	}

}
