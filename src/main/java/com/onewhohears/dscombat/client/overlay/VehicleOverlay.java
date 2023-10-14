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
		new DebugOverlay(poseStack, screenWidth, screenHeight);
		new PlaneAttitudeOverlay(poseStack, screenWidth, screenHeight);
		new PlaneDataOverlay(poseStack, screenWidth, screenHeight);
		// Unfortunately, lerp lags behind by a frame if not done like this... But is that even an issue?
		RadarOverlay.setPartialTick(partialTick);
		new RadarOverlay(poseStack, screenWidth, screenHeight);
		new VehicleCompassOverlay(poseStack, screenWidth, screenHeight);
		new VehicleControlOverlay(poseStack, screenWidth, screenHeight);
		new VehicleStatsOverlay(poseStack, screenWidth, screenHeight);
		new VehicleFuelOverlay(poseStack, screenWidth, screenHeight);
		new VehicleThrottleOverlay(poseStack, screenWidth, screenHeight);
		new VehicleWeaponsOverlay(poseStack, screenWidth, screenHeight);
		new TurnCoordinatorOverlay(poseStack, screenWidth, screenHeight);
		new TurretDataOverlay(poseStack, screenWidth, screenHeight);
	});
}
