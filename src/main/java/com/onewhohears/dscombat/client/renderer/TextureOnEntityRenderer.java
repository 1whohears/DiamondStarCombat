package com.onewhohears.dscombat.client.renderer;

import java.io.IOException;
import java.io.InputStream;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
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
	public default void renderTexture(T entity, ResourceLocation texture, PoseStack poseStack, MultiBufferSource bufferSource,
			int packedLight, Vec3 pos, double width, double height, float xRot, float yRot, float zRot) {
		// TODO 1.2 how to render an image onto an entity
		poseStack.pushPose();
		
		poseStack.mulPose(Vector3f.XP.rotationDegrees(xRot));
		poseStack.mulPose(Vector3f.YP.rotationDegrees(yRot));
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(zRot));
		// transform pose stack to fit all the position rotation parameters
		NativeImage image = null;
		try {
			InputStream stream = Minecraft.getInstance().getResourceManager().getResource(texture).get().open();
			image = NativeImage.read(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		poseStack.popPose();
	}
	
}
