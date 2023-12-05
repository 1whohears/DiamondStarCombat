package com.onewhohears.dscombat.client.entityscreen;

import java.util.List;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RWRScreenInstance extends EntityDynamicScreenInstance {
	
	public static final ResourceLocation RWR_SCREEN_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/rwr_screen_bg.png");
	
	protected final int width, height, centerX, centerY, textureRadius;
	
	public RWRScreenInstance(int id) {
		super("rwr", id, RWR_SCREEN_TEXTURE);
		width = 512;
		height = 512;
		textureRadius = 400;
		centerX = width/2; 
		centerY = height/2;
	}
	
	@Override
	public boolean shouldUpdateTexture(Entity entity) {
		EntityVehicle vehicle = (EntityVehicle)entity;
		return vehicle.radarSystem.shouldUpdateRWRTexture();
	}
	
	@Override
	protected void updateTexture(Entity entity) {
		clearDynamicPixels();
		EntityVehicle vehicle = (EntityVehicle)entity;
		List<RadarSystem.RWRWarning> warnings = vehicle.radarSystem.getClientRWRWarnings();
		for (int i = 0; i < warnings.size(); ++i) drawWarning(warnings.get(i), vehicle);
	}
	
	protected void drawWarning(RadarSystem.RWRWarning warn, EntityVehicle vehicle) {
		Vec3 dp = warn.pos.subtract(vehicle.position());
		double dist = dp.horizontalDistance();
		double screen_dist = dist*0.001;
		if (screen_dist > 1) screen_dist = 1;
		else if (screen_dist < 0.4) screen_dist = 0.4;
		float yaw = (UtilAngles.getYaw(dp)-vehicle.getYRot()+180)*Mth.DEG_TO_RAD;
		int x = Math.min(centerX + (int)(-Mth.sin(yaw)*textureRadius*screen_dist), width-1);
		int y = Math.min(centerY + (int)(Mth.cos(yaw)*textureRadius*screen_dist), height-1);
		drawWarningAtPos(warn, x, y);
	}
	
	protected void drawWarningAtPos(RadarSystem.RWRWarning warn, int x, int y) {
		int color = 0xff00ff00;
		if (warn.isMissile) color = 0xff0000ff;
		drawDiamond(x, y, 32, 5, color); 
	}

}
