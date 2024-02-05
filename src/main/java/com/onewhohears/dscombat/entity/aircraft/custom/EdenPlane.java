package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.data.aircraft.presets.EdenPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EdenPlane extends EntityPlane {

	public EdenPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, EdenPresets.DEFAULT_EDEN_PLANE.getId());
	}
	
	private static final Vec3[] ENGINE_SMOKE = new Vec3[]{
			new Vec3(-1,0.1,-5), new Vec3(1,0.1,-5)};
	
	@Override
	public Vec3[] getAfterBurnerSmokePos() {
		return ENGINE_SMOKE;
	}

}
