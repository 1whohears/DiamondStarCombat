package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.data.aircraft.presets.JaviPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class JaviPlane extends EntityPlane {
	
	public JaviPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, JaviPresets.DEFAULT_JAVI_PLANE.getId());
	}
	
	private static final Vec3[] ENGINE_SMOKE = new Vec3[]{
			new Vec3(1.85,1.4,-4.4),
			new Vec3(-1.85,1.4,-4.4)};
	
	@Override
	public Vec3[] getAfterBurnerSmokePos() {
		return ENGINE_SMOKE;
	}

}
