package com.onewhohears.dscombat.data.parts;

import java.util.List;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class SeatData extends PartData {

	public SeatData(String id) {
		super(id);
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
	public void setup(EntityAbstractAircraft craft, Vec3 pos) {
		super.setup(craft, pos);
		List<Entity> passengers = craft.getPassengers();
		for (Entity p : passengers) {
			System.out.println("CHECK SEAT "+p);
			if (p instanceof EntitySeat seat) {
			if (seat.getPartId().equals(this.getId())) {
				System.out.println("ALREADY SEAT");
				return;
			} }
		}
		EntitySeat seat = new EntitySeat(craft.level, this.getId(), pos);
		seat.setPos(craft.position());
		seat.startRiding(craft);
		craft.level.addFreshEntity(seat);
		System.out.println("ADDED SEAT "+seat);
	}
	
	@Override
	public void remove(EntityAbstractAircraft craft) {
		// TODO remove seat
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
		// TODO item doesn't have nbt?
		CompoundTag tag = this.write();
		System.out.println("getting item data "+this+" compound "+tag);
		return new ItemStack(ModItems.SEAT.get(), 1, tag);
	}

}
