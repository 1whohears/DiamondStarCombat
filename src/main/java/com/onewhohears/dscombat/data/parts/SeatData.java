package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class SeatData extends PartData {
	
	public SeatData(float weight) {
		super(weight);
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
	public void setup(EntityAbstractAircraft craft, String slotId, Vec3 pos) {
		super.setup(craft, slotId, pos);
		if (isSetup(slotId, craft)) {
			System.out.println("ALREADY SEAT "+slotId);
			return;
		}
		EntitySeat seat = new EntitySeat(craft.level, slotId, pos);
		seat.setPos(craft.position());
		seat.startRiding(craft);
		craft.level.addFreshEntity(seat);
		System.out.println("ADDED SEAT "+seat);
	}
	
	@Override
	public boolean isSetup(String slotId, EntityAbstractAircraft craft) {
		System.out.println("is this seat setup "+slotId);
		for (EntitySeat seat : craft.getSeats()) {
			System.out.println("check seat slot "+seat.getSlotId());
			if (seat.getSlotId().equals(slotId)) 
				return true;
		}
		return false;
	}
	
	@Override
	public void remove(String slotId) {
		System.out.println("removing seat of slot "+slotId);
		for (EntitySeat seat : this.getParent().getSeats()) {
			System.out.println("checking seat "+seat.getSlotId());
			if (seat.getSlotId().equals(slotId))
					seat.discard();
		}
	}

	@Override
	public SlotType[] getCompatibleSlots() {
		return new SlotType[]{SlotType.SEAT};
	}

	@Override
	public ItemStack getItemStack() {
		//System.out.println("getting item data "+this+" compound "+tag);
		ItemStack stack = new ItemStack(ModItems.SEAT.get(), 1);
		stack.setTag(write());
		System.out.println("created stack "+stack.toString()+" "+stack.getTag());
		return stack;
	}

}
