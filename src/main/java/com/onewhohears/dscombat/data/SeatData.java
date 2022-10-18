package com.onewhohears.dscombat.data;

import java.util.List;

import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.parts.EntitySeat;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class SeatData extends PartData {

	public SeatData(String id, Vec3 pos) {
		super(id, pos);
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
	public void setup(EntityAbstractAircraft craft) {
		super.setup(craft);
		List<Entity> passengers = craft.getPassengers();
		for (Entity p : passengers) {
			System.out.println("CHECK SEAT "+p);
			if (p instanceof EntitySeat seat) {
			if (seat.getPartId().equals(this.getId())) {
				System.out.println("ALREADY SEAT");
				return;
			} }
		}
		EntitySeat seat = new EntitySeat(craft.level, this.getId(), this.getRelativePos());
		seat.setPos(craft.position());
		seat.setRelativePos(this.getRelativePos());
		seat.startRiding(craft);
		craft.level.addFreshEntity(seat);
		System.out.println("ADDED SEAT "+seat);
	}

	@Override
	public float getWeight() {
		return 0.01f;
	}

}
