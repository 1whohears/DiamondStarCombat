package com.onewhohears.dscombat.client.entityscreen.instance;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.Vec3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import static com.onewhohears.dscombat.util.UtilRender.drawTextureCentered;

public class AttitudeScreenInstance extends EntityScreenInstance {

    public static final ResourceLocation ATTITUDE_BASE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/attitude_base.png");
    public static final ResourceLocation ATTITUDE_FRAME = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/attitude_frame.png");
    public static final ResourceLocation ATTITUDE_MID = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/attitude_mid.png");
    public static final ResourceLocation ATTITUDE_FRONT = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/attitude_front.png");

    protected final RenderType spinRenderType, frameRenderType, frontRenderType;

    public AttitudeScreenInstance(int id) {
        super(id, ATTITUDE_BASE);
        spinRenderType = RenderType.text(ATTITUDE_MID);
        frameRenderType = RenderType.text(ATTITUDE_FRAME);
        frontRenderType = RenderType.text(ATTITUDE_FRONT);
    }

    @Override
    public void draw(Entity entity, PoseStack poseStack, MultiBufferSource buffer,
                     float partialTicks, int packedLight, float worldWidth, float worldHeight) {
        super.draw(entity, poseStack, buffer, partialTicks, packedLight, worldWidth, worldHeight);
        poseStack.pushPose();
        poseStack.mulPose(Vec3f.ZP.rotationDegrees(-((EntityVehicle)entity).zRot).convert());
        float pitchPointY = Mth.clamp(-entity.getXRot(), -30, 30) * 0.0055f;
        poseStack.translate(0, pitchPointY, 0);
        Mat4f matrix4f = Mat4f.from(poseStack.last().pose());
        drawTextureCentered(spinRenderType, matrix4f, buffer, packedLight, -0.001f);
        poseStack.popPose();
        Mat4f matrix4f2 = Mat4f.from(poseStack.last().pose());
        drawTextureCentered(frameRenderType, matrix4f2, buffer, packedLight, -0.002f);
        drawTextureCentered(frontRenderType, matrix4f2, buffer, packedLight, -0.003f);
    }
}
