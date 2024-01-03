package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData.Builder;
import com.onewhohears.dscombat.data.aircraft.LiftKGraph;
import com.onewhohears.dscombat.data.aircraft.presets.BroncoPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BroncoPlane extends EntityPlane {
	
	public static final ImmutableVehicleData BRONCO_PLANE_DATA = Builder.create(BroncoPresets.DEFAULT_BRONCO_PLANE)
			.setInteralEngineSound(ModSounds.BIPLANE_1)
			.setExternalEngineSound(ModSounds.BIPLANE_1)
			.setNegativeThrottle(false)
			.setRotationalInertia(6, 10, 5)
			.setCrashExplosionRadius(4)
			.setCameraDistance(14)
			.setSpinRate(360)
			.setLiftKGraph(LiftKGraph.JAVI_PLANE_GRAPH)
			.setFlapsAOABias(9f)
			.setCanAimDown(false)
			.setTextureNum(2, 1)
			.build();
	
	public BroncoPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, BRONCO_PLANE_DATA);
	}

}
