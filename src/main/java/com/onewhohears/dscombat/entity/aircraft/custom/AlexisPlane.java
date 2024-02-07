package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.data.aircraft.presets.AlexisPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AlexisPlane extends EntityPlane {
	
	public AlexisPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, AlexisPresets.DEFAULT_ALEXIS_PLANE.getId());
	}
	
	private static final Vec3[] ENGINE_SMOKE = new Vec3[]{new Vec3(0,0.3,-6.5)};
	
	@Override
	public Vec3[] getAfterBurnerSmokePos() {
		return ENGINE_SMOKE;
	}

}
