package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.data.aircraft.presets.FelixPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FelixPlane extends EntityPlane {
	
	public FelixPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, FelixPresets.DEFAULT_FELIX_PLANE.getId());
	}
	
	private static final Vec3[] ENGINE_SMOKE = new Vec3[]{new Vec3(0,-0.1,-6)};
	
	@Override
	public Vec3[] getAfterBurnerSmokePos() {
		return ENGINE_SMOKE;
	}

}
