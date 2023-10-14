package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import net.minecraft.resources.ResourceLocation;

public class TurnCoordinatorOverlay extends VehicleOverlayComponent {
    private static final ResourceLocation TURN_COORD_BASE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/turn_coord_base.png");
    private static final ResourceLocation TURN_COORD_BALL = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/turn_coord_ball.png");
    private static final ResourceLocation TURN_COORD_NEEDLE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/turn_coord_needle.png");

    public static final int TURN_COORD_SIZE = 80;

    @Override
    public void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!shouldRender()) return;
        EntityPlane plane = (EntityPlane) getPlayerVehicle();

        // TODO: make magic numbers 60 & 10 public in ThrottleVehicleOverlay
        int xOrigin = screenWidth - TURN_COORD_SIZE - PADDING * 3 - 60 - 10;
        int yOrigin = screenHeight - PADDING - TURN_COORD_SIZE;

        RenderSystem.setShaderTexture(0, TURN_COORD_BASE);
        blit(poseStack,
                xOrigin, yOrigin,
                0, 0,
                TURN_COORD_SIZE, TURN_COORD_SIZE,
                TURN_COORD_SIZE, TURN_COORD_SIZE);

        RenderSystem.setShaderTexture(0, TURN_COORD_BALL);
        // FIXME: #getCentripetalForce can return a NullPointerException?
        int xTranslation = (int) ((plane.getCentripetalForce() - plane.getCentrifugalForce()) * 25);
        blit(poseStack,
                xOrigin + xTranslation, yOrigin,
                0, 0,
                TURN_COORD_SIZE, TURN_COORD_SIZE,
                TURN_COORD_SIZE, TURN_COORD_SIZE);

        RenderSystem.setShaderTexture(0, TURN_COORD_NEEDLE);
        poseStack.pushPose();
        poseStack.translate(xOrigin + (double) TURN_COORD_SIZE / 2, yOrigin + (double) TURN_COORD_SIZE / 2, 0);
        float yawRate = plane.getYawRate() * 20 / 40 * 30; // yawRate /  (indicator rate) * (indicator angle)
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(yawRate));
        blit(poseStack,
                -TURN_COORD_SIZE / 2, -TURN_COORD_SIZE / 2,
                0, 0,
                TURN_COORD_SIZE, TURN_COORD_SIZE,
                TURN_COORD_SIZE, TURN_COORD_SIZE);
        poseStack.popPose();
    }

    @Override
    public boolean shouldRender() {
        return getPlayerVehicle() instanceof EntityPlane;
    }
}
