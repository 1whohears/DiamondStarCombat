package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.client.renderer.texture.HudScreenInstance;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData.Builder;
import com.onewhohears.dscombat.data.aircraft.presets.HeliPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityHelicopter;
import com.onewhohears.dscombat.init.ModSounds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.onewhohears.dscombat.client.renderer.texture.EntityScreenTypes.HUD_SCREEN;
import static com.onewhohears.dscombat.client.renderer.texture.EntityScreenTypes.RADAR_SCREEN;

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
		screens = new EntityScreenData[2];
		screens[0] = new EntityScreenData(RADAR_SCREEN,
				new Vec3(0.35, 0.05, 1.83), 
				0.6f, 0.6f, 
				20f, 0f, 0f);
		screens[1] = HudScreenInstance.DEFAULT_DATA;
	}

}
