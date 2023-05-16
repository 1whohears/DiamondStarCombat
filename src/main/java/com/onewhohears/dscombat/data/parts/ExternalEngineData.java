package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ExternalEngineData extends EngineData {

	public ExternalEngineData(EngineType engineType, float weight, float thrust, float heat, float fuelRate, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(engineType, weight, thrust, heat, fuelRate, itemid, compatibleSlots);
	}
	
	public ExternalEngineData(CompoundTag tag) {
		super(tag);
	}
	
	public ExternalEngineData(FriendlyByteBuf buffer) {
		super(buffer);
	}
	
	@Override
	public PartType getType() {
		return PartType.EXTERNAL_ENGINE;
	}

}
