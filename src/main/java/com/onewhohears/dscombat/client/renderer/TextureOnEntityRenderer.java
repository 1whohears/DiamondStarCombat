package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

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
	public default void renderTexture(T entity, ResourceLocation texture, PoseStack poseStack, MultiBufferSource multiBufferSource,
			int packedLight, Vec3 topLeft, Vec3 topRight, Vec3 bottomLeft) {
		// TODO 1.2 how to render an image onto an entity
	}
	
}
