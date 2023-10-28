package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.client.model.InWorldScreenModel;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface TextureOnEntityRenderer<T extends Entity> {
	
	/**
	 * @param entity entity the texture is rendered onto
	 * @param texture texture to be rendered
	 * @param topLeft in world top left corner position of the texture
	 * @param topRight in world top right corner position of the texture
	 * @param bottomLeft in world bottom left corner position of the texture
	 */ 
	public default void renderTexture(T entity, ResourceLocation texture, PoseStack poseStack, MultiBufferSource multiBufferSource,
			int packedLight, Vec3 pos, double width, double height, float xRot, float yRot, float zRot, Quaternion rot) {
		// TODO 1.2 how to render an image onto an entity
		poseStack.pushPose();
		
		InWorldScreenModel model = InWorldScreenModel.get();
		// transform pose stack to fit all the position rotation parameters
		VertexConsumer vertexconsumer = multiBufferSource.getBuffer(model.renderType(texture));
		model.renderToBuffer(poseStack, vertexconsumer, packedLight, 
				OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		
		poseStack.popPose();
	}
	
}
