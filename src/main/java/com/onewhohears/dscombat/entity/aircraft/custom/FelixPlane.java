package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.client.entityscreen.EntityScreenTypes;
import com.onewhohears.dscombat.client.entityscreen.HudScreenInstance;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData.Builder;
import com.onewhohears.dscombat.data.aircraft.LiftKGraph;
import com.onewhohears.dscombat.data.aircraft.presets.FelixPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FelixPlane extends EntityPlane {
	
	public static final ImmutableVehicleData FELIX_PLANE_DATA = Builder.create(FelixPresets.DEFAULT_FELIX_PLANE)
			.setInteralEngineSound(ModSounds.JET_1)
			.setExternalEngineSound(ModSounds.JET_1)
			.setNegativeThrottle(false)
			.setRotationalInertia(4.5f, 9, 3)
			.setCrashExplosionRadius(5)
			.setCameraDistance(17)
			.setSpinRate(0)
			.setLiftKGraph(LiftKGraph.ALEXIS_PLANE_GRAPH)
			.setFlapsAOABias(8f)
			.setCanAimDown(false)
			.setTextureNum(2, 2)
			.build();
	
	public FelixPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, FELIX_PLANE_DATA);
	}
	
	@Override
	public void addVehicleScreens() {
		screens = new EntityScreenData[3];
		screens[0] = new EntityScreenData(EntityScreenTypes.AIR_RADAR_SCREEN, 
				new Vec3(0.29, 0.438, 5.42), 
				0.185f, 0.185f, 
				15f, 0f, 0f);
		screens[1] = new EntityScreenData(EntityScreenTypes.FUEL_SCREEN, 
				new Vec3(-0.39, 0.135, 5.155), 
				0.1f, 0.1f, 
				90f, 0f, 0f);
		screens[2] = HudScreenInstance.getDefaultData(0, -0.3, 4.7);
	}

}
