package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.client.entityscreen.EntityScreenTypes;
import com.onewhohears.dscombat.client.entityscreen.HudScreenInstance;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData;
import com.onewhohears.dscombat.data.aircraft.LiftKGraph;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData.Builder;
import com.onewhohears.dscombat.data.aircraft.presets.AlexisPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AlexisPlane extends EntityPlane {
	
	public static final ImmutableVehicleData ALEXIS_PLANE_DATA = Builder.create(AlexisPresets.DEFAULT_ALEXIS_PLANE)
			.setInteralEngineSound(ModSounds.JET_1)
			.setExternalEngineSound(ModSounds.JET_1)
			.setNegativeThrottle(false)
			.setRotationalInertia(4, 8, 2)
			.setCrashExplosionRadius(5)
			.setCameraDistance(17)
			.setSpinRate(0)
			.setLiftKGraph(LiftKGraph.ALEXIS_PLANE_GRAPH)
			.setFlapsAOABias(8f)
			.setCanAimDown(false)
			.setTextureNum(2, 2)
			.build();
	
	public AlexisPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, ALEXIS_PLANE_DATA);
	}
	
	@Override
	public void addVehicleScreens() {
		screens = new EntityScreenData[4];
		screens[0] = new EntityScreenData(EntityScreenTypes.RADAR_SCREEN, 
				new Vec3(0.225, 0.798, 7.195), 
				0.15f, 0.15f, 
				0f, 0f, 0f);
		screens[1] = new EntityScreenData(EntityScreenTypes.FUEL_SCREEN, 
				new Vec3(-0.265, 0.948, 7.195), 
				0.07f, 0.07f, 
				0f, 0f, 0f);
		screens[2] = HudScreenInstance.getDefaultData(0, 0.1, 6.5);
		screens[3] = new EntityScreenData(EntityScreenTypes.RWR_SCREEN, 
				new Vec3(0.19, 0.974, 7.195), 
				0.13f, 0.13f, 
				0f, 0f, 0f);
	}

}
