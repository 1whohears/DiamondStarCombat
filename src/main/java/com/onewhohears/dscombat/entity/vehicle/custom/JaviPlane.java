package com.onewhohears.dscombat.entity.vehicle.custom;

import com.onewhohears.dscombat.data.aircraft.presets.JaviPresets;
import com.onewhohears.dscombat.entity.vehicle.EntityPlane;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class JaviPlane extends EntityPlane {
	
	public JaviPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, JaviPresets.DEFAULT_JAVI_PLANE.getId());
	}

}
