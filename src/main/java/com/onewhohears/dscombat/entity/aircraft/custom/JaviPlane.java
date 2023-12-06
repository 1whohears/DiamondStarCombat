package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.client.entityscreen.EntityScreenTypes;
import com.onewhohears.dscombat.client.entityscreen.HudScreenInstance;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData;
import com.onewhohears.dscombat.data.aircraft.LiftKGraph;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData.Builder;
import com.onewhohears.dscombat.data.aircraft.presets.JaviPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class JaviPlane extends EntityPlane {
	
	public static final ImmutableVehicleData JAVI_PLANE_DATA = Builder.create(JaviPresets.DEFAULT_JAVI_PLANE)
			.setInteralEngineSound(ModSounds.JET_1)
			.setExternalEngineSound(ModSounds.JET_1)
			.setNegativeThrottle(false)
			.setRotationalInertia(6, 10, 4)
			.setCrashExplosionRadius(5)
			.setCameraDistance(16)
			.setSpinRate(0)
			.setLiftKGraph(LiftKGraph.JAVI_PLANE_GRAPH)
			.setFlapsAOABias(10f)
			.setCanAimDown(true)
			.setTextureNum(1, 1)
			.build();
	
	public JaviPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, JAVI_PLANE_DATA);
	}
	
	@Override
	public void addVehicleScreens() {
		screens = new EntityScreenData[3];
		screens[0] = new EntityScreenData(EntityScreenTypes.RADAR_SCREEN, 
				new Vec3(0.327, 1.259, 7.19), 
				0.173f, 0.173f, 
				10f, 0f, 0f);
		screens[1] = HudScreenInstance.getDefaultData(0, 0.5, 6.34375);
		screens[2] = new EntityScreenData(EntityScreenTypes.RWR_SCREEN, 
				new Vec3(0.145, 1.325, 7.2), 
				0.1f, 0.1f, 
				10f, 0f, 0f);
	}

}
