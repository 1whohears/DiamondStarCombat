package com.onewhohears.dscombat.client.entityscreen;

import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.data.radar.RadarData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public abstract class RadarScreenInstance extends EntityDynamicScreenInstance {
	
	protected final int width, height, centerX, centerY, textureRadius, pingIconRadius;
	
	protected RadarScreenInstance(String path, int id, ResourceLocation baseTexture, 
			int width, int height, int centerX, int centerY, int textureRadius, int pingIconRadius) {
		super(path, id, baseTexture);
		this.width = width;
		this.height = height;
		this.centerX = centerX;
		this.centerY = centerY;
		this.textureRadius= textureRadius;
		this.pingIconRadius = pingIconRadius;
	}

	@Override
	public boolean shouldUpdateTexture(Entity entity) {
		EntityVehicle vehicle = (EntityVehicle)entity;
		if (entity.tickCount == prevUpdateTickCount) return false;
		if (entity.tickCount % 2 != 0) return false;
		if ((entity.tickCount-vehicle.radarSystem.clientPingRefreshTime) > 100) return false;
		return true;
	}
	
	protected void drawPing(RadarData.RadarPing ping, EntityVehicle vehicle, boolean selected, boolean hover) {
		Vec3 dp = ping.getPosForClient().subtract(vehicle.position());
		double dist = dp.horizontalDistance();
		double screen_dist = getScreenDistRatio(dist);
		if (screen_dist > 1) screen_dist = 1;
		float yaw = (UtilAngles.getYaw(dp)-vehicle.getYRot()+180)*Mth.DEG_TO_RAD;
		int x = Mth.clamp(centerX + (int)(-Mth.sin(yaw)*textureRadius*screen_dist), 10, width-10);
		int y = Mth.clamp(centerY + (int)(Mth.cos(yaw)*textureRadius*screen_dist), 10, height-10);
		drawPingAtPos(ping, x, y, selected, hover);
	}
	
	protected double getScreenDistRatio(double distance) {
		return distance / DSCClientInputs.getRadarDisplayRange();
	}
	
	protected void drawPingAtPos(RadarData.RadarPing ping, int x, int y, boolean selected, boolean hover) {
		// ABGR format for some reason
		int color = 0xff00ff00;
		if (selected) color = 0xff0000ff;
		else if (hover) color = 0xff00ffff;
		else if (ping.isFriendly) color = 0xffff0000;
		else if (ping.isShared()) color = 0xffaacd66;
		if (ping.terrainType.isGround()) drawPlus(x, y, pingIconRadius, 5, color);
		else if (ping.terrainType.isAir()) drawCross(x, y, pingIconRadius, 7, color);
		else {
			drawCross(x, y, pingIconRadius, 5, color);
			drawPlus(x, y, pingIconRadius, 5, color);
		}
		if (ping.isFriendly) drawHollowCircle(x, y, pingIconRadius, 2, color);
	}

}
