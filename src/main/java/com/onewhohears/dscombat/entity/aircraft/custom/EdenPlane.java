package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.data.aircraft.presets.EdenPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EdenPlane extends EntityPlane {

	public EdenPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, EdenPresets.DEFAULT_EDEN_PLANE.getId());
	}

}
