package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.ExternalRadarPartData;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.RadarPartData;
import com.onewhohears.dscombat.util.UtilItem;

public class ItemRadarPart extends ItemPart {
	
	public final String preset;
	public final String entityTypeKey;
	public final boolean external;
	
	public ItemRadarPart(float weight, String preset, SlotType[] compatibleSlots) {
		super(16, weight, compatibleSlots);
		this.preset = preset;
		this.external = false;
		this.entityTypeKey = "";
	}
	
	public ItemRadarPart(float weight, String preset, SlotType[] compatibleSlots, String externalEntityTypeKey) {
		super(16, weight, compatibleSlots);
		this.preset = preset;
		this.external = true;
		this.entityTypeKey = externalEntityTypeKey;
	}

	@Override
	public PartData getPartData() {
		if (external) return new ExternalRadarPartData(weight, preset, UtilItem.getItemKey(this), compatibleSlots, entityTypeKey);
		return new RadarPartData(weight, preset, UtilItem.getItemKey(this), compatibleSlots);
	}

}
