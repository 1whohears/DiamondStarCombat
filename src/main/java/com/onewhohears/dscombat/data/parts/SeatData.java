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
	
	public SeatData() {
		super();
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
		if (isSetup(slotId)) {
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
	public boolean isSetup(String slotId) {
		EntityAbstractAircraft p = getParent();
		if (p == null) return false;
		for (EntitySeat seat : p.getSeats()) 
			if (seat.getSlotId().equals(slotId)) 
				return true;
		return false;
	}
	
	@Override
	public void remove(String slotId) {
		for (EntitySeat seat : this.getParent().getSeats())
			if (seat.getSlotId().equals(slotId))
					seat.discard();
	}

	@Override
	public float getWeight() {
		return 0.01f;
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
