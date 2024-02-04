package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.client.entityscreen.EntityScreenIds;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.data.aircraft.presets.AlexisPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AlexisPlane extends EntityPlane {
	
	public AlexisPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, AlexisPresets.DEFAULT_ALEXIS_PLANE.getId());
	}
	
	@Override
	public void addVehicleScreens() {
		screens = new EntityScreenData[5];
		screens[0] = new EntityScreenData(EntityScreenIds.AIR_RADAR_SCREEN, 
				new Vec3(0.225, 0.798, 7.195), 
				0.15f, 0.15f, 
				0f, 0f, 0f);
		screens[1] = new EntityScreenData(EntityScreenIds.FUEL_SCREEN, 
				new Vec3(-0.265, 0.948, 7.195), 
				0.07f, 0.07f, 
				0f, 0f, 0f);
		screens[2] = EntityScreenIds.getDefaultHUDData(0, 0.1, 6.5);
		screens[3] = new EntityScreenData(EntityScreenIds.RWR_SCREEN, 
				new Vec3(0.19, 0.974, 7.195), 
				0.13f, 0.13f, 
				0f, 0f, 0f);
		screens[4] = new EntityScreenData(EntityScreenIds.GROUND_RADAR_SCREEN, 
				new Vec3(-0.225, 0.798, 7.195), 
				0.15f, 0.15f, 
				0f, 0f, 0f);
	}
	
	private static final Vec3[] ENGINE_SMOKE = new Vec3[]{new Vec3(0,0.3,-6.5)};
	
	@Override
	public Vec3[] getAfterBurnerSmokePos() {
		return ENGINE_SMOKE;
	}

}
