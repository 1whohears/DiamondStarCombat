package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class ExternalEngineData extends EngineData {
	
	public ExternalEngineData(EngineType engineType, float weight, float thrust, float heat, float fuelRate, 
			ResourceLocation itemid, SlotType[] compatibleSlots, String entityTypeKey) {
		super(engineType, weight, thrust, heat, fuelRate, itemid, compatibleSlots);
		this.entityTypeKey = entityTypeKey;
	}
	
	@Override
	public PartType getType() {
		return PartType.EXTERNAL_ENGINE;
	}
	
	@Override
	public EntityType<?> getDefaultExternalEntity() {
		return ModEntities.CFM56.get();
	}
	
	@Override
	public float getExternalEntityDefaultHealth() {
		return 15;
	}

}
