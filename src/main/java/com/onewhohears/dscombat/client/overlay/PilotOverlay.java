package com.onewhohears.dscombat.client.overlay;

import java.awt.Color;
import java.util.List;

import com.onewhohears.dscombat.client.event.ClientForgeEvents;
import com.onewhohears.dscombat.data.RadarData;
import com.onewhohears.dscombat.data.RadarData.RadarPing;
import com.onewhohears.dscombat.data.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.aircraft.parts.EntitySeat;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class PilotOverlay {
	
	public static final IGuiOverlay HUD_Aircraft_Stats = ((gui, poseStack, partialTick, width, height) -> {
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player.getVehicle() instanceof EntitySeat seat 
				&& seat.getVehicle() instanceof EntityAbstractAircraft plane) {
			// plane position
			GuiComponent.drawString(poseStack, m.font, 
					"["+plane.getBlockX()+","+plane.getBlockY()+","+plane.getBlockZ()+"]", 
					width/2-100, height-70, 0x00ff00);
			// plane speed
			int s = (int)(plane.getDeltaMovement().length() * 20d);
			GuiComponent.drawString(poseStack, m.font, 
					"m/s: "+s, 
					width/2-100, height-60, 0x00ff00);
			// distance from ground
			GuiComponent.drawString(poseStack, m.font, 
					"H: "+UtilGeometry.getDistFromGround(plane), 
					width/2-100, height-50, 0x00ff00);
			// plane health
			float h = plane.getHealth(), max = plane.getMaxHealth();
			GuiComponent.drawString(poseStack, m.font, 
						"Health: "+(int)h+"/"+(int)max, 
						width/2-100, height-40, getHealthColor(h, max));
			// weapon data
			WeaponData weapon = plane.weaponSystem.getSelected();
			if (weapon != null) {
				GuiComponent.drawString(poseStack, m.font, 
						"Weapon: "+weapon.getId()+" "+weapon.getCurrentAmmo()+"/"+weapon.getMaxAmmo(), 
						width/2-100, height-30, 0x0000ff);
			}
			// target distance
			RadarData radar = plane.getRadar();
			if (radar != null) {
				List<RadarPing> pings = radar.getClientRadarPings();
				if (pings != null && pings.size() != 0) {
					int selected = radar.getClientSelectedPingIndex();
					int hover = ClientForgeEvents.getHoverIndex();
					if (hover != -1 && hover < pings.size()) {
						GuiComponent.drawString(poseStack, m.font, 
								"Dist: "+(int)pings.get(hover).pos.distanceTo(plane.position()), 
								width/2-20, height/2+10, 0xffff00);
					}
					if (selected != -1 && selected < pings.size()) {
						GuiComponent.drawString(poseStack, m.font, 
								"Target Dist: "+(int)pings.get(selected).pos.distanceTo(plane.position()), 
								width/2-100, height-20, 0xff0000);
					}
				}
			}
		}
	});
	
	private static final Color green = new Color(0, 255, 0);
	private static final Color red = new Color(255, 0, 0);
	private static final float start = 0.6f;
	private static final float end = 0.1f;
	private static final float changeG = (float)green.getGreen() / (start-end);
	private static final float changeR = (float)red.getRed() / (start-end);
	
	private static int getHealthColor(float health, float max) {
		float r = health / max;
		if (r >= start) return green.getRGB();
		if (r < start && r > end) {
			return new Color(
				(int)(changeR*(start-r)), 
				(int)(changeG*(r-end)), 
				0).getRGB();
		}
		return red.getRGB();
	}
	
}
