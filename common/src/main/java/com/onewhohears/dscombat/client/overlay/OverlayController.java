package com.onewhohears.dscombat.client.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

public class OverlayController {

    public static void onRenderHud(PoseStack poseStack, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getScreenWidth();
        int height = mc.getWindow().getScreenHeight();

        WindTunnelOverlay.render(mc.gui, poseStack, partialTicks, width, height);
        VehicleOverlayComponent.renderAll(mc.gui, poseStack, partialTicks, width, height);
    }
}
