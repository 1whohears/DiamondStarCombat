package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.client.entityscreen.EntityScreenIds;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData.Builder;
import com.onewhohears.dscombat.data.aircraft.LiftKGraph;
import com.onewhohears.dscombat.data.aircraft.presets.PlanePresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class E3SentryPlane extends EntityPlane {
	
	public static final ImmutableVehicleData E3SENTRY_PLANE_DATA = Builder.create(PlanePresets.DEFAULT_E3SENTRY_PLANE)
			.setInteralEngineSound(ModSounds.JET_1)
			.setExternalEngineSound(ModSounds.JET_1)
			.setNegativeThrottle(false)
			.setRotationalInertia(10, 12, 8)
			.setCrashExplosionRadius(8)
			.setCameraDistance(12)
			.setSpinRate(Mth.PI*0.01f)
			.setLiftKGraph(LiftKGraph.E3SENTRY_PLANE_GRAPH)
			.setFlapsAOABias(10f)
			.setCanAimDown(false)
			.build();
	
	public E3SentryPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, E3SENTRY_PLANE_DATA);
	}
	
	@Override
	public void addVehicleScreens() {
		screens = new EntityScreenData[3];
		screens[0] = new EntityScreenData(EntityScreenIds.AIR_RADAR_SCREEN, 
				new Vec3(0, -0.85, 5.49), 
				0.7f, 0.7f, 
				0f, 0f, 0f);
		screens[1] = new EntityScreenData(EntityScreenIds.FUEL_SCREEN, 
				new Vec3(0.59, -0.59, 5.49), 
				0.15f, 0.15f, 
				0f, 0f, 0f);
		screens[2] = new EntityScreenData(EntityScreenIds.RWR_SCREEN, 
				new Vec3(0.83, -0.66, 5.49), 
				0.3f, 0.3f, 
				0f, 0f, 0f);
	}

}
