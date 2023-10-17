package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

import static com.onewhohears.dscombat.DSCombatMod.MODID;
import static com.onewhohears.dscombat.client.event.forgebus.ClientInputEvents.MOUSE_MODE;

public class HudOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation HUD = new ResourceLocation(MODID,
            "textures/ui/hud_overlay.png");
    public static final short HUD_TEXTURE_WIDTH = 320;
    public static final short HUD_TEXTURE_HEIGHT = 294;
    public static final byte VERTICAL_BOUNDS_WIDTH = 125;
    public static final byte VERTICAL_BOUNDS_HEIGHT = 124;
    public static final byte VERTICAL_BOUNDS_U_OFFSET = 78;
    public static final byte HORIZONTAL_BOUNDS_U_WIDTH = 67;
    public static final byte HORIZONTAL_BOUNDS_U_WIDTH_WIDE = 78;
    public static final short HORIZONTAL_BOUNDS_V_HEIGHT = 71;
    public static final byte HORIZONTAL_BOUNDS_U_OFFSET = 1;
    public static final short HORIZONTAL_BOUNDS_V_OFFSET_0 = 112;
    public static final byte NUMBERS_UV_WIDTH = 6;
    public static final byte NUMBERS_UV_HEIGHT = 8;

    private static HudOverlay INSTANCE;

    public static void renderIfAllowed(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (Objects.isNull(INSTANCE)) INSTANCE = new HudOverlay();
        INSTANCE.render(poseStack, screenWidth, screenHeight);
    }

    private HudOverlay() {}
    @Override
    protected void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!(getPlayerVehicle() instanceof EntityPlane plane)) return;
        if (!MOUSE_MODE || plane.onlyFreeLook()) return;

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

    private static void drawAttitudeOverlay(PoseStack poseStack, int screenWidth, int screenHeight, EntityPlane plane) {
        poseStack.pushPose();

        blit(poseStack,
                ((screenWidth - HORIZONTAL_BOUNDS_U_WIDTH) / 2) + 1, ((screenHeight - HORIZONTAL_BOUNDS_V_HEIGHT) / 2) + 1,
                HORIZONTAL_BOUNDS_U_OFFSET, HORIZONTAL_BOUNDS_V_OFFSET_0 + (int) (plane.getXRot() * (5/3)),
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
}
