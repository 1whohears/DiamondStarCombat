package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.obj.ObjVehicleModel;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.EntityWindTunnel;
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
        drawForces(entity.getSimulatedVehicle(), partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }

    private void drawForces(@NotNull EntityVehicle vehicle, float partialTicks, @NotNull PoseStack poseStack,
                            @NotNull MultiBufferSource buffer, int packedLight) {
        float y = UtilAngles.getYaw(vehicle.position().subtract(Minecraft.getInstance().player.position()));

    }

    private void drawForce(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight,
                           float yaw, int[] color, Vec3 pos) {
        poseStack.pushPose();
        poseStack.translate(pos.x, pos.y, pos.z);
        poseStack.mulPose(Vector3f.YN.rotationDegrees(yaw));
        Matrix4f matrix4f = poseStack.last().pose();
        drawTextureCentered(ARROW, matrix4f, buffer, packedLight, 0, color);
        poseStack.popPose();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityWindTunnel entity) {
        return null;
    }
}
