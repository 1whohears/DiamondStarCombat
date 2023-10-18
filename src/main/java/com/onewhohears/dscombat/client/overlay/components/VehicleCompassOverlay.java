package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Objects;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

public class VehicleCompassOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation COMPASS = new ResourceLocation(MODID,
            "textures/ui/hud_compass.png");

    public static final short COMPASS_TEXTURE_WIDTH = 720;
    public static final byte COMPASS_TEXTURE_HEIGHT = 16;
    public static final short COMPASS_U_WIDTH = 161;
    public static final byte COMPASS_CORRECTIONAL_OFFSET = 30;

    private static VehicleCompassOverlay INSTANCE;

    public static void renderIfAllowed(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (Objects.isNull(INSTANCE)) INSTANCE = new VehicleCompassOverlay();
        INSTANCE.render(poseStack, screenWidth, screenHeight);
    }

    private VehicleCompassOverlay() {}

    @Override
    protected void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!(getPlayerVehicle() instanceof EntityVehicle)) return;

        // HEADING
        int y = 10, color = 0xe6e600;
        float heading = (Mth.wrapDegrees(Minecraft.getInstance().player.getYRot()));
        if (heading < 0) heading += 360f;
        String stringHeading = !Objects.equals(textByHeading((int) heading), "")
                ? textByHeading((int) heading)
                : String.valueOf((int) heading);

        drawCenteredString(poseStack, getFont(), stringHeading,
                screenWidth / 2, y + 40, color);

        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, COMPASS);

        poseStack.pushPose();
        poseStack.translate(((double) (screenWidth - COMPASS_U_WIDTH) / 2) - 1, 30, 0);

        blit(poseStack,
                0, 0,
                (Minecraft.getInstance().player.getYRot() * 2) - COMPASS_CORRECTIONAL_OFFSET, 0,
                COMPASS_U_WIDTH, COMPASS_TEXTURE_HEIGHT,
                COMPASS_TEXTURE_WIDTH, COMPASS_TEXTURE_HEIGHT);

        poseStack.popPose();
    }

    private static String textByHeading(int h) {
        return switch (h) {
            case 0 -> "S";
            case 180 -> "N";
            case 90 -> "W";
            case 270 -> "E";
            case 45 -> "SW";
            case 135 -> "NW";
            case 225 -> "NE";
            case 315 -> "SE";
            default -> "";
        };
    }
}
