package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class VehicleControlOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation RUDDER_PEDAL = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/rudder_pedal.png");
    public static final ResourceLocation RUDDER_PEDAL_PUSHED = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/rudder_pedal_pushed.png");
    public static final ResourceLocation STICK_BASE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/stick_base.png");
    public static final ResourceLocation STICK_KNOB = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/stick_knob.png");

    public static final int STICK_BASE_SIZE = 60, STICK_KNOB_SIZE = STICK_BASE_SIZE / 6;
    protected static int PEDAL_HEIGHT = 25, PEDAL_WIDTH = 20;

    public VehicleControlOverlay(PoseStack poseStack, int screenWidth, int screenHeight) {
        super(poseStack, screenWidth, screenHeight);
    }

    @Override
    public void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!(getPlayerVehicle() instanceof EntityVehicle vehicle)) return;

        int xOrigin = screenWidth - STICK_BASE_SIZE - PADDING;
        int yOrigin = screenHeight - PADDING;

        // rudder (yaw input)
        if (vehicle.isAircraft()) {
            yOrigin -= PEDAL_HEIGHT;
            if (vehicle.inputs.yaw < 0) RenderSystem.setShaderTexture(0, RUDDER_PEDAL_PUSHED);
            else RenderSystem.setShaderTexture(0, RUDDER_PEDAL);
            blit(poseStack,
                    xOrigin, yOrigin,
                    0, 0,
                    PEDAL_WIDTH, PEDAL_HEIGHT,
                    PEDAL_WIDTH, PEDAL_HEIGHT);
            if (vehicle.inputs.yaw > 0) RenderSystem.setShaderTexture(0, RUDDER_PEDAL_PUSHED);
            else RenderSystem.setShaderTexture(0, RUDDER_PEDAL);
            blit(poseStack,
                    xOrigin + STICK_BASE_SIZE - PEDAL_WIDTH, yOrigin,
                    0, 0,
                    PEDAL_WIDTH, PEDAL_HEIGHT,
                    PEDAL_WIDTH, PEDAL_HEIGHT);
        }

        // stick (pitch roll input)
        RenderSystem.setShaderTexture(0, STICK_BASE);
        yOrigin -= STICK_BASE_SIZE;
        blit(poseStack,
                xOrigin, yOrigin,
                0, 0,
                STICK_BASE_SIZE, STICK_BASE_SIZE,
                STICK_BASE_SIZE, STICK_BASE_SIZE);
        RenderSystem.setShaderTexture(0, STICK_KNOB);

        // FIXME idk what these single-letter variables are doing at a glance. consider renaming them

        int b = STICK_BASE_SIZE / 2, n = STICK_KNOB_SIZE / 2;
        float xinput, yinput = vehicle.inputs.pitch;

        if (vehicle.isAircraft()) xinput = vehicle.inputs.roll;
        else xinput = vehicle.inputs.yaw;

        // PIE HAT GORE-US
        float l = Mth.sqrt(xinput * xinput + yinput * yinput);
        if (l > 1) {
            xinput = xinput / l;
            yinput = yinput / l;
        }

        blit(poseStack,
                xOrigin + b - n + (int) (xinput * b),
                yOrigin + b - n + (int) (yinput * b),
                0, 0,
                STICK_KNOB_SIZE, STICK_KNOB_SIZE,
                STICK_KNOB_SIZE, STICK_KNOB_SIZE);
    }
}
