package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class ExternalRadarPartData extends RadarPartData {
	
	public final String entityTypeKey;
	
	public ExternalRadarPartData(float weight, String preset, ResourceLocation itemid, SlotType[] compatibleSlots, String entityTypeKey) {
		super(weight, preset, itemid, compatibleSlots);
		this.entityTypeKey = entityTypeKey;
	}
	
	@Override
	public EntityType<?> getDefaultExternalEntity() {
		return ModEntities.SURVEY_ALL_A.get();
	}
	
	@Override
	public float getExternalEntityDefaultHealth() {
		return 15;
	}
	
	@Override
	public PartType getType() {
		return PartType.EXTERNAL_RADAR;
	}

}
