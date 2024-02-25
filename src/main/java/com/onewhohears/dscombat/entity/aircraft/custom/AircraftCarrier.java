package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.data.aircraft.presets.BoatPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityBoat;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AircraftCarrier extends EntityBoat {
	
	public AircraftCarrier(EntityType<? extends EntityBoat> entity, Level level) {
		super(entity, level, BoatPresets.DEFAULT_AIRCRAFT_CARRIER.getId());
	}
	
}
