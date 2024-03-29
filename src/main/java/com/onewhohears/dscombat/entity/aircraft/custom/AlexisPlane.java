package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.data.aircraft.presets.AlexisPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AlexisPlane extends EntityPlane {
	
	public AlexisPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, AlexisPresets.DEFAULT_ALEXIS_PLANE.getId());
	}

}
