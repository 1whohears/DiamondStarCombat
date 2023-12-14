package com.onewhohears.dscombat.client.entityscreen;

import java.util.List;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.data.radar.RadarData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class GroundRadarScreenInstance extends RadarScreenInstance {
	
	public static final ResourceLocation TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/ground_radar_screen_bg.png");
	
	public GroundRadarScreenInstance(int id) {
		super("ground_radar", id, TEXTURE, 512, 512,
				256, 256, 240, 20);
	}
	
	@Override
	protected void updateTexture(Entity entity) {
		clearDynamicPixels();
		EntityVehicle vehicle = (EntityVehicle)entity;
		List<RadarData.RadarPing> pings = vehicle.radarSystem.getClientRadarPings();
		int selected = vehicle.radarSystem.getClientSelectedPingIndex();
		int hover = DSCClientInputs.getRadarHoverIndex();
		// render all other pings first
		for (int i = 0; i < pings.size(); ++i) {
			if (i == selected || i == hover) continue;
			RadarData.RadarPing ping = pings.get(i);
			if (ping.terrainType.isAir()) continue;
			drawPing(ping, vehicle, false, false);
		}
		// render hover next
		if (hover > -1 && hover < pings.size() && !pings.get(hover).terrainType.isAir()) 
			drawPing(pings.get(hover), vehicle, false, true);
		// render selected last
		if (selected > -1 && selected < pings.size() && !pings.get(selected).terrainType.isAir()) 
			drawPing(pings.get(selected), vehicle, true, false);
	}
	
	@Override
	protected double getScreenDistRatio(double distance) {
		return distance * 0.002;
	}

}