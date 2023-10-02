package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.parts.EntityEngine;
import com.onewhohears.dscombat.entity.parts.EntityVehiclePart;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;

public class ExternalEngineData extends EngineData {
	
	public ExternalEngineData(EngineType engineType, float weight, float thrust, float heat, float fuelRate, 
			ResourceLocation itemid, SlotType[] compatibleSlots, String modelId, EntityDimensions size) {
		super(engineType, weight, thrust, heat, fuelRate, itemid, compatibleSlots, modelId, size);
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
	
	@Override
	public EntityVehiclePart getPartEntity() {
		return new EntityEngine(getParent(), getModelId(), getExternalEntitySize(), getSlotId(), getRelPos(), getZRot());
	}
	
	@Override
	public boolean hasExternalPartEntity() {
		return true;
	}

}
