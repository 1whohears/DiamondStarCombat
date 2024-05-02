package com.onewhohears.dscombat.entity.vehicle.custom;

import com.onewhohears.dscombat.data.vehicle.presets.BoatPresets;
import com.onewhohears.dscombat.entity.vehicle.EntityBoat;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AircraftCarrier extends EntityBoat {
	
	public AircraftCarrier(EntityType<? extends EntityBoat> entity, Level level) {
		super(entity, level, BoatPresets.DEFAULT_AIRCRAFT_CARRIER.getId());
	}
	
}
