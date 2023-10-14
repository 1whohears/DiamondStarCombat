package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.UtilEntity;

import java.awt.*;

import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.PEDAL_HEIGHT;
import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.STICK_BASE_SIZE;
import static com.onewhohears.dscombat.client.overlay.components.VehicleFuelOverlay.FUEL_GAUGE_HEIGHT;

public class VehicleStatsOverlay extends VehicleOverlayComponent {
    public static final Color GREEN_ME_SAY_ALONE_RAMP = new Color(0, 255, 0);
    public static final Color RED = new Color(255, 0, 0);
    public static final float START = 0.6f;
    public static final float END = 0.1f;
    public static final float CHANGE_G = (float) GREEN_ME_SAY_ALONE_RAMP.getGreen() / (START - END);
    public static final float CHANGE_R = (float) RED.getRed() / (START - END);

    public VehicleStatsOverlay(PoseStack poseStack, int screenWidth, int screenHeight) {
        super(poseStack, screenWidth, screenHeight);
    }

    @Override
    public void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!(getPlayerVehicle() instanceof EntityAircraft vehicle)) return;

        int xOrigin = screenWidth - STICK_BASE_SIZE - PADDING;
        int yOrigin = screenHeight - STICK_BASE_SIZE - PADDING * 3 - FUEL_GAUGE_HEIGHT - 10;
        
        if (vehicle.isAircraft()) yOrigin -= PEDAL_HEIGHT;

        drawString(poseStack, getFont(),
                "m/s: "+String.format("%3.1f", vehicle.getDeltaMovement().length()*20),
                xOrigin, yOrigin,
                0x00ff00);
        drawString(poseStack, getFont(),
                "A: "+ UtilEntity.getDistFromSeaLevel(vehicle),
                xOrigin, yOrigin-10,
                0x00ff00);

        float h = vehicle.getHealth(), max = vehicle.getMaxHealth();
        drawString(poseStack, getFont(),
                "H: "+(int)h+"/"+(int)max,
                xOrigin, yOrigin-20,
                getHealthColor(h, max));
        drawCenteredString(poseStack, getFont(),
                "["+vehicle.getBlockX()+","+vehicle.getBlockY()+","+vehicle.getBlockZ()+"]",
                screenWidth / 2, 0, 0x00ff00);
    }

    private static int getHealthColor(float health, float max) {
        float r = health / max;
        if (r >= START) return GREEN_ME_SAY_ALONE_RAMP.getRGB();
        if (r < START && r > END) {
            return new Color(
                    (int)(CHANGE_R *(START -r)),
                    (int)(CHANGE_G *(r- END)),
                    0).getRGB();
        }
        return RED.getRGB();
    }
}
