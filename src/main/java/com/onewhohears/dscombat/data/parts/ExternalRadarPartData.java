package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.parts.EntityRadar;
import com.onewhohears.dscombat.entity.parts.EntityVehiclePart;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;

public class ExternalRadarPartData extends RadarPartData {
	
	public ExternalRadarPartData(float weight, String preset, ResourceLocation itemid, SlotType[] compatibleSlots, 
			String modelId, EntityDimensions size) {
		super(weight, preset, itemid, compatibleSlots, modelId, size);
	}
	
	public ExternalRadarPartData(CompoundTag tag) {
		super(tag);
	}

	public ExternalRadarPartData(FriendlyByteBuf buffer) {
		super(buffer);
	}
	
	@Override
	public EntityVehiclePart getPartEntity() {
		return new EntityRadar(getParent(), getModelId(), getExternalEntitySize(), getSlotId(), getRelPos(), getZRot());
	}
	
	@Override
	public boolean hasExternalPartEntity() {
		return true;
	}
	
	@Override
	public PartType getType() {
		return PartType.EXTERNAL_RADAR;
	}

}
