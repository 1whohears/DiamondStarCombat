package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.vehicle.EntityHelicopter;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Simple helicopter status overlay showing rotor RPM and IGE/ETL/VRS state.
 */
public class HeliStatusOverlay extends VehicleOverlayComponent {

    private static final int TEXT_COLOR = new Color(0x00FF00).getRGB();
    private static final int WARN_COLOR = new Color(0xFFAA00).getRGB();
    private static final int DANGER_COLOR = new Color(0xFF3333).getRGB();
    private static final int OFF_COLOR = new Color(0x888888).getRGB();

    @Override
    protected boolean shouldRender(Gui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        if (Config.CLIENT.enableModernHUD.get()) return false;
        return getPlayerRootVehicle() instanceof EntityHelicopter;
    }

    @Override
    protected void render(Gui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        EntityHelicopter heli = (EntityHelicopter) getPlayerRootVehicle();
        assert heli != null;

        // Position: bottom-left corner above keybinds
        int x = 6;
        int y = screenHeight - 70;

        // Rotor RPM (as percent of rotorPower)
        double rpm = Math.max(0.0, Math.min(1.0, heli.getRotorPower())) * 100.0;
        drawString(poseStack, FONT, String.format("RPM: %3.0f%%", rpm), x, y, TEXT_COLOR);
        y += 10;

        // Compute derived states similar to physics logic
        // AGL
        double hMax = Config.SERVER.groundEffectMaxHeight.get();
        boolean ige = false;
        if (Config.SERVER.enableGroundEffect.get() && hMax > 0) {
            int limit = (int)Math.ceil(hMax) + 1;
            int aglBlocks = UtilVehicleEntity.getDistFromGround(heli, limit, true);
            ige = aglBlocks < hMax;
        }
        drawString(poseStack, FONT, "IGE: " + (ige ? "ON" : "OFF"), x, y, ige ? TEXT_COLOR : OFF_COLOR);
        y += 10;

        // ETL
        double speedXZ = heli.getDeltaMovement().horizontalDistance();
        double etlFull = Math.max(1e-6, Config.SERVER.translationalLiftFullSpeed.get());
        double etlFactor = Math.min(1.0, speedXZ / etlFull);
        boolean etl = Config.SERVER.enableTranslationalLift.get() && etlFactor > 0.05;
        drawString(poseStack, FONT, "ETL: " + (etl ? String.format("%d%%", (int)(etlFactor*100)) : "OFF"), x, y, etl ? WARN_COLOR : OFF_COLOR);
        y += 10;

        // VRS
        double vy = heli.getDeltaMovement().y;
        double descent = vy < 0 ? -vy : 0.0;
        double vrsTrig = Config.SERVER.vrsDescentTrigger.get();
        double vrsMaxSpeed = Config.SERVER.vrsHorizMaxSpeed.get();
        boolean vrs = Config.SERVER.enableVRS.get() && descent > vrsTrig && speedXZ < vrsMaxSpeed;
        drawString(poseStack, FONT, "VRS: " + (vrs ? "ENGAGED" : "OK"), x, y, vrs ? DANGER_COLOR : TEXT_COLOR);
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_heli_status";
    }
}
