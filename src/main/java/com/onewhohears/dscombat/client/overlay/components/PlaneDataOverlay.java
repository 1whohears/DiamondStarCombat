package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;

import java.util.Objects;

import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.PEDAL_HEIGHT;
import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.STICK_BASE_SIZE;
import static com.onewhohears.dscombat.client.overlay.components.VehicleFuelOverlay.FUEL_GAUGE_HEIGHT;

public class PlaneDataOverlay extends VehicleOverlayComponent {
    private static PlaneDataOverlay INSTANCE;

    public static void renderIfAllowed(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (Objects.isNull(INSTANCE)) INSTANCE = new PlaneDataOverlay();
        INSTANCE.render(poseStack, screenWidth, screenHeight);
    }

    private PlaneDataOverlay() {}

    @Override
    protected void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!(getPlayerRootVehicle() instanceof EntityPlane plane)) return;
        if (DSCClientInputs.isCameraLockedForward()) return;
        
        int color = 0x00ff00;
        if (plane.isStalling()) color = 0xff0000;
        else if (plane.isAboutToStall()) color = 0xffff00;
        
        drawString(poseStack, getFont(),
                String.format("AOA: %3.1f", plane.getAOA()),
                screenWidth - STICK_BASE_SIZE - PADDING,
                screenHeight - STICK_BASE_SIZE - PEDAL_HEIGHT - FUEL_GAUGE_HEIGHT - PADDING *3-40,
                color);
    }
}
