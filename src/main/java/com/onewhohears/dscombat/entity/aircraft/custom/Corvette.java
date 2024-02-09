package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.data.aircraft.presets.BoatPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityBoat;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Corvette extends EntityBoat {
	
	public Corvette(EntityType<? extends EntityBoat> entity, Level level) {
		super(entity, level, BoatPresets.DEFAULT_CORVETTE.getId());
	}
	
}
