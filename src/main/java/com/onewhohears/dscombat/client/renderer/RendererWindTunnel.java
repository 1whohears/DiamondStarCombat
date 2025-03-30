package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.obj.ObjVehicleModel;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.EntityWindTunnel;
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
    private static final float ARROW_LENGTH_SCALE = 4;

    public RendererWindTunnel(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(@NotNull EntityWindTunnel entity, float yaw, float partialTicks, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        ObjVehicleModel<EntityVehicle> model = entity.getSimulatedVehicle().getClientStatsHolder().get().getModel();
        poseStack.translate(0, 4, 0);
        model.render(entity.getSimulatedVehicle(), poseStack, buffer, packedLight, partialTicks);
        drawForces(entity, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }

    private void drawForces(@NotNull EntityWindTunnel entity, float partialTicks, @NotNull PoseStack poseStack,
                            @NotNull MultiBufferSource buffer, int packedLight) {
        EntityVehicle vehicle = entity.getSimulatedVehicle();
        float y = UtilAngles.getYaw(vehicle.position().subtract(Minecraft.getInstance().player.position()));
        Quaternion q = vehicle.getQBySide();
        float totalForceMag = (float) vehicle.getForces().length();
        // draw sum forces
        drawForce(poseStack, buffer, packedLight, y, WHITE, totalForceMag, Vec3.ZERO, vehicle.getForces());
        // draw gravity

        // draw thrust

        // draw drag

        // draw forces from surfaces

    }

    private void drawForce(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight,
                           float lookYaw, int[] color, float totalForceMag, Vec3 pos, Vec3 force) {
        drawForce(poseStack, buffer, packedLight, lookYaw, color, pos, force.normalize(),
                (float)force.length()/totalForceMag*ARROW_LENGTH_SCALE);
    }

    private void drawForce(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight,
                           float lookYaw, int[] color, Vec3 pos, Vec3 dir, float mag) {
        poseStack.pushPose();
        poseStack.translate(pos.x, pos.y, pos.z);
        //poseStack.mulPose(Vector3f.YN.rotationDegrees(yaw));
        float y = UtilAngles.getYaw(dir);
        float x = UtilAngles.getPitch(dir);
        //poseStack.mulPose(Vector3f.YN.rotationDegrees(y));
        poseStack.mulPose(Vector3f.XN.rotationDegrees(x-90));
        poseStack.translate(0, -mag*0.5, 0);

        poseStack.scale(1, mag, 1);
        drawTextureCentered(ARROW, poseStack.last().pose(), buffer, packedLight, 0, color);
        poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
        drawTextureCentered(ARROW, poseStack.last().pose(), buffer, packedLight, 0, color);
        poseStack.popPose();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityWindTunnel entity) {
        return null;
    }
}
