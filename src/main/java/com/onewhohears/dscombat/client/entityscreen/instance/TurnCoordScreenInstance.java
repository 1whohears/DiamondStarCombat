package com.onewhohears.dscombat.client.entityscreen.instance;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.vehicle.EntityPlane;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class TurnCoordScreenInstance extends SpinMeterScreenInstance {

    public static final ResourceLocation TURN_COORD_BASE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/turn_coord_base.png");
    public static final ResourceLocation TURN_COORD_BALL = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/turn_coord_ball.png");
    public static final ResourceLocation TURN_COORD_NEEDLE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/turn_coord_needle.png");

    private static final float MAX_BALL_MOVE = 0.23f;

    protected final RenderType ballRenderType;

    public TurnCoordScreenInstance(int id) {
        super(id, TURN_COORD_BASE, TURN_COORD_NEEDLE);
        ballRenderType = RenderType.text(TURN_COORD_BALL);
    }

    @Override
    public void draw(Entity entity, PoseStack poseStack, MultiBufferSource buffer,
                     float partialTicks, int packedLight, float worldWidth, float worldHeight) {
        super.draw(entity, poseStack, buffer, partialTicks, packedLight, worldWidth, worldHeight);
        poseStack.pushPose();
        EntityPlane plane = (EntityPlane)entity;
        float xTranslation = (int) ((plane.getCentripetalForce() - plane.getCentrifugalForce()) * 0.1);
        if (Math.abs(xTranslation) > MAX_BALL_MOVE) xTranslation = MAX_BALL_MOVE * Math.signum(xTranslation);
        poseStack.translate(xTranslation, 0, 0);
        Matrix4f matrix4f = poseStack.last().pose();
        drawTextureCentered(ballRenderType, matrix4f, buffer, packedLight, -0.001f);
        poseStack.popPose();
    }

    @Override
    protected float getAngleDegrees(Entity entity) {
        return ((EntityVehicle)entity).getYawRate() * 15;
    }
}
