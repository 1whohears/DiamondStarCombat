package com.onewhohears.dscombat.entity.aircraft.custom;

import static com.onewhohears.dscombat.client.entityscreen.EntityScreenTypes.AIR_RADAR_SCREEN;

import com.onewhohears.dscombat.client.entityscreen.EntityScreenTypes;
import com.onewhohears.dscombat.client.entityscreen.HudScreenInstance;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData.Builder;
import com.onewhohears.dscombat.data.aircraft.presets.HeliPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityHelicopter;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NoahChopper extends EntityHelicopter {
	
	public static final ImmutableVehicleData NOAH_CHOPPER_DATA = Builder.create(HeliPresets.DEFAULT_NOAH_CHOPPER)
			.setInteralEngineSound(ModSounds.HELI_1)
			.setExternalEngineSound(ModSounds.HELI_1)
			.setNegativeThrottle(false)
			.setRotationalInertia(8, 6, 4)
			.setCrashExplosionRadius(4)
			.setCameraDistance(6)
			.setSpinRate(3.141f)
			.setAlwaysLandingGear(true)
			.setHeliLiftFactor(2.75f)
			.setTextureNum(1, 1)
			.build();
	
	public NoahChopper(EntityType<? extends EntityHelicopter> entity, Level level) {
		super(entity, level, NOAH_CHOPPER_DATA);
	}
	
	@Override
	public void addVehicleScreens() {
		screens = new EntityScreenData[5];
		screens[0] = new EntityScreenData(AIR_RADAR_SCREEN,
				new Vec3(0.19, 0.18, 1.934), 
				0.3f, 0.3f, 
				20f, 0f, 0f);
		screens[1] = HudScreenInstance.getDefaultData(0.4, -0.65, 1.5);
		screens[2] = new EntityScreenData(EntityScreenTypes.FUEL_SCREEN, 
				new Vec3(0.19, -0.12, 1.825), 
				0.3f, 0.3f, 
				20f, 0f, 0f);
		screens[3] = new EntityScreenData(EntityScreenTypes.RWR_SCREEN, 
				new Vec3(0.5, -0.12, 1.825), 
				0.3f, 0.3f, 
				20f, 0f, 0f);
		screens[4] = new EntityScreenData(EntityScreenTypes.GROUND_RADAR_SCREEN, 
				new Vec3(0.5, 0.18, 1.934), 
				0.3f, 0.3f, 
				20f, 0f, 0f);
	}

}
