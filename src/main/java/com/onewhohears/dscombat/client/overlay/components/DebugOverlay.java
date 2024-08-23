package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.NotNull;

public class DebugOverlay extends VehicleOverlayComponent {
    @Override
    protected boolean shouldRender(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        return getPlayerRootVehicle() instanceof EntityVehicle && Config.CLIENT.debugMode.get();
    }

    @Override
    protected void render(ForgeGui gui, PoseStack stack, float partialTick, int screenWidth, int screenHeight) {
        EntityVehicle vehicle = (EntityVehicle) getPlayerRootVehicle();
        assert vehicle != null;

        int color = 0x00ff00;
        int space = 120;
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
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_debug";
    }
}
