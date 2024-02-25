package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.BuffData;
import com.onewhohears.dscombat.data.parts.BuffData.BuffType;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.util.UtilItem;

public class ItemBuffPart extends ItemPart {

	public final BuffType type;
	
	public ItemBuffPart(BuffType type, SlotType[] compatibleSlots, float weight) {
		super(1, weight, compatibleSlots);
		this.type = type;
	}

	@Override
	public PartData getPartData() {
		return new BuffData(type, UtilItem.getItemKey(this), compatibleSlots, weight);
	}

}
