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
	
	protected final int width, height, centerX, centerY, textureRadius, pingIconRadius;
	
	public RadarScreenInstance(int id) {
		super("radar", id, RadarOverlay.RADAR);
		width = dynamicTexture.getPixels().getWidth();
		height = dynamicTexture.getPixels().getHeight();
		centerX = width/2; 
		centerY = height/2;
		textureRadius = height/2;
		pingIconRadius = textureRadius / 16;
	}
	
	@Override
	public boolean shouldUpdateTexture(Entity entity) {
		EntityVehicle vehicle = (EntityVehicle)entity;
		return vehicle.radarSystem.checkClientPingsRefreshed();
	}
	
	protected void drawPingAtPos(RadarData.RadarPing ping, int x, int y, boolean selected, boolean hover) {
		// ABGR format for some reason
		int color = 0xff00ff00;
		if (selected) color = 0xff0000ff;
		else if (hover) color = 0xff00ffff;
		else if (ping.isFriendly) color = 0xffff0000;
		else if (ping.isShared()) color = 0xffaacd66;
		if (ping.terrainType.isGround()) drawPlus(x, y, pingIconRadius, color);
		else if (ping.terrainType.isAir()) drawCross(x, y, pingIconRadius, color);
		else {
			drawCross(x, y, pingIconRadius, color);
			drawPlus(x, y, pingIconRadius, color);
		}
	}
	
	@Override
	protected void updateTexture(Entity entity) {
		clearPixels();
		EntityVehicle vehicle = (EntityVehicle)entity;
		List<RadarData.RadarPing> pings = vehicle.radarSystem.getClientRadarPings();
		int selected = vehicle.radarSystem.getClientSelectedPingIndex();
		int hover = ClientInputEvents.getHoverIndex();
		// render all other pings first
		for (int i = 0; i < pings.size(); ++i) {
			if (i == selected || i == hover) continue;
			RadarData.RadarPing ping = pings.get(i);
			drawPing(ping, vehicle, false, false);
		}
		// render hover next
		if (hover > -1 && hover < pings.size()) 
			drawPing(pings.get(hover), vehicle, false, true);
		// render selected last
		if (selected > -1 && selected < pings.size()) 
			drawPing(pings.get(selected), vehicle, true, false);
	}
	
	protected void drawPing(RadarData.RadarPing ping, EntityVehicle vehicle, boolean selected, boolean hover) {
		Vec3 dp = ping.getPosForClient().subtract(vehicle.position());
		double dist = dp.horizontalDistance();
		double screen_dist = dist/ClientInputEvents.getRadarDisplayRange();
		if (screen_dist > 1) screen_dist = 1;
		float yaw = (UtilAngles.getYaw(dp)-vehicle.getYRot())*Mth.DEG_TO_RAD;
		int x = Math.min(centerX + (int)(-Mth.sin(yaw)*textureRadius*screen_dist), width-1);
		int y = Math.min(centerY + (int)(Mth.cos(yaw)*textureRadius*screen_dist), height-1);
		drawPingAtPos(ping, x, y, selected, hover);
	}

}
