package com.onewhohears.dscombat.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class PilotOverlay {
	
	public static final IGuiOverlay HUD_Aircraft_Stats = ((gui, poseStack, partialTick, width, height) -> {
		Minecraft m = Minecraft.getInstance();
		// TODO show distance of radar ping currently hovering over
		// TODO show plane's current speed
		// TODO show plane's distance from ground
		// TODO show plane's current xyz position
		GuiComponent.drawString(poseStack, m.font, "TEST", 0, 0, 0xffffff);
	});
	
}
