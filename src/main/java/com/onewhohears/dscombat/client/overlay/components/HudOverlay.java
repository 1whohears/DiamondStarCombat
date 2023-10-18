package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.util.UtilEntity;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.Objects;

import static com.onewhohears.dscombat.DSCombatMod.MODID;
import static com.onewhohears.dscombat.client.event.forgebus.ClientInputEvents.MOUSE_MODE;

public class HudOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation HUD = new ResourceLocation(MODID,
            "textures/ui/hud_overlay.png");

    public static final Color GREEN_ME_SAY_ALONE_RAMP = new Color(0, 255, 0);
    public static final Color RED = new Color(255, 0, 0);
    public static final float START = 0.6f;
    public static final float END = 0.1f;
    public static final float CHANGE_G = (float) GREEN_ME_SAY_ALONE_RAMP.getGreen() / (START - END);
    public static final float CHANGE_R = (float) RED.getRed() / (START - END);

    public static final short HUD_TEXTURE_WIDTH = 320;
    public static final short HUD_TEXTURE_HEIGHT = 294;
    public static final byte VERTICAL_BOUNDS_WIDTH = 125;
    public static final byte VERTICAL_BOUNDS_HEIGHT = 124;
    public static final byte VERTICAL_BOUNDS_U_OFFSET = 78;
    public static final byte HORIZONTAL_BOUNDS_U_WIDTH = 67;
    public static final byte HORIZONTAL_BOUNDS_U_WIDTH_WIDE = 77;
    public static final short HORIZONTAL_BOUNDS_V_HEIGHT = 71;
    public static final byte HORIZONTAL_BOUNDS_U_OFFSET = 1;
    public static final short HORIZONTAL_BOUNDS_V_OFFSET_0 = 112;
    public static final byte NUMBERS_UV_WIDTH = 6;
    public static final byte NUMBERS_UV_HEIGHT = 8;
    public static final byte HORIZONTAL_BOUNDS_BLIT_OFFSET = -2;

    private static HudOverlay INSTANCE;

    public static void renderIfAllowed(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (Objects.isNull(INSTANCE)) INSTANCE = new HudOverlay();
        INSTANCE.render(poseStack, screenWidth, screenHeight);
    }

    private HudOverlay() {}

    // FIXME: switching b/w planes while mouse mode is on causes it to break
    // Suggested fix is to tick MOUSE_MODE = false whenever the player is not in a vehicle.
    @Override
    protected void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!(getPlayerVehicle() instanceof EntityPlane plane)) return;
        if (!MOUSE_MODE || plane.onlyFreeLook()) return;

        drawStrings(poseStack, screenWidth, screenHeight, plane);

        RenderSystem.setShaderTexture(0, HUD);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();

        blit(poseStack,
                ((screenWidth - VERTICAL_BOUNDS_WIDTH) / 2), (screenHeight - VERTICAL_BOUNDS_HEIGHT) / 2,
                VERTICAL_BOUNDS_U_OFFSET, 0,
                VERTICAL_BOUNDS_WIDTH, VERTICAL_BOUNDS_HEIGHT,
                HUD_TEXTURE_WIDTH, HUD_TEXTURE_HEIGHT);

        drawAttitudeOverlay(poseStack, screenWidth, screenHeight, plane);


        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

    /**
     * It's important to call this first as weird visual fuckshit happens otherwise
     */
    private static void drawStrings(PoseStack poseStack, int screenWidth, int screenHeight, EntityPlane plane) {
        poseStack.pushPose();
        poseStack.translate(((double) screenWidth / 2) + 23, ((double) (screenHeight + VERTICAL_BOUNDS_HEIGHT) / 2) - 25, 0);
        poseStack.scale(0.7F, 0.7F, 1);

        drawString(poseStack, getFont(),
                String.format("AOA: %3.1f", plane.getAOA()),
                0, 0,
                0x00ff00);

        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(((double) screenWidth / 2) -50, ((double) (screenHeight + VERTICAL_BOUNDS_HEIGHT) / 2) - 25, 0);
        poseStack.scale(0.7F, 0.7F, 1);

        drawString(poseStack, getFont(),
                "m/s: " + String.format("%3.1f", plane.getDeltaMovement().length() * 20),
                0, 0,
                0x00ff00);

        drawString(poseStack, getFont(),
                "A: " + UtilEntity.getDistFromSeaLevel(plane),
                0, 10,
                0x00ff00);

        drawString(poseStack, getFont(),
                "[" + plane.getBlockX() + "," + plane.getBlockY() + "," + plane.getBlockZ() + "]",
                0, 20, 0x00ff00);

        poseStack.popPose();
    }

    // FIXME: different resolutions/screen sizes will cause these values to not line up properly (usually by one or two pixels)
    // FIXME: behaviour near +-90 degrees pitch
    private static void drawAttitudeOverlay(PoseStack poseStack, int screenWidth, int screenHeight, EntityPlane plane) {
        poseStack.pushPose();

        int xOrigin = ((screenWidth - HORIZONTAL_BOUNDS_U_WIDTH) / 2) + 1;
        int yOrigin = ((screenHeight - HORIZONTAL_BOUNDS_V_HEIGHT) / 2) + 1;

        poseStack.translate(xOrigin + (double) HORIZONTAL_BOUNDS_U_WIDTH / 2, yOrigin + (double) HORIZONTAL_BOUNDS_V_HEIGHT / 2, HORIZONTAL_BOUNDS_BLIT_OFFSET);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(-plane.zRot));

        // TODO: make this look less jarring
        poseStack.translate(-(plane.getYawRate() * 5.6), 0, 0);

        blit(poseStack,
                -HORIZONTAL_BOUNDS_U_WIDTH / 2, -HORIZONTAL_BOUNDS_V_HEIGHT / 2,
                HORIZONTAL_BOUNDS_U_OFFSET, HORIZONTAL_BOUNDS_V_OFFSET_0 + (int) (plane.getXRot() * 8 / 5),
                HORIZONTAL_BOUNDS_U_WIDTH_WIDE, HORIZONTAL_BOUNDS_V_HEIGHT,
                HUD_TEXTURE_WIDTH, HUD_TEXTURE_HEIGHT);

        poseStack.popPose();
    }

    /**
     * Draws a 1-digit sexy HUD display number.
     * @param number the number drawn from 0 to 9 inclusive
     */
    private void drawNumber(int number, PoseStack poseStack, int xOrigin, int yOrigin) throws IllegalArgumentException {
        if (number > 9 || number < 0) throw new IllegalArgumentException("Argument MUST be between 0 to 9 inclusive");
        blit(poseStack,
                xOrigin, yOrigin,
                ((number * 7) - (number - 1)) + HORIZONTAL_BOUNDS_U_OFFSET, 0,
                NUMBERS_UV_WIDTH, NUMBERS_UV_HEIGHT);
    }

    private static int getHealthColor(float health, float max) {
        float healthPercent = health / max;
        if (healthPercent >= START) return GREEN_ME_SAY_ALONE_RAMP.getRGB();
        if (healthPercent < START && healthPercent > END) {
            return new Color(
                    (int)(CHANGE_R *(START -healthPercent)),
                    (int)(CHANGE_G *(healthPercent- END)),
                    0).getRGB();
        }
        return RED.getRGB();
    }
}
