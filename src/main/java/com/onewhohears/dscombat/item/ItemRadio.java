package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;

import net.minecraftforge.registries.ForgeRegistries;

import com.onewhohears.dscombat.data.parts.RadioData;

public class ItemRadio extends ItemPart {

	protected ItemRadio() {
		super(1, 0.1f, SlotType.INTERNAL_ALL);
	}

	@Override
	public PartData getPartData() {
		return new RadioData(ForgeRegistries.ITEMS.getKey(this), "");
	}

}
