package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class RadioData extends PartData {
	
	public RadioData(ResourceLocation itemid) {
		super(0.1f, itemid, SlotType.INTERNAL_ALL);
	}

	public RadioData(CompoundTag tag) {
		super(tag);
	}

	public RadioData(FriendlyByteBuf buffer) {
		super(buffer);
	}
	
	@Override
	public PartType getType() {
		return PartType.RADIO;
	}

	@Override
	public boolean isSetup(String slotId, EntityAircraft craft) {
		// TODO Auto-generated method stub
		return false;
	}

}
