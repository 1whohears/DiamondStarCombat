package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class SeatData extends PartData {
	
	public SeatData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(weight, itemid, compatibleSlots);
	}

	@Override
	public PartType getType() {
		return PartType.SEAT;
	}

	@Override
	public void serverSetup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.serverSetup(craft, slotId, pos);
		if (isSetup(slotId, craft)) {
			//System.out.println("ALREADY SEAT "+slotId);
			return;
		}
		EntitySeat seat = ModEntities.SEAT.get().create(craft.level);
		seat.setSlotId(slotId);
		seat.setRelativePos(getRelPos());
		seat.setPos(craft.position());
		seat.startRiding(craft);
		craft.level.addFreshEntity(seat);
		//System.out.println("ADDED SEAT "+seat);
	}
	
	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return isEntitySetup(slotId, craft);
	}
	
	@Override
	public void serverRemove(String slotId) {
		super.serverRemove(slotId);
		removeEntity(slotId);
	}

}
