package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class TurrentData extends SeatData {
	
	public TurrentData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(weight, itemid, compatibleSlots);
	}

	public TurrentData(CompoundTag tag) {
		super(tag);
	}

	public TurrentData(FriendlyByteBuf buffer) {
		super(buffer);
	}
	
	@Override
	public PartType getType() {
		return PartType.TURRENT;
	}
	
	// TODO a passenger seat with a gun
	
}
