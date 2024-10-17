package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

public class VehicleCompassOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation COMPASS = new ResourceLocation(MODID,
            "textures/ui/hud_compass.png");

    public static final short COMPASS_TEXTURE_WIDTH = 720;
    public static final byte COMPASS_TEXTURE_HEIGHT = 16;
    public static final short COMPASS_U_WIDTH = 161;
    public static final byte COMPASS_CORRECTIONAL_OFFSET = 30;

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

    @Override
    protected boolean shouldRender(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        return getPlayerRootVehicle() instanceof EntityVehicle;
    }

    @Override
    protected void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        // HEADING
        int y = 10, color = 0xe6e600;
        assert Minecraft.getInstance().player != null;

        // Get player's yaw (heading) and wrap it to [0, 360] degrees
        float heading = Mth.wrapDegrees(Minecraft.getInstance().player.getYRot());
        if (heading < 0) heading += 360f;

        // Get textual representation of the heading or default to the angle
        String stringHeading = !Objects.equals(textByHeading((int) heading), "")
                ? textByHeading((int) heading)
                : String.valueOf((int) heading);

        // Draw the heading text in the center of the screen
        graphics.drawCenteredString(FONT, stringHeading, screenWidth / 2, y + 40, color);

        // Enable blending for transparency
        RenderSystem.enableBlend();

        // Bind the compass texture
        graphics.blit(COMPASS, 0, 0, 0, 0, COMPASS_U_WIDTH, COMPASS_TEXTURE_HEIGHT);

        // Push the current pose (for transformation)
        graphics.pose().pushPose();

        // Translate to the correct position for compass rendering
        graphics.pose().translate((screenWidth - COMPASS_U_WIDTH) / 2.0 - 1, 30, 0);

        // Draw the compass using the player's yaw and correcting its offset
        graphics.blit(COMPASS,
                0, 0,
                (Minecraft.getInstance().player.getYRot() * 2) - COMPASS_CORRECTIONAL_OFFSET, 0,
                COMPASS_U_WIDTH, COMPASS_TEXTURE_HEIGHT,
                COMPASS_TEXTURE_WIDTH, COMPASS_TEXTURE_HEIGHT);

        // Pop the pose to revert to previous state
        graphics.pose().popPose();
    }





    @Override
    protected @NotNull String componentId() {
        return "dscombat_compass";
    }
}
