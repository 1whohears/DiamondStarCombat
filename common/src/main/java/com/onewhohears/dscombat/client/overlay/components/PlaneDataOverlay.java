package com.onewhohears.dscombat.client.overlay.components;

import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.vehicle.EntityPlane;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.PEDAL_HEIGHT;
import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.STICK_BASE_SIZE;
import static com.onewhohears.dscombat.client.screen.VehicleHealthScreen.FUEL_GAUGE_HEIGHT;

public class PlaneDataOverlay extends VehicleOverlayComponent {
    @Override
    protected boolean shouldRender(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        if (!(getPlayerRootVehicle() instanceof EntityPlane)) return false;
        return !DSCClientInputs.isCameraLockedForward();
    }

    @Override
    protected void render(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        EntityPlane plane = (EntityPlane) getPlayerRootVehicle();
        assert plane != null;

        int color = 0x00ff00;
        if (plane.isStalling()) color = 0xff0000;
        else if (plane.isAboutToStall()) color = 0xffff00;

        graphics.drawString(FONT,
                String.format("AOA: %3.1f", plane.getAOA()),
                screenWidth - STICK_BASE_SIZE - PADDING,
                screenHeight - STICK_BASE_SIZE - PEDAL_HEIGHT - FUEL_GAUGE_HEIGHT - PADDING *3-50,
                color);
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_data";
    }
}
