package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.entity.weapon.EntityAbstractWeapon;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RendererEntityAbstractWeapon<T extends EntityAbstractWeapon> extends EntityRenderer<T> {
	
	protected final EntityModel<?> model;
	protected final ResourceLocation texture;
	
	public RendererEntityAbstractWeapon(Context ctx, EntityModel<T> model, ResourceLocation texture) {
		super(ctx);
		this.model = model;
		this.texture = texture;
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return texture;
	}
	
	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
		poseStack.pushPose();
		poseStack.mulPose(Vector3f.YN.rotationDegrees(entity.getYRot()));
		poseStack.mulPose(Vector3f.XP.rotationDegrees(entity.getXRot()));
		
		VertexConsumer vertexconsumer = multiBufferSource.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
		model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		
		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, multiBufferSource, packedLight);
	}

}
