package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class ChainHookData extends PartData {

	public ChainHookData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots, String entityTypeKey) {
		super(weight, itemid, compatibleSlots);
		this.entityTypeKey = entityTypeKey;
	}

	@Override
	public PartType getType() {
		return PartType.CHAIN_HOOK;
	}

	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}
	
	@Override
	public EntityType<?> getDefaultExternalEntity() {
		return ModEntities.CHAIN_HOOK.get();
	}
	
	@Override
	public float getExternalEntityDefaultHealth() {
		return 50;
	}

}
