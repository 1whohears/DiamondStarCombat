package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.obj.ObjVehicleModel;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.EntityWindTunnel;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.util.UtilParse;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import static com.onewhohears.dscombat.util.UtilRender.*;

public class RendererWindTunnel extends EntityRenderer<EntityWindTunnel> {

    public static final ResourceLocation ARROW_TXT = new ResourceLocation(DSCombatMod.MODID,
            "textures/misc/arrow.png");
    private static final RenderType ARROW = RenderType.text(ARROW_TXT);
    private static final float ARROW_LENGTH_SCALE = 10;

    public RendererWindTunnel(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(@NotNull EntityWindTunnel entity, float yaw, float partialTicks, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource buffer, int packedLight) {
        if (entity.getSimulatedVehicle().getClientStatsHolder() == null) return;
        poseStack.pushPose();
        ObjVehicleModel<EntityVehicle> model = entity.getSimulatedVehicle().getClientStatsHolder().get().getModel();
        poseStack.translate(0, 4, 0);
        drawGlobalAxis(partialTicks, poseStack, buffer, packedLight);
        model.render(entity.getSimulatedVehicle(), poseStack, buffer, packedLight, partialTicks);
        ObjEntityModels.ModelOverrides overrides = model.getModelOverride();
        poseStack.translate(-overrides.translate.x(), -overrides.translate.y(), -overrides.translate.z());
        drawForces(entity, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }

    private void drawForces(@NotNull EntityWindTunnel entity, float partialTicks, @NotNull PoseStack poseStack,
                            @NotNull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        EntityVehicle vehicle = entity.getSimulatedVehicle();
        Quaternion qi = vehicle.getQBySide();
        qi.conj();
        poseStack.mulPose(qi);
        float maxForceMag = (float) entity.weightForce.length();
        // draw sum forces
        drawForce(poseStack, buffer, packedLight, WHITE, maxForceMag, Vec3.ZERO, vehicle.getForces());
        // draw gravity
        drawForce(poseStack, buffer, packedLight, BLACK, maxForceMag, Vec3.ZERO, entity.weightForce);
        // draw thrust
        drawForce(poseStack, buffer, packedLight, BLUE, maxForceMag, Vec3.ZERO, entity.thrustForce);
        // draw drag
        drawForce(poseStack, buffer, packedLight, RED, maxForceMag, Vec3.ZERO, entity.dragForce);
        // draw forces from surfaces

        poseStack.popPose();
    }

    private void drawForce(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight,
                           int[] color, float maxForceMag, Vec3 pos, Vec3 force) {
        drawForce(poseStack, buffer, packedLight, color, pos, force.normalize(),
                (float)force.length()/maxForceMag*ARROW_LENGTH_SCALE);
    }

    private void drawForce(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight,
                           int[] color, Vec3 pos, Vec3 dir, float mag) {
        poseStack.pushPose();
        poseStack.translate(pos.x, pos.y, pos.z);
        float y = UtilAngles.getYaw(dir);
        float x = UtilAngles.getPitch(dir);
        poseStack.mulPose(Vector3f.YN.rotationDegrees(y+180));
        poseStack.mulPose(Vector3f.XN.rotationDegrees(x-90));
        poseStack.translate(0, -mag*0.5, 0);

        poseStack.scale(1, mag, 1);
        for (int i = 0; i < 4; ++i) {
            poseStack.mulPose(Vector3f.YN.rotationDegrees(90*i));
            drawTextureCentered(ARROW, poseStack.last().pose(), buffer, packedLight, 0, color);
        }
        poseStack.popPose();
    }

    private void drawGlobalAxis(float partialTicks, @NotNull PoseStack poseStack,
                                @NotNull MultiBufferSource buffer, int packedLight) {
        VertexConsumer buff = buffer.getBuffer(RenderType.lines());
        Vector3f O = new Vector3f();
        Vector3f X = new Vector3f(8, 0, 0);
        Vector3f Y = new Vector3f(0, 8, 0);
        Vector3f Z = new Vector3f(0, 0, 8);
        Matrix4f m4 = poseStack.last().pose();
        Matrix3f m3 = poseStack.last().normal();
        drawLine(O, X, buff, m4, m3, RED);
        drawLine(O, Y, buff, m4, m3, GREEN);
        drawLine(O, Z, buff, m4, m3, BLUE);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityWindTunnel entity) {
        return null;
    }
}
