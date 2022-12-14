package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntitySeat;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class SeatData extends PartData {
	
	public SeatData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(weight, itemid, compatibleSlots);
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
	public void setup(EntityAircraft craft, String slotId, Vec3 pos) {
		super.setup(craft, slotId, pos);
		if (isSetup(slotId, craft)) {
			//System.out.println("ALREADY SEAT "+slotId);
			return;
		}
		EntitySeat seat = new EntitySeat(craft.level, slotId, pos);
		seat.setPos(craft.position());
		seat.startRiding(craft);
		craft.level.addFreshEntity(seat);
		//System.out.println("ADDED SEAT "+seat);
	}
	
	@Override
	public boolean isSetup(String slotId, EntityAircraft craft) {
		//System.out.println("is this seat setup "+slotId);
		for (EntitySeat seat : craft.getSeats()) {
			//System.out.println("check seat slot "+seat.getSlotId());
			if (seat.getPartType() == getType() && seat.getSlotId().equals(slotId)) 
				return true;
		}
		return false;
	}
	
	@Override
	public void remove(String slotId) {
		super.remove(slotId);
		//System.out.println("removing seat of slot "+slotId);
		for (EntitySeat seat : this.getParent().getSeats()) {
			//System.out.println("checking seat "+seat.getSlotId());
			if (seat.getSlotId().equals(slotId))
					seat.discard();
		}
	}

}
