package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.NotNull;

// TODO: redo texture
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
    @Override
    protected boolean shouldRender(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        return getPlayerRootVehicle() instanceof EntityVehicle;
    }

    @Override
    protected void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        EntityVehicle vehicle = (EntityVehicle) getPlayerRootVehicle();
        assert vehicle != null;

        int xOrigin = screenWidth - STICK_BASE_SIZE - PADDING;
        int yOrigin = screenHeight - PADDING;

        // rudder (yaw input)
        if (vehicle.isAircraft()) {
            yOrigin -= PEDAL_HEIGHT;

            // Render left rudder pedal
            if (vehicle.inputs.yaw < 0) graphics.blit(RUDDER_PEDAL_PUSHED, xOrigin, yOrigin, 0, 0, PEDAL_WIDTH, PEDAL_HEIGHT, PEDAL_WIDTH, PEDAL_HEIGHT);
            else graphics.blit(RUDDER_PEDAL, xOrigin, yOrigin, 0, 0, PEDAL_WIDTH, PEDAL_HEIGHT, PEDAL_WIDTH, PEDAL_HEIGHT);

            // Render right rudder pedal
            if (vehicle.inputs.yaw > 0) graphics.blit(RUDDER_PEDAL_PUSHED, xOrigin + STICK_BASE_SIZE - PEDAL_WIDTH, yOrigin, 0, 0, PEDAL_WIDTH, PEDAL_HEIGHT, PEDAL_WIDTH, PEDAL_HEIGHT);
            else graphics.blit(RUDDER_PEDAL, xOrigin + STICK_BASE_SIZE - PEDAL_WIDTH, yOrigin, 0, 0, PEDAL_WIDTH, PEDAL_HEIGHT, PEDAL_WIDTH, PEDAL_HEIGHT);
        }

        // stick (pitch/roll input)
        graphics.blit(STICK_BASE, xOrigin, yOrigin - STICK_BASE_SIZE, 0, 0, STICK_BASE_SIZE, STICK_BASE_SIZE, STICK_BASE_SIZE, STICK_BASE_SIZE);

        int baseSizeHalf = STICK_BASE_SIZE / 2;
        int knobSizeHalf = STICK_KNOB_SIZE / 2;
        float xinput, yinput = vehicle.inputs.pitch;

        if (vehicle.isAircraft()) {
            xinput = vehicle.inputs.roll;
        } else {
            xinput = vehicle.inputs.yaw;
        }

        // Normalize input to avoid exceeding joystick boundaries
        float inputLength = Mth.sqrt(xinput * xinput + yinput * yinput);
        if (inputLength > 1) {
            xinput = xinput / inputLength;
            yinput = yinput / inputLength;
        }

        // Render stick knob
        graphics.blit(STICK_KNOB,
                xOrigin + baseSizeHalf - knobSizeHalf + (int) (xinput * baseSizeHalf),
                yOrigin - STICK_BASE_SIZE + baseSizeHalf - knobSizeHalf + (int) (yinput * baseSizeHalf),
                0, 0,
                STICK_KNOB_SIZE, STICK_KNOB_SIZE, STICK_KNOB_SIZE, STICK_KNOB_SIZE);
    }


    @Override
    protected @NotNull String componentId() {
        return "dscombat_controls";
    }
}
