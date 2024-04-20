package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.GimbalPartData;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.util.UtilItem;

public class ItemGimbal extends ItemPart {
	
	public final String entityTypeKey;
	
	public ItemGimbal(float weight, SlotType[] compatibleSlots, String entityTypeKey) {
		super(16, weight, compatibleSlots);
		this.entityTypeKey = entityTypeKey;
	}
	
	@Override
	public PartData getPartData() {
		return new GimbalPartData(weight, UtilItem.getItemKey(this), compatibleSlots, entityTypeKey);
	}

}
