package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilParse;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class DebugOverlay extends VehicleOverlayComponent {
    @Override
    protected boolean shouldRender(Gui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        return getPlayerRootVehicle() instanceof EntityVehicle && Config.CLIENT.debugMode.get();
    }

    @Override
    protected void render(Gui gui, PoseStack stack, float partialTick, int screenWidth, int screenHeight) {
        EntityVehicle vehicle = (EntityVehicle) getPlayerRootVehicle();
        assert vehicle != null;

        int color = 0x00ff00;
        int space = 160;
        drawString(stack, FONT,
                "V"+ UtilParse.prettyVec3(vehicle.getDeltaMovement(), 2),
                screenWidth - space, 0, color);
        drawString(stack, FONT,
                "F"+UtilParse.prettyVec3(vehicle.forces, 2),
                screenWidth - space, 10, color);
        drawString(stack, FONT,
                "A"+UtilParse.prettyVec3(vehicle.getAngularVel(), 2),
                screenWidth - space, 20, color);
        drawString(stack, FONT,
                "M"+UtilParse.prettyVec3(vehicle.getMoment(), 2),
                screenWidth - space, 30, color);
        drawString(stack, FONT,
                "Q"+UtilParse.prettyQ(vehicle.getClientQ(), 2),
                screenWidth - space, 40, color);
        drawString(stack, FONT,
                "YR: "+String.format("%3.2f", vehicle.getYawRate()*20)+" TR: "+String.format("%3.2f", vehicle.getActualTurnRadius()),
                screenWidth - space, 50, color);

        QuaternionF q = vehicle.getClientQ();
        Vec3 thrust = vehicle.getThrustForce(q);
        Vec3 drag = vehicle.calcTotalDrag(q);
        Vec3 lift = vehicle.calcTotalLift(q);
        double weight = vehicle.getWeightForce().y;
        double forceY = thrust.y + drag.y + lift.y + weight;
        double thrustDragXZ = thrust.horizontalDistance() - drag.horizontalDistance();
        double tdXZAcc = vehicle.getAccFromForce(thrustDragXZ, true) * 1000;

        drawString(stack, FONT,
                "FY: "+String.format("%3.1f", forceY),
                screenWidth - space, 60, color);
        drawString(stack, FONT,
                "FTD_XZ: "+String.format("%3.1f", thrustDragXZ)+" ATD_XZ: "+String.format("%3.3f", tdXZAcc),
                screenWidth - space, 70, color);
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_debug";
    }
}
