package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;

import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.PEDAL_HEIGHT;
import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.STICK_BASE_SIZE;
import static com.onewhohears.dscombat.client.overlay.components.VehicleFuelOverlay.FUEL_GAUGE_HEIGHT;

public class PlaneDataOverlay extends VehicleOverlayComponent {
    public PlaneDataOverlay(PoseStack poseStack, int screenWidth, int screenHeight) {
        super(poseStack, screenWidth, screenHeight);
    }

    @Override
    public void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!(getPlayerVehicle() instanceof EntityPlane plane)) return;

        drawString(poseStack, getFont(),
                String.format("AOA: %3.1f", plane.getAOA()),
                screenWidth - STICK_BASE_SIZE - PADDING,
                screenHeight - STICK_BASE_SIZE - PEDAL_HEIGHT - FUEL_GAUGE_HEIGHT - PADDING *3-40,
                0x00ff00);
    }
}
