package com.onewhohears.dscombat.entity.aircraft.custom;

import com.onewhohears.dscombat.client.entityscreen.EntityScreenIds;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.data.aircraft.presets.JaviPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class JaviPlane extends EntityPlane {
	
	public JaviPlane(EntityType<? extends EntityPlane> entity, Level level) {
		super(entity, level, JaviPresets.DEFAULT_JAVI_PLANE);
	}
	
	@Override
	public void addVehicleScreens() {
		screens = new EntityScreenData[4];
		screens[0] = new EntityScreenData(EntityScreenIds.AIR_RADAR_SCREEN, 
				new Vec3(0.327, 1.259, 7.19), 
				0.173f, 0.173f, 
				10f, 0f, 0f);
		screens[1] = EntityScreenIds.getDefaultHUDData(0, 0.5, 6.34375);
		screens[2] = new EntityScreenData(EntityScreenIds.RWR_SCREEN, 
				new Vec3(0.145, 1.325, 7.2), 
				0.1f, 0.1f, 
				10f, 0f, 0f);
		screens[3] = new EntityScreenData(EntityScreenIds.GROUND_RADAR_SCREEN, 
				new Vec3(-0.349, 1.259, 7.19), 
				0.173f, 0.173f, 
				10f, 0f, 0f);
	}
	
	private static final Vec3[] ENGINE_SMOKE = new Vec3[]{
			new Vec3(1.85,1.4,-4.4),
			new Vec3(-1.85,1.4,-4.4)};
	
	@Override
	public Vec3[] getAfterBurnerSmokePos() {
		return ENGINE_SMOKE;
	}

}
