package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.parts.EntityTurret;

import java.util.Objects;

public class TurretDataOverlay extends VehicleOverlayComponent {
    private static TurretDataOverlay INSTANCE;

    public static void renderIfAllowed(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (Objects.isNull(INSTANCE)) INSTANCE = new TurretDataOverlay();
        INSTANCE.render(poseStack, screenWidth, screenHeight);
    }

    private TurretDataOverlay() {}

    @Override
    protected void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!(getPlayerVehicle() instanceof EntityTurret turret)) return;

        drawString(poseStack, getFont(),
                "Turret: "+turret.getAmmo(),
                screenWidth / 2-100, 1, 0xffff00);
    }
}
