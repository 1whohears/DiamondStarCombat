package com.onewhohears.dscombat.client.renderer.texture;

import java.util.List;

import com.onewhohears.dscombat.client.event.forgebus.ClientInputEvents;
import com.onewhohears.dscombat.client.overlay.components.RadarOverlay;
import com.onewhohears.dscombat.data.radar.RadarData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RadarScreenInstance extends EntityScreenInstance {
	
	public RadarScreenInstance(int id) {
		super("radar", id, RadarOverlay.RADAR);
	}
	
	@Override
	public boolean shouldUpdateTexture(Entity entity) {
		EntityVehicle vehicle = (EntityVehicle)entity;
		return vehicle.radarSystem.checkClientPingsRefreshed();
	}
	
	protected void drawPing(int x, int y, int color) {
		// TODO 1.2 draw cool shapes for radar pings
		drawCircle(x, y, 4, color);
	}
	
	@Override
	protected void updateTexture(Entity entity) {
		clearPixels();
		EntityVehicle vehicle = (EntityVehicle)entity;
		List<RadarData.RadarPing> pings = vehicle.radarSystem.getClientRadarPings();
		int selected = vehicle.radarSystem.getClientSelectedPingIndex();
		int hover = ClientInputEvents.getHoverIndex();
		int width = dynamicTexture.getPixels().getWidth();
		int height = dynamicTexture.getPixels().getHeight();
		int centerX = width/2, centerY = height/2;
		int screenRadius = height/2;
		// TODO 1.4 how to deal with pings clumping on top of each other
		for (int i = 0; i < pings.size(); ++i) {
			RadarData.RadarPing ping = pings.get(i);
			Vec3 dp = ping.getPosForClient().subtract(vehicle.position());
			double dist = dp.horizontalDistance();
			double screen_dist = dist/ClientInputEvents.getRadarDisplayRange();
			if (screen_dist > 1) screen_dist = 1;
			float yaw = (UtilAngles.getYaw(dp)-vehicle.getYRot())*Mth.DEG_TO_RAD;
			int x = Math.min(centerX + (int)(-Mth.sin(yaw)*screenRadius*screen_dist), width-1);
			int y = Math.min(centerY + (int)(Mth.cos(yaw)*screenRadius*screen_dist), height-1);
			// ABGR format for some reason
			int color = 0xff00ff00;
			if (i == selected) color = 0xff0000ff;
			else if (i == hover) color = 0xff00ffff;
			else if (ping.isFriendly) color = 0xffff0000;
			else if (ping.isShared()) color = 0xffaacd66;
			drawPing(x, y, color);
		}
	}

}
