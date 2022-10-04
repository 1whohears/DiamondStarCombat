package com.onewhohears.dscombat.client.overlay;

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
					width/2-60, height-60, 0x00ff00);
			// plane speed
			int s = (int)(plane.getDeltaMovement().length() * 20d);
			GuiComponent.drawString(poseStack, m.font, 
					"m/s: "+s, 
					width/2-60, height-50, 0x00ff00);
			// distance from ground
			GuiComponent.drawString(poseStack, m.font, 
					"H: "+UtilGeometry.getDistFromGround(plane), 
					width/2-60, height-40, 0x00ff00);
			// weapon data
			WeaponData weapon = plane.getPartsManager().getWeapons().getSelected();
			if (weapon != null) {
				GuiComponent.drawString(poseStack, m.font, 
						"Weapon: "+weapon.getId()+" "+weapon.getCurrentAmmo()+"/"+weapon.getMaxAmmo(), 
						width/2-60, height-30, 0x0000ff);
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
								width/2-60, height-20, 0xff0000);
					}
				}
			}
		}
	});
	
}
