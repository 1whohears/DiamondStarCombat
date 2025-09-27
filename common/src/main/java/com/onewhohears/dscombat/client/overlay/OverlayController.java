package com.onewhohears.dscombat.client.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.onewholibs.util.math.Mat4f;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class OverlayController {

    @NotNull
    public static Mat4f PROJECTION_MATRIX = new Mat4f();

    public static void onRenderHud(PoseStack poseStack, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        WindTunnelOverlay.render(mc.gui, poseStack, partialTicks, width, height);
        VehicleOverlayComponent.renderAll(mc.gui, poseStack, partialTicks, width, height);
    }
}
