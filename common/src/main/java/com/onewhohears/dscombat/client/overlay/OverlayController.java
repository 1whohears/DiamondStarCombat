package com.onewhohears.dscombat.client.overlay;

import com.onewhohears.onewholibs.util.math.Mat4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class OverlayController {

    @NotNull
    public static Mat4f PROJECTION_MATRIX = new Mat4f();

    public static void onRenderHud(GuiGraphics graphics, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        WindTunnelOverlay.render(mc.gui, graphics, partialTicks, width, height);
        VehicleOverlayComponent.renderAll(mc.gui, graphics, partialTicks, width, height);
    }
}
