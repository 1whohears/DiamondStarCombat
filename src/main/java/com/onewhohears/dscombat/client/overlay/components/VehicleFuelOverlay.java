package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.PEDAL_HEIGHT;
import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.STICK_BASE_SIZE;

// TODO: redo texture
public class VehicleFuelOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation FUEL_GAUGE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/fuel_guage.png");
    public static final ResourceLocation FUEL_GAUGE_ARROW = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/fuel_guage_arrow.png");
    public static final int FUEL_GAUGE_HEIGHT = 40, FUEL_GAUGE_WIDTH = 60;
    public static final int FUEL_ARROW_HEIGHT = 7, FUEL_ARROW_WIDTH = 24;

    @Override
    protected boolean shouldRender(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        return getPlayerRootVehicle() instanceof EntityVehicle;
    }

    @Override
    protected void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        EntityVehicle vehicle = (EntityVehicle) getPlayerRootVehicle();
        assert vehicle != null;

        int xOrigin = screenWidth - PADDING - FUEL_GAUGE_WIDTH;
        int yOrigin = screenHeight - STICK_BASE_SIZE - PADDING * 2 - FUEL_GAUGE_HEIGHT;
        if (vehicle.isAircraft()) yOrigin -= PEDAL_HEIGHT;

        graphics.blit(FUEL_GAUGE,
                xOrigin, yOrigin,
                0, 0,
                FUEL_GAUGE_WIDTH, FUEL_GAUGE_HEIGHT,
                FUEL_GAUGE_WIDTH, FUEL_GAUGE_HEIGHT);

        float max = vehicle.getMaxFuel(), fuelPercent = 0;
        if (max != 0) fuelPercent = vehicle.getCurrentFuel() / max;

        float angleDegrees = 160F * fuelPercent + 10F;
        Quaternionf rotation = new Quaternionf().rotateZ((float) Math.toRadians(angleDegrees));

        graphics.pose().pushPose();
        graphics.pose().translate(xOrigin + (double) FUEL_GAUGE_WIDTH / 2, yOrigin + 24, 0);

        graphics.pose().mulPoseMatrix(new Matrix4f().rotate(rotation));

        // Bind and draw the fuel gauge arrow
        graphics.blit(FUEL_GAUGE_ARROW,
                -FUEL_ARROW_WIDTH + 5, -FUEL_ARROW_HEIGHT / 2,
                0, 0,
                FUEL_ARROW_WIDTH, FUEL_ARROW_HEIGHT,
                FUEL_ARROW_WIDTH, FUEL_ARROW_HEIGHT);

        graphics.pose().popPose();
    }




    @Override
    protected @NotNull String componentId() {
        return "dscombat_fuel";
    }
}
