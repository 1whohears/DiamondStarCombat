package com.onewhohears.dscombat.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;

public class TestBoat extends Boat {

	public TestBoat(EntityType<? extends Boat> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	@Override
    public void positionRider(Entity passenger) {
		super.positionRider(passenger);
		passenger.setPos(passenger.position().add(5, 0, 0));
		/*
		 * FIXME 7.2 issue described in 7.1 still happens with this almost vanilla boat!
		 * therefore this issue is not caused by diamond star combat!
		 * it happens when the player passenger and the vehicle are not in the same chunk.
		 */
	}

}
