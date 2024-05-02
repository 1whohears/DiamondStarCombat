package com.onewhohears.dscombat.entity.vehicle.custom;

import com.onewhohears.dscombat.data.aircraft.presets.BoatPresets;
import com.onewhohears.dscombat.entity.vehicle.EntityBoat;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class GronkBattleship extends EntityBoat {
	
	public GronkBattleship(EntityType<? extends EntityBoat> entity, Level level) {
		super(entity, level, BoatPresets.DEFAULT_GRONK_BATTLESHIP.getId());
	}

}
