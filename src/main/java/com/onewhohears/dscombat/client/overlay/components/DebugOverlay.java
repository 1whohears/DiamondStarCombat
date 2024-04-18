package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.UtilParse;

import java.util.Objects;

public class DebugOverlay extends VehicleOverlayComponent {
    private static DebugOverlay INSTANCE;

    public static void renderIfAllowed(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (Objects.isNull(INSTANCE)) INSTANCE = new DebugOverlay();
        INSTANCE.render(poseStack, screenWidth, screenHeight);
    }

    private DebugOverlay() {}

    @Override
    public void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if ((!(getPlayerRootVehicle() instanceof EntityVehicle vehicle)) || !(Config.CLIENT.debugMode.get())) return;

        int color = 0x00ff00;
        int space = 120;
        drawString(poseStack, getFont(),
                "V"+ UtilParse.prettyVec3(vehicle.getDeltaMovement(), 2),
                screenWidth - space, 0, color);
        drawString(poseStack, getFont(),
                "F"+UtilParse.prettyVec3(vehicle.forces, 2),
                screenWidth - space, 10, color);
        drawString(poseStack, getFont(),
                "A"+UtilParse.prettyVec3(vehicle.getAngularVel(), 2),
                screenWidth - space, 20, color);
        drawString(poseStack, getFont(),
                "M"+UtilParse.prettyVec3(vehicle.getMoment(), 2),
                screenWidth - space, 30, color);
        drawString(poseStack, getFont(),
                "Q"+UtilParse.prettyQ(vehicle.getClientQ(), 2),
                screenWidth - space, 40, color);
    }
}
