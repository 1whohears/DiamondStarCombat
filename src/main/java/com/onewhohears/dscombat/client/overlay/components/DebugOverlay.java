package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.UtilParse;

public class DebugOverlay extends VehicleOverlayComponent {
    public DebugOverlay(PoseStack poseStack, int screenWidth, int screenHeight) {
        super(poseStack, screenWidth, screenHeight);
    }

    @Override
    public void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if ((!(getPlayerVehicle() instanceof EntityVehicle vehicle)) || !(Config.CLIENT.debugMode.get())) return;

        int color = 0x00ff00;
        drawString(poseStack, getFont(),
                "V"+ UtilParse.prettyVec3(vehicle.getDeltaMovement(), 2),
                screenWidth - 100, 0, color);
        drawString(poseStack, getFont(),
                "F"+UtilParse.prettyVec3(vehicle.forces, 2),
                screenWidth - 100, 10, color);
        drawString(poseStack, getFont(),
                "A"+UtilParse.prettyVec3(vehicle.getAngularVel(), 2),
                screenWidth - 100, 20, color);
        drawString(poseStack, getFont(),
                "M"+UtilParse.prettyVec3(vehicle.getMoment(), 2),
                screenWidth - 100, 30, color);
    }
}
