package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.BuffData;
import com.onewhohears.dscombat.data.parts.BuffData.BuffType;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;

import net.minecraft.nbt.CompoundTag;

public class ItemBuffPart extends ItemPart {

	public final BuffType type;
	
	public ItemBuffPart(BuffType type, SlotType[] compatibleSlots) {
		super(1, 0, compatibleSlots);
		this.type = type;
	}

	@Override
	public CompoundTag getNbt() {
		return new BuffData(type, getIdPart(), compatibleSlots).write();
	}

}
