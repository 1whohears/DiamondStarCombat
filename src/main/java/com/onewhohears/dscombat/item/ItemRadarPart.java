package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;

import net.minecraftforge.registries.ForgeRegistries;

import com.onewhohears.dscombat.data.parts.RadarPartData;

public class ItemRadarPart extends ItemPart {
	
	public final String preset;
	
	public ItemRadarPart(float weight, String preset, SlotType[] compatibleSlots) {
		super(1, weight, compatibleSlots);
		this.preset = preset;
	}

	@Override
	public PartData getPartData() {
		return new RadarPartData(weight, preset, ForgeRegistries.ITEMS.getKey(this), compatibleSlots);
	}

}
