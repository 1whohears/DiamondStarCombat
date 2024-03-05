package com.onewhohears.dscombat.client.entityscreen.instance;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EntityScreenInstance implements AutoCloseable{
	
	@Nullable private final RenderType baseRenderType;
	
	public EntityScreenInstance(int id, @Nullable ResourceLocation baseTexture) {
		if (baseTexture != null) baseRenderType = RenderType.text(baseTexture);
		else baseRenderType = null;
	}
	
	public void draw(Entity entity, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, int packedLight,
			float worldWidth, float worldHeight) {
		Matrix4f matrix4f = poseStack.last().pose();
		draw(entity, matrix4f, buffer, partialTicks, packedLight, worldWidth, worldHeight);
	}
	
	public void draw(Entity entity, Matrix4f matrix4f, MultiBufferSource buffer, float partialTicks, int packedLight,
			float worldWidth, float worldHeight) {
		if (baseRenderType != null) drawTextureCentered(baseRenderType, matrix4f, buffer, packedLight, 0);
	}
	
	protected void drawText(Component text, float xPos, float yPos, float maxWidth, PoseStack poseStack, MultiBufferSource buffer, int color, int packedLight) {
		Font font = Minecraft.getInstance().font;
		float width = font.width(text);
		float scale = maxWidth/width;
		poseStack.pushPose();
		poseStack.translate(xPos, yPos, -0.005f);
		poseStack.scale(scale, scale, 1);
		font.drawInBatch(text, 0, 0, color, false, poseStack.last().pose(), 
				buffer, true, 0, packedLight);
		poseStack.popPose();
	}
	
	protected void drawTextureTopLeft(RenderType type, Matrix4f matrix4f, MultiBufferSource buffer, int packedLight, float z) {
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
