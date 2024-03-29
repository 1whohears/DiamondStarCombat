package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.data.aircraft.presets.FelixPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class FelixPlane extends EntityPlane {
	
	public FelixPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, FelixPresets.DEFAULT_FELIX_PLANE.getId());
	}

}
