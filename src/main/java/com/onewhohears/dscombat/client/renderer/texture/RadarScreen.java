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

public class RadarScreen extends EntityScreen {
	
	public static final double DISPLAY_RANGE = 1000;
	
	public RadarScreen() {
		super("radar", RadarOverlay.RADAR);
	}
	
	@Override
	public boolean requiresUpload() {
		return true;
	}
	
	protected void drawPing(int x, int y, int color) {
		// TODO 1.2 draw cool shapes for radar pings
		drawCircle(x, y, 3, color);
	}
	
	@Override
	protected void updateTexture(Entity entity) {
		EntityVehicle vehicle = (EntityVehicle)entity;
		List<RadarData.RadarPing> pings = vehicle.radarSystem.getClientRadarPings();
		int selected = vehicle.radarSystem.getClientSelectedPingIndex();
		int hover = ClientInputEvents.getHoverIndex();
		int width = texture.getPixels().getWidth();
		int height = texture.getPixels().getHeight();
		int centerX = width/2, centerY = height/2;
		int screenRadius = height/2;
		for (int i = 0; i < pings.size(); ++i) {
			RadarData.RadarPing ping = pings.get(i);
			Vec3 dp = ping.getPosForClient().subtract(vehicle.position());
			double dist = dp.horizontalDistance();
			double screen_dist = dist/DISPLAY_RANGE;
			if (screen_dist > 1) screen_dist = 1;
			float yaw = (UtilAngles.getYaw(dp)-vehicle.getYRot())*Mth.DEG_TO_RAD;
			int x = Math.min(centerX + (int)(Mth.sin(yaw)*screenRadius*screen_dist), width-1);
			int y = Math.min(centerY + (int)(-Mth.cos(yaw)*screenRadius*screen_dist), height-1);
			int color = 0x00ff00ff;
			if (i == selected) color = 0xff0000ff;
			else if (i == hover) color = 0xffff00ff;
			else if (ping.isFriendly) color = 0x0000ffff;
			else if (ping.isShared()) color = 0x66cdaaff;
			drawPing(x, y, color);
		}
	}

}
