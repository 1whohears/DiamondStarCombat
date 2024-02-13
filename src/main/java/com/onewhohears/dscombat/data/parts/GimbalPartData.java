package com.onewhohears.dscombat.data.parts;

import java.util.NoSuchElementException;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityGimbal;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class GimbalPartData extends PartData {
	
	public final String entityTypeKey;
	
	public GimbalPartData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots, String entityTypeKey) {
		super(weight, itemid, compatibleSlots);
		this.entityTypeKey = entityTypeKey;
	}
	
	protected EntityType<?> getEntityType() {
		try {
			return ForgeRegistries.ENTITY_TYPES.getDelegate(
				new ResourceLocation(entityTypeKey)).get().get();
		} catch(NoSuchElementException e) { 
			return ModEntities.GIMBAL_CAMERA.get(); 
		}
	}
	
	@Override
	public void serverSetup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.serverSetup(craft, slotId, pos);
		if (!isEntitySetup(slotId, craft)) {
			EntityGimbal gimbal = (EntityGimbal) getEntityType().create(craft.level);
			setUpPartEntity(gimbal, craft, slotId, pos, 5);
			craft.level.addFreshEntity(gimbal);
		}
	}
	
	@Override
	public void serverRemove(String slotId) {
		super.serverRemove(slotId);
		removeEntity(slotId);
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
