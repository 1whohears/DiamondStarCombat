package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.EntityControllableModel;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RendererEntityTurret<T extends EntityTurret> extends EntityRenderer<T> {
	
	protected final EntityControllableModel<T> model;
	protected final ResourceLocation texture;
	
	public RendererEntityTurret(Context ctx, EntityControllableModel<T> model, ResourceLocation texture) {
		super(ctx);
		this.model = model;
		this.texture = texture;
	}

	@Override
	public ResourceLocation getTextureLocation(T pEntity) {
		return texture;
	}
	
	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
		if (!entity.shouldRender()) return;
		poseStack.pushPose();
		if (entity.getVehicle() instanceof EntityAircraft plane) {
			Quaternion qp = UtilAngles.lerpQ(partialTicks, plane.getPrevQ(), plane.getClientQ());
			poseStack.mulPose(qp);
		}
		poseStack.mulPose(Vector3f.YN.rotationDegrees(UtilAngles.lerpAngle180(partialTicks, entity.yRotRelO, entity.getRelRotY())));
		VertexConsumer vertexconsumer = multiBufferSource.getBuffer(model.renderType(getTextureLocation(entity)));
		model.renderToBuffer(entity, partialTicks, poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, multiBufferSource, packedLight);
	}

}
