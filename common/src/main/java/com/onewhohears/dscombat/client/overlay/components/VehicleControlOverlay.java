package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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
    protected boolean shouldRender(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        if (com.onewhohears.dscombat.Config.CLIENT.enableModernHUD.get() &&
                !com.onewhohears.dscombat.Config.CLIENT.showControlsInModernHUD.get()) return false;
        return getPlayerRootVehicle() instanceof EntityVehicle;
    }

    @Override
    protected void render(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        EntityVehicle vehicle = (EntityVehicle) getPlayerRootVehicle();
        assert vehicle != null;

        int xOrigin = screenWidth - STICK_BASE_SIZE - PADDING;
        int yOrigin = screenHeight - PADDING;

        // rudder (yaw input)
        if (vehicle.isAircraft()) {
            yOrigin -= PEDAL_HEIGHT;
            ResourceLocation texture;
            if (vehicle.inputs.yaw < 0) texture = RUDDER_PEDAL_PUSHED;
            else texture = RUDDER_PEDAL;
            graphics.blit(texture,
                    xOrigin, yOrigin,
                    0, 0,
                    PEDAL_WIDTH, PEDAL_HEIGHT,
                    PEDAL_WIDTH, PEDAL_HEIGHT);
            if (vehicle.inputs.yaw > 0) texture = RUDDER_PEDAL_PUSHED;
            else texture = RUDDER_PEDAL;
            graphics.blit(texture,
                    xOrigin + STICK_BASE_SIZE - PEDAL_WIDTH, yOrigin,
                    0, 0,
                    PEDAL_WIDTH, PEDAL_HEIGHT,
                    PEDAL_WIDTH, PEDAL_HEIGHT);
        }

        // stick (pitch roll input)
        RenderSystem.setShaderTexture(0, STICK_BASE);
        yOrigin -= STICK_BASE_SIZE;
        graphics.blit(STICK_BASE,
                xOrigin, yOrigin,
                0, 0,
                STICK_BASE_SIZE, STICK_BASE_SIZE,
                STICK_BASE_SIZE, STICK_BASE_SIZE);
        RenderSystem.setShaderTexture(0, STICK_KNOB);

        int baseSizeHalf = STICK_BASE_SIZE / 2, knobSizeHalf = STICK_KNOB_SIZE / 2;
        float xinput, yinput = vehicle.inputs.pitch;

        if (vehicle.isAircraft()) xinput = vehicle.inputs.roll;
        else xinput = vehicle.inputs.yaw;

        // PIE HAT GORE-US
        float inputLength = Mth.sqrt(xinput * xinput + yinput * yinput);
        if (inputLength > 1) {
            xinput = xinput / inputLength;
            yinput = yinput / inputLength;
        }

        graphics.blit(STICK_KNOB,
                xOrigin + baseSizeHalf - knobSizeHalf + (int) (xinput * baseSizeHalf),
                yOrigin + baseSizeHalf - knobSizeHalf + (int) (yinput * baseSizeHalf),
                0, 0,
                STICK_KNOB_SIZE, STICK_KNOB_SIZE,
                STICK_KNOB_SIZE, STICK_KNOB_SIZE);
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_controls";
    }
}
