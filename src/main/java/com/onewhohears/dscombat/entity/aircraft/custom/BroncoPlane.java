package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.data.aircraft.presets.BroncoPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BroncoPlane extends EntityPlane {
	
	public BroncoPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, BroncoPresets.DEFAULT_BRONCO_PLANE.getId());
	}

}
