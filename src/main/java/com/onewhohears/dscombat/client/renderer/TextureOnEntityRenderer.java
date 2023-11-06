package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.renderer.texture.EntityScreen;

import net.minecraft.client.renderer.MultiBufferSource;
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
	public default void renderScreen(T entity, EntityScreen screen, PoseStack poseStack, MultiBufferSource buffer,
			float partialTicks, int packedLight, Vec3 pos, float width, float height, float xRot, float yRot, float zRot) {
		poseStack.pushPose();
		
		poseStack.translate(pos.x, pos.y, pos.z);
		poseStack.mulPose(Vector3f.XP.rotationDegrees(xRot));
		poseStack.mulPose(Vector3f.YP.rotationDegrees(yRot));
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(zRot));
		poseStack.translate(-0.5*width, -0.5*height, 0);
		poseStack.scale(width, height, 1);
		
		screen.draw(entity, poseStack, buffer, partialTicks, packedLight);
		
		poseStack.popPose();
	}
	
}
