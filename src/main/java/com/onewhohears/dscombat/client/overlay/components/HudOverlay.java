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
    public static final byte VERTICAL_BOUNDS_WIDTH = 125;
    public static final byte VERTICAL_BOUNDS_HEIGHT = 124;
    public static final byte HORIZONTAL_BOUNDS_UV_HEIGHT = 42;
    public static final byte HORIZONTAL_BOUNDS_U_OFFSET = 69; // nice
    public static final byte HORIZONTAL_BOUNDS_V_OFFSET_DEFAULT = 41;
    public static final byte HORIZONTAL_BOUNDS_WIDTH_NO_NUMBERS = 37;
    public static final byte NUMBERS_U_OFFSET = 105;
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
        RenderSystem.enableBlend();

        blit(poseStack,
                ((screenWidth - VERTICAL_BOUNDS_WIDTH) / 2), (screenHeight - VERTICAL_BOUNDS_HEIGHT) / 2,
                0, 0,
                VERTICAL_BOUNDS_WIDTH, VERTICAL_BOUNDS_HEIGHT);

        // TODO: PoseStack manipulation to scale the entire thing down may be necessary for more fine movement
        /* b
        blit(poseStack,
                (screenWidth - HORIZONTAL_BOUNDS_WIDTH_NO_NUMBERS) / 2, ((screenHeight - VERTICAL_BOUNDS_HEIGHT) / 2) + HORIZONTAL_BOUNDS_V_OFFSET_DEFAULT - 1,
                HORIZONTAL_BOUNDS_U_OFFSET, HORIZONTAL_BOUNDS_V_OFFSET_DEFAULT + (int) plane.getXRot(),
                HORIZONTAL_BOUNDS_WIDTH_NO_NUMBERS, HORIZONTAL_BOUNDS_UV_HEIGHT);
        */

        RenderSystem.disableBlend();
    }

    /**
     * Draws a 1-digit sexy HUD display number.
     * @param number the number drawn from 0 to 9 inclusive
     */
    private void drawNumber(int number, PoseStack poseStack, int xOrigin, int yOrigin) throws IllegalArgumentException {
        if (number > 9 || number < 0) throw new IllegalArgumentException("Argument MUST be between 0 to 9 inclusive");
        blit(poseStack,
                xOrigin, yOrigin,
                ((number * 7) - (number - 1)) + NUMBERS_U_OFFSET, 0,
                NUMBERS_UV_WIDTH, NUMBERS_UV_HEIGHT);
    }
}
