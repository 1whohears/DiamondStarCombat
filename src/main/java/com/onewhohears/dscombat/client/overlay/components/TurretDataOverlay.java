package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.parts.EntityTurret;

public class TurretDataOverlay extends VehicleOverlayComponent {
    public TurretDataOverlay(int screenWidth, int screenHeight) {
        super(screenWidth, screenHeight);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (!(getPlayerVehicle() instanceof EntityTurret turret)) return;

        drawString(poseStack, getFont(),
                "Turret: "+turret.getAmmo(),
                this.screenWidth / 2-100, 1, 0xffff00);
    }
}
