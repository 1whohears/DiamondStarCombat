package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.renderer.model.EntityModelBullet1;
import com.onewhohears.dscombat.client.renderer.model.EntityModelMissile1;
import com.onewhohears.dscombat.entity.weapon.EntityAbstractWeapon;
import com.onewhohears.dscombat.entity.weapon.EntityBullet;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RendererEntityAbstractWeapon<T extends EntityAbstractWeapon> extends EntityRenderer<T> {
	
	protected EntityModel<?> model;
	
	public RendererEntityAbstractWeapon(Context ctx) {
		super(ctx);
		this.model = null;
		EntityBullet.MODEL_BULLET1 = new EntityModelBullet1<>(ctx.bakeLayer(EntityModelBullet1.LAYER_LOCATION));
		EntityMissile.MODEL_MISSILE1 = new EntityModelMissile1<>(ctx.bakeLayer(EntityModelMissile1.LAYER_LOCATION));
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return entity.getTexture();
	}
	
	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
		model = entity.getModel();
		
		poseStack.pushPose();
		poseStack.mulPose(Vector3f.YN.rotationDegrees(entity.getYRot()));
		poseStack.mulPose(Vector3f.XP.rotationDegrees(entity.getXRot()));
		
		VertexConsumer vertexconsumer = multiBufferSource.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
		model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		
		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, multiBufferSource, packedLight);
	}

}
