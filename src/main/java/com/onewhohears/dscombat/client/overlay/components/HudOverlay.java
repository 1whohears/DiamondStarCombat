package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.util.UtilEntity;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.Objects;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

public class HudOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation HUD = new ResourceLocation(MODID,
            "textures/ui/hud_overlay.png");
    public static final ResourceLocation ATTITUDE_TEXTURE = new ResourceLocation(MODID,
            "textures/ui/hud_attitude.png");

    public static final Color GREEN_ME_SAY_ALONE_RAMP = new Color(0, 255, 0);
    public static final Color RED = new Color(255, 0, 0);
    public static final float START = 0.6f;
    public static final float END = 0.1f;
    public static final float CHANGE_G = (float) GREEN_ME_SAY_ALONE_RAMP.getGreen() / (START - END);
    public static final float CHANGE_R = (float) RED.getRed() / (START - END);

    public static final byte ATTITUDE_TEXTURE_WIDTH = 90;
    public static final short ATTITUDE_TEXTURE_HEIGHT = 294;
    public static final short VERTICAL_BOUNDS_WIDTH = 128;
    public static final byte HORIZONTAL_BOUNDS_U_WIDTH = 79;
    public static final short HORIZONTAL_BOUNDS_V_HEIGHT = 71;
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

    @Override
    protected void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!(getPlayerRootVehicle() instanceof EntityPlane plane)) return;
        if (DSCClientInputs.isCameraFree()) return;

        drawStrings(poseStack, screenWidth, screenHeight, plane);

        RenderSystem.setShaderTexture(0, HUD);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();

        // TODO: vertical bounds change colour w/ vehicle health
        //noinspection SuspiciousNameCombination
        blit(poseStack,
                ((screenWidth - VERTICAL_BOUNDS_WIDTH) / 2), (screenHeight - VERTICAL_BOUNDS_WIDTH) / 2,
                0, 0,
                VERTICAL_BOUNDS_WIDTH, VERTICAL_BOUNDS_WIDTH);

        // PoseStack manipulation is done here to render the crosshair components as the blit method only takes
        // ints for position; making it impossible to center them
        poseStack.pushPose();
        poseStack.translate(((((double) screenWidth) - 9.0) / 2.0), (((double) screenHeight) - 6.0) / 2.0, 0);
        blit(poseStack, 0, 0, 150, 16, 9, 6);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(((((double) screenWidth) - 22.0) / 2.0), ((((double) screenHeight) - 22.0) / 2.0), 0);
        int vOffsetForCircle = plane.radarSystem.isClientLocking() ? 38 : 16; // assuming this was intended 
        blit(poseStack, 0, 0, 128, vOffsetForCircle, 22, 22);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(((((double) screenWidth) - 25.0) / 2.0), ((((double) screenHeight) - 5.0) / 2.0), 0);
        blit(poseStack, 0, 59, 128, 60, 25, 5);
        poseStack.popPose();

        this.drawAttitudeOverlay(poseStack, screenWidth, screenHeight, plane);


        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

    /**
     * It's important to call this first as weird visual fuckshit happens otherwise
     */
    private static void drawStrings(PoseStack poseStack, int screenWidth, int screenHeight, EntityPlane plane) {
        poseStack.pushPose();
        poseStack.translate((((double) screenWidth) / 2.0) + 30.0, ((((double) screenHeight) + ((double) VERTICAL_BOUNDS_WIDTH)) / 2.0) - 28.0, 0);
        poseStack.scale(0.7F, 0.7F, 1);

        drawString(poseStack, getFont(),
                String.format("AOA: %3.1f", plane.getAOA()),
                0, 0,
                0x00ff00);

        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate((((double) screenWidth) / 2.0) - 56.0, ((((double) screenHeight) + ((double) VERTICAL_BOUNDS_WIDTH)) / 2.0) - 28.0, 0);
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

    // FIXME: behaviour near +-90 degrees pitch
    private void drawAttitudeOverlay(PoseStack poseStack, int screenWidth, int screenHeight, EntityPlane plane) {
        poseStack.pushPose();

        RenderSystem.setShaderTexture(0, ATTITUDE_TEXTURE);

        double xOrigin = ((((double) screenWidth) - ((double) HORIZONTAL_BOUNDS_U_WIDTH)) / 2.0);
        double yOrigin = ((((double) screenHeight) - ((double) HORIZONTAL_BOUNDS_V_HEIGHT)) / 2.0) + 1.0;

        poseStack.translate(xOrigin + (((double) HORIZONTAL_BOUNDS_U_WIDTH) - 1.0) / 2.0, yOrigin + ((double) HORIZONTAL_BOUNDS_V_HEIGHT) / 2.0, HORIZONTAL_BOUNDS_BLIT_OFFSET);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(-plane.zRot));

        // TODO: make this look less jarring
        poseStack.translate(-(plane.getYawRate() * 5.6), 0, 0);

        blit(poseStack,
                -HORIZONTAL_BOUNDS_U_WIDTH / 2, -HORIZONTAL_BOUNDS_V_HEIGHT / 2,
                0, HORIZONTAL_BOUNDS_V_OFFSET_0 + (int) (plane.getXRot() * 8 / 5),
                ATTITUDE_TEXTURE_WIDTH, HORIZONTAL_BOUNDS_V_HEIGHT,
                ATTITUDE_TEXTURE_WIDTH, ATTITUDE_TEXTURE_HEIGHT);

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
                ((number * 7) - (number - 1)), 0,
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
