package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.EntityModelParachute;
import com.onewhohears.dscombat.entity.EntityParachute;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

@Deprecated
public class RendererEntityParachute extends EntityRenderer<EntityParachute> {
	
	private EntityModelParachute model;
	private ResourceLocation texture;
	
	public RendererEntityParachute(Context context, EntityModelParachute model, ResourceLocation texture) {
		super(context);
		this.model = model;
		this.texture = texture;
	}
	
	@Override
	public void render(EntityParachute entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
		poseStack.pushPose();
		poseStack.mulPose(Vector3f.YN.rotationDegrees(UtilAngles.lerpAngle180(partialTicks, entity.yRotO, entity.getYRot())));
		poseStack.mulPose(Vector3f.XP.rotationDegrees(UtilAngles.lerpAngle(partialTicks, entity.xRotO, entity.getXRot())));
		
		VertexConsumer vertexconsumer = multiBufferSource.getBuffer(model.renderType(getTextureLocation(entity)));
		model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		
		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, multiBufferSource, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityParachute pEntity) {
		return texture;
	}

}
