package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData.Builder;
import com.onewhohears.dscombat.data.aircraft.LiftKGraph;
import com.onewhohears.dscombat.data.aircraft.presets.JasonPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class JasonPlane extends EntityPlane {
	
	public static final ImmutableVehicleData JASON_PLANE_DATA = Builder.create(JasonPresets.DEFAULT_JASON_PLANE)
			.setInteralEngineSound(ModSounds.BIPLANE_1)
			.setExternalEngineSound(ModSounds.BIPLANE_1)
			.setNegativeThrottle(false)
			.setRotationalInertia(5, 9, 3)
			.setCrashExplosionRadius(3)
			.setCameraDistance(8)
			.setSpinRate(360)
			.setLiftKGraph(LiftKGraph.WOODEN_PLANE_GRAPH)
			.setFlapsAOABias(8f)
			.setCanAimDown(false)
			.setTextureNum(3, 3)
			.build();
	
	public JasonPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, JasonPresets.DEFAULT_JASON_PLANE);
	}

}
