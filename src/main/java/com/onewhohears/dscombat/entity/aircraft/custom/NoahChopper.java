package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.client.entityscreen.EntityScreenIds;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.data.aircraft.presets.HeliPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityHelicopter;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NoahChopper extends EntityHelicopter {
	
	public NoahChopper(EntityType<? extends EntityHelicopter> entity, Level level) {
		super(entity, level, HeliPresets.DEFAULT_NOAH_CHOPPER.getId());
	}
	
	@Override
	public void addVehicleScreens() {
		screens = new EntityScreenData[5];
		screens[0] = new EntityScreenData(EntityScreenIds.AIR_RADAR_SCREEN,
				new Vec3(0.19, 0.18, 1.934), 
				0.3f, 0.3f, 
				20f, 0f, 0f);
		screens[1] = EntityScreenIds.getDefaultHUDData(0.4, -0.65, 1.5);
		screens[2] = new EntityScreenData(EntityScreenIds.FUEL_SCREEN, 
				new Vec3(0.19, -0.12, 1.825), 
				0.3f, 0.3f, 
				20f, 0f, 0f);
		screens[3] = new EntityScreenData(EntityScreenIds.RWR_SCREEN, 
				new Vec3(0.5, -0.12, 1.825), 
				0.3f, 0.3f, 
				20f, 0f, 0f);
		screens[4] = new EntityScreenData(EntityScreenIds.GROUND_RADAR_SCREEN, 
				new Vec3(0.5, 0.18, 1.934), 
				0.3f, 0.3f, 
				20f, 0f, 0f);
	}

}
