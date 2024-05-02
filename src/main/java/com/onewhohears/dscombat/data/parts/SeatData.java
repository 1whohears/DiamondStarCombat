package com.onewhohears.dscombat.data.parts;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class SeatData extends PartData {
	
	public SeatData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(weight, itemid, compatibleSlots);
		entityTypeKey = ModEntities.SEAT.getKey().location().toString();
	}

	@Override
	public PartType getType() {
		return PartType.SEAT;
	}
	
	@Nullable
	public EntityType<?> getDefaultExernalEntityType() {
		return ModEntities.SEAT.get();
	}

	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}

}
