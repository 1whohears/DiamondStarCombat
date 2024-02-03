package com.onewhohears.dscombat.data.parts;

import java.util.NoSuchElementException;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityRadar;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class ExternalRadarPartData extends RadarPartData {
	
	public final String entityTypeKey;
	
	public ExternalRadarPartData(float weight, String preset, ResourceLocation itemid, SlotType[] compatibleSlots, String entityTypeKey) {
		super(weight, preset, itemid, compatibleSlots);
		this.entityTypeKey = entityTypeKey;
	}
	
	@Override
	public void serverSetup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.serverSetup(craft, slotId, pos);
		if (!isEntitySetup(slotId, craft)) {
			EntityRadar radar = new EntityRadar(getEntityType(), craft.level, slotId, pos);
			radar.setPos(craft.position());
			radar.startRiding(craft);
			craft.level.addFreshEntity(radar);
		}
	}
	
	@Override
	public void serverRemove(String slotId) {
		super.serverRemove(slotId);
		removeEntity(slotId);
	}
	
	@Override
	public PartType getType() {
		return PartType.EXTERNAL_RADAR;
	}
	
	protected EntityType<?> getEntityType() {
		try {
			return ForgeRegistries.ENTITY_TYPES.getDelegate(
					new ResourceLocation(entityTypeKey)).get().get();
		} catch(NoSuchElementException e) { 
			return ModEntities.CFM56.get(); 
		}
	}

}
