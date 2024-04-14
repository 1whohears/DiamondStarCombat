package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.ChainHookData;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.util.UtilItem;

public class ItemChainHook extends ItemPart {
	
	public final String entityTypeKey;
	
	public ItemChainHook(float weight, SlotType[] compatibleSlots, String entityTypeKey) {
		super(1, weight, compatibleSlots);
		this.entityTypeKey = entityTypeKey;
	}

	@Override
	public PartData getPartData() {
		return new ChainHookData(weight, UtilItem.getItemKey(this), compatibleSlots, entityTypeKey);
	}

}
