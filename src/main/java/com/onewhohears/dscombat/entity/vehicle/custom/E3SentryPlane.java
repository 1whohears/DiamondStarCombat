package com.onewhohears.dscombat.entity.vehicle.custom;

import com.onewhohears.dscombat.data.vehicle.presets.PlanePresets;
import com.onewhohears.dscombat.entity.vehicle.EntityPlane;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class E3SentryPlane extends EntityPlane {
	
	public E3SentryPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, PlanePresets.DEFAULT_E3SENTRY_PLANE.getId());
	}

}
