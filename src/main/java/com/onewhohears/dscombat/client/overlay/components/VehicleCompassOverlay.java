package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

// FIXME: FPS does NOT like this. drawCenteredString seems to be the culprit
public class VehicleCompassOverlay extends VehicleOverlayComponent {
    public VehicleCompassOverlay(int screenWidth, int screenHeight) {
        super(screenWidth, screenHeight);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (!(getPlayerVehicle() instanceof EntityAircraft)) return;

        // HEADING
        int y = 10, color = 0xe6e600;
        int heading = (int) Mth.wrapDegrees(Minecraft.getInstance().player.getYRot());
        if (heading < 0) heading += 360f;
        drawCenteredString(poseStack, getFont(),
                heading+"", this.screenWidth/2, y+20, color);
        int num = 15, degSpace = 3, degPerLine = 3, steps = 3;
        for (int i = -num; i < num; ++i) {
            int j = i*degPerLine;
            int x = this.screenWidth/2+j*degSpace-heading%degPerLine*degSpace;
            drawCenteredString(poseStack, getFont(),
                    "|", x, y+10, color);
            int hl = heading / degPerLine;
            if ((hl+i) % steps != 0) continue;
            int h = hl*degPerLine+j;
            if (h < 0) h += 360;
            else if (h >= 360) h-= 360;
            drawCenteredString(poseStack, getFont(),
                    textByHeading(h), x, y, color);
        }
        // TODO 1.2 draw pitch, roll, and cursor elements to hud
    }

    private static String textByHeading(int h) {
        if (h == 0) return "S";
        else if (h == 180) return "N";
        else if (h == 90) return "W";
        else if (h == 270) return "E";
        else if (h == 45) return "SW";
        else if (h == 135) return "NW";
        else if (h == 225) return "NE";
        else if (h == 315) return "SE";
        return h+"";
    }
}
