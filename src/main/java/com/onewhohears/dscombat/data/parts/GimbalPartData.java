package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class GimbalPartData extends PartData {
	
	public GimbalPartData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots, String entityTypeKey) {
		super(weight, itemid, compatibleSlots);
		this.entityTypeKey = entityTypeKey;
	}
	
	@Override
	public EntityType<?> getDefaultExternalEntity() {
		return ModEntities.GIMBAL_CAMERA.get();
	}
	
	@Override
	public float getExternalEntityDefaultHealth() {
		return 5;
	}

	@Override
	public PartType getType() {
		return PartType.GIMBAL;
	}

	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}

}
