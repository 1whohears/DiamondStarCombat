package com.onewhohears.dscombat.entity.vehicle.custom;

import com.onewhohears.dscombat.data.vehicle.presets.HeliPresets;
import com.onewhohears.dscombat.entity.vehicle.EntityHelicopter;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class NoahChopper extends EntityHelicopter {
	
	public NoahChopper(EntityType<? extends EntityHelicopter> entity, Level level) {
		super(entity, level, HeliPresets.DEFAULT_NOAH_CHOPPER.getId());
	}

}
