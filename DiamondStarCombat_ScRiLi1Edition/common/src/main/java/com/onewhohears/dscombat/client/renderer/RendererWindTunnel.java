package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.onewholibs.util.math.Mat3f;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.Vec3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.obj.ObjVehicleModel;
import com.onewhohears.dscombat.data.vehicle.physics.PhysicsComponentInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.wind_tunnel.EntityWindTunnel;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.util.math.UtilAngles;
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
        poseStack.translate(0, 4, 0);
        drawGlobalAxis(partialTicks, poseStack, buffer, packedLight);
        if (!entity.getHideModel()) {
            ObjVehicleModel<EntityVehicle> model = entity.getSimulatedVehicle().getClientStatsHolder().get().getModel();
            model.render(entity.getSimulatedVehicle(), poseStack, buffer, packedLight, partialTicks);
            ObjEntityModels.ModelOverrides overrides = model.getModelOverride();
            poseStack.translate(-overrides.translate.x(), -overrides.translate.y(), -overrides.translate.z());
        } else {
            poseStack.mulPose(entity.getQ().convert());
        }
        drawAccs(entity, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }

    private void drawAccs(@NotNull EntityWindTunnel entity, float partialTicks, @NotNull PoseStack poseStack,
                          @NotNull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        EntityVehicle vehicle = entity.getSimulatedVehicle();
        QuaternionF q = vehicle.getQBySide();
        QuaternionF qi = vehicle.getQBySide();
        qi.conj();
        poseStack.mulPose(qi.convert());
        float maxForceMag = (float) entity.weightAcc.length();
        // draw sum forces
        drawAcc(poseStack, buffer, packedLight, WHITE, maxForceMag, Vec3.ZERO, entity.totalAcc);
        // draw gravity
        drawAcc(poseStack, buffer, packedLight, BLACK, maxForceMag, Vec3.ZERO, entity.weightAcc);
        // draw thrust
        drawAcc(poseStack, buffer, packedLight, BLUE, maxForceMag, Vec3.ZERO, entity.thrustAcc);
        // draw drag
        drawAcc(poseStack, buffer, packedLight, RED, maxForceMag, Vec3.ZERO, entity.dragAcc);
        // draw lift
        drawAcc(poseStack, buffer, packedLight, GREEN, maxForceMag, Vec3.ZERO, entity.liftAcc);
        // draw forces from surfaces
        for (PhysicsComponentInstance<?> phy : vehicle.getPhysicsInstances()) {
            Vec3 pos = UtilAngles.rotateVector(phy.getData().getPos(), q);
            drawAcc(poseStack, buffer, packedLight, YELLOW, maxForceMag, pos, vehicle.getAccFromForce(phy.getDragForce()));
            drawAcc(poseStack, buffer, packedLight, CYAN, maxForceMag, pos, vehicle.getAccFromForce(phy.getLiftForce()));
        }
        poseStack.popPose();
    }

    private void drawAcc(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight,
                         int[] color, float maxForceMag, Vec3 pos, Vec3 force) {
        float mag = (float)force.length();
        Vec3 dir;
        if (mag < 0.001f) dir = force.scale(1000).normalize();
        else dir = force.normalize();
        drawAcc(poseStack, buffer, packedLight, color, pos, dir, mag/maxForceMag*ARROW_LENGTH_SCALE);
    }

    private void drawAcc(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight,
                         int[] color, Vec3 pos, Vec3 dir, float mag) {
        poseStack.pushPose();
        poseStack.translate(pos.x, pos.y, pos.z);
        float y = UtilAngles.getYaw(dir);
        float x = UtilAngles.getPitch(dir);
        QuaternionF q1 = Vec3f.YN.rotationDegrees(y+180);
        q1.mul(Vec3f.XN.rotationDegrees(x-90));
        poseStack.mulPose(q1.convert());
        poseStack.translate(0, -mag*0.5, 0);

        poseStack.scale(1, mag, 1);
        for (int i = 0; i < 4; ++i) {
            poseStack.mulPose(Vec3f.YN.rotationDegrees(90*i).convert());
            drawTextureCentered(ARROW, Mat4f.from(poseStack.last().pose()), buffer, packedLight, 0, color);
        }
        poseStack.popPose();
    }

    private void drawGlobalAxis(float partialTicks, @NotNull PoseStack poseStack,
                                @NotNull MultiBufferSource buffer, int packedLight) {
        VertexConsumer buff = buffer.getBuffer(RenderType.lines());
        Vec3f O = new Vec3f();
        Vec3f X = new Vec3f(8, 0, 0);
        Vec3f Y = new Vec3f(0, 8, 0);
        Vec3f Z = new Vec3f(0, 0, 8);
        Mat4f m4 = Mat4f.from(poseStack.last().pose());
        Mat3f m3 = Mat3f.from(poseStack.last().normal());
        drawLine(O, X, buff, m4, m3, RED);
        drawLine(O, Y, buff, m4, m3, GREEN);
        drawLine(O, Z, buff, m4, m3, BLUE);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityWindTunnel entity) {
        return null;
    }
}
