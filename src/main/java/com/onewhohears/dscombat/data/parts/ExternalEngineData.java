package com.onewhohears.dscombat.data.parts;

import java.util.NoSuchElementException;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityEngine;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class ExternalEngineData extends EngineData {
	
	public final String entityTypeKey;
	
	public ExternalEngineData(EngineType engineType, float weight, float thrust, float heat, float fuelRate, 
			ResourceLocation itemid, SlotType[] compatibleSlots, String entityTypeKey) {
		super(engineType, weight, thrust, heat, fuelRate, itemid, compatibleSlots);
		this.entityTypeKey = entityTypeKey;
	}
	
	@Override
	public PartType getType() {
		return PartType.EXTERNAL_ENGINE;
	}
	
	protected EntityType<?> getEntityType() {
		try {
			return ForgeRegistries.ENTITY_TYPES.getDelegate(
					new ResourceLocation(entityTypeKey)).get().get();
		} catch(NoSuchElementException e) { 
			return ModEntities.CFM56.get(); 
		}
	}
	
	@Override
	public void serverSetup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.serverSetup(craft, slotId, pos);
		if (!isEntitySetup(slotId, craft)) {
			EntityEngine engine = (EntityEngine) getEntityType().create(craft.level);
			setUpPartEntity(engine, craft, slotId, pos, 15);
			craft.level.addFreshEntity(engine);
		}
	}
	
	@Override
	public void serverRemove(String slotId) {
		super.serverRemove(slotId);
		removeEntity(slotId);
	}

}
