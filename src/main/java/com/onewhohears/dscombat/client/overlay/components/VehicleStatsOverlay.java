package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.NotNull;

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

    @Override
    protected boolean shouldRender(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        if (!(getPlayerRootVehicle() instanceof EntityVehicle)) return false;
        return !DSCClientInputs.isCameraLockedForward();
    }

    @Override
    protected void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        EntityVehicle vehicle = (EntityVehicle) getPlayerRootVehicle();
        assert vehicle != null;

        int xOrigin = screenWidth - STICK_BASE_SIZE - PADDING;
        int yOrigin = screenHeight - STICK_BASE_SIZE - PADDING * 3 - FUEL_GAUGE_HEIGHT - 10;

        if (vehicle.isAircraft()) yOrigin -= PEDAL_HEIGHT;

        // Display speed (m/s)
        graphics.drawString(FONT,
                "m/s: " + String.format("%3.1f", vehicle.getDeltaMovement().length() * 20),
                xOrigin, yOrigin,
                0x00ff00, false);

        // Display altitude
        graphics.drawString(FONT,
                "A: " + vehicle.getAltitude(),
                xOrigin, yOrigin - 10,
                0x00ff00, false);

        // Display health
        float health = vehicle.getHealth();
        float maxHealth = vehicle.getMaxHealth();
        graphics.drawString(FONT,
                "H: " + (int) health + "/" + (int) maxHealth,
                xOrigin, yOrigin - 20,
                getHealthColor(health, maxHealth), false);

        // Display armor
        float armor = vehicle.getArmor();
        float maxArmor = vehicle.getMaxTotalArmor();
        graphics.drawString(FONT,
                "S: " + (int) armor + "/" + (int) maxArmor,
                xOrigin, yOrigin - 30,
                getHealthColor(armor, maxArmor), false);

        // Display vehicle's block coordinates
        graphics.drawCenteredString(FONT,
                "[" + vehicle.getBlockX() + "," + vehicle.getBlockY() + "," + vehicle.getBlockZ() + "]",
                screenWidth / 2, 0, 0x00ff00);
    }




    @Override
    protected @NotNull String componentId() {
        return "dscombat_stats";
    }
}
