package com.onewhohears.dscombat.client.renderer.texture;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EntityScreenInstance implements AutoCloseable{
	
	protected final RenderType baseRenderType;
	
	public EntityScreenInstance(int id, ResourceLocation baseTexture) {
		baseRenderType = RenderType.text(baseTexture);
	}
	
	public void draw(Entity entity, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, int packedLight,
			float worldWidth, float worldHeight) {
		Matrix4f matrix4f = poseStack.last().pose();
		draw(entity, matrix4f, buffer, partialTicks, packedLight, worldWidth, worldHeight);
	}
	
	public void draw(Entity entity, Matrix4f matrix4f, MultiBufferSource buffer, float partialTicks, int packedLight,
			float worldWidth, float worldHeight) {
		drawTexture(baseRenderType, matrix4f, buffer, packedLight, 0);
	}
	
	protected void drawTexture(RenderType type, Matrix4f matrix4f, MultiBufferSource buffer, int packedLight, float z) {
		VertexConsumer vertexconsumer = buffer.getBuffer(type);
        vertexconsumer.vertex(matrix4f, 0, 1, z)
        	.color(255, 255, 255, 255)
        	.uv(0.0F, 1.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(matrix4f, 1, 1, z)
        	.color(255, 255, 255, 255)
        	.uv(1.0F, 1.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(matrix4f, 1, 0, z)
        	.color(255, 255, 255, 255)
        	.uv(1.0F, 0.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(matrix4f, 0, 0, z)
        	.color(255, 255, 255, 255)
        	.uv(0.0F, 0.0F).uv2(packedLight).endVertex();
	}
	
	protected void drawTextureCentered(RenderType type, Matrix4f matrix4f, MultiBufferSource buffer, int packedLight, float z) {
		VertexConsumer vertexconsumer = buffer.getBuffer(type);
        vertexconsumer.vertex(matrix4f, -0.5f, 0.5f, z)
        	.color(255, 255, 255, 255)
        	.uv(0.0F, 1.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(matrix4f, 0.5f, 0.5f, z)
        	.color(255, 255, 255, 255)
        	.uv(1.0F, 1.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(matrix4f, 0.5f, -0.5f, z)
        	.color(255, 255, 255, 255)
        	.uv(1.0F, 0.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(matrix4f, -0.5f, -0.5f, z)
        	.color(255, 255, 255, 255)
        	.uv(0.0F, 0.0F).uv2(packedLight).endVertex();
	}
	
	@Override
	public void close() {
	}
	
}
