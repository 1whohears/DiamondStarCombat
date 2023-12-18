package com.onewhohears.dscombat.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.onewhohears.dscombat.client.overlay.components.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class VehicleOverlay {
	public static final IGuiOverlay HUD_Aircraft_Stats = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
		if (Minecraft.getInstance().options.hideGui) return;
		if (Minecraft.getInstance().gameMode.getPlayerMode() == GameType.SPECTATOR) return;

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		// TODO: make method bodies in these subclasses consistent with style
		DebugOverlay			.renderIfAllowed(poseStack, screenWidth, screenHeight);
		HudOverlay				.renderIfAllowed(poseStack, screenWidth, screenHeight);
		PlaneAttitudeOverlay	.renderIfAllowed(poseStack, screenWidth, screenHeight);
		PlaneDataOverlay		.renderIfAllowed(poseStack, screenWidth, screenHeight);
		RadarOverlay			.renderIfAllowed(poseStack, screenWidth, screenHeight, partialTick);
		TurnCoordinatorOverlay	.renderIfAllowed(poseStack, screenWidth, screenHeight);
		TurretDataOverlay		.renderIfAllowed(poseStack, screenWidth, screenHeight);
		VehicleCompassOverlay	.renderIfAllowed(poseStack, screenWidth, screenHeight);
		VehicleControlOverlay	.renderIfAllowed(poseStack, screenWidth, screenHeight);
		VehicleFuelOverlay		.renderIfAllowed(poseStack, screenWidth, screenHeight);
		VehicleStatsOverlay		.renderIfAllowed(poseStack, screenWidth, screenHeight);
		VehicleThrottleOverlay	.renderIfAllowed(poseStack, screenWidth, screenHeight);
		VehicleWeaponsOverlay	.renderIfAllowed(poseStack, screenWidth, screenHeight);
	});
	
}
