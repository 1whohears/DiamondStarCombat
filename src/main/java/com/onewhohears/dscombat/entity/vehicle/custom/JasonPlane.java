package com.onewhohears.dscombat.entity.vehicle.custom;

import com.onewhohears.dscombat.data.aircraft.presets.JasonPresets;
import com.onewhohears.dscombat.entity.vehicle.EntityPlane;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class JasonPlane extends EntityPlane {
	
	public JasonPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, JasonPresets.DEFAULT_JASON_PLANE.getId());
	}

}
