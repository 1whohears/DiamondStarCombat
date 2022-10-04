package com.onewhohears.dscombat.client.overlay;

import java.util.List;

import com.onewhohears.dscombat.client.event.ClientForgeEvents;
import com.onewhohears.dscombat.data.RadarData;
import com.onewhohears.dscombat.data.RadarData.RadarPing;
import com.onewhohears.dscombat.data.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.aircraft.parts.EntitySeat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class PilotOverlay {
	
	public static final IGuiOverlay HUD_Aircraft_Stats = ((gui, poseStack, partialTick, width, height) -> {
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		// TODO show plane's current speed
		// TODO show plane's distance from ground
		// TODO show plane's current xyz position
		//GuiComponent.drawString(poseStack, m.font, "TEST", 0, 0, 0xffffff);
		if (player.getVehicle() instanceof EntitySeat seat 
				&& seat.getVehicle() instanceof EntityAbstractAircraft plane) {
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
								width/2-40, height-40, 0xff0000);
					}
				}
			}
			WeaponData weapon = plane.getPartsManager().getWeapons().getSelected();
			if (weapon != null) {
				GuiComponent.drawString(poseStack, m.font, 
						"Weapon: "+weapon.getId()+" "+weapon.getCurrentAmmo()+"/"+weapon.getMaxAmmo(), 
						width/2-40, height-30, 0x0000ff);
			}
		}
	});
	
}
