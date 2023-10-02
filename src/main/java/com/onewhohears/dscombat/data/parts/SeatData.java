package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntityVehiclePart;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.Vec3;

public class SeatData extends PartData {
	
	public SeatData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(weight, itemid, compatibleSlots, "", EntityDimensions.scalable(0.8f, 0.8f));
	}
	
	protected SeatData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots,
			String modelId, EntityDimensions size) {
		super(weight, itemid, compatibleSlots, modelId, size);
	}

	public SeatData(CompoundTag tag) {
		super(tag);
	}

	public SeatData(FriendlyByteBuf buffer) {
		super(buffer);
	}

	@Override
	public PartType getType() {
		return PartType.SEAT;
	}
	
	@Override
	public EntityVehiclePart getPartEntity() {
		return new EntitySeat(getParent(), getModelId(), getExternalEntitySize(),
				getSlotId(), getRelPos(), getZRot(), Vec3.ZERO);
	}

	@Override
	public boolean hasExternalPartEntity() {
		return true;
	}

}
