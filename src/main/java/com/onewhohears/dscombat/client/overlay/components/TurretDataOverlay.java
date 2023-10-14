package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.parts.EntityTurret;

public class TurretDataOverlay extends VehicleOverlayComponent {
    public TurretDataOverlay(PoseStack poseStack, int screenWidth, int screenHeight) {
        super(poseStack, screenWidth, screenHeight);
    }

    @Override
    public void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!(getPlayerVehicle() instanceof EntityTurret turret)) return;

        drawString(poseStack, getFont(),
                "Turret: "+turret.getAmmo(),
                screenWidth / 2-100, 1, 0xffff00);
    }
}
