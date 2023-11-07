package com.onewhohears.dscombat.client.renderer.texture;

import java.io.IOException;
import java.io.InputStream;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EntityScreenInstance implements AutoCloseable {
	
	protected final DynamicTexture dynamicTexture;
	private final RenderType dynamicRenderType;
    private final RenderType baseRenderType;
	
	public EntityScreenInstance(String path, int id, ResourceLocation baseTexture) {
		baseRenderType = RenderType.text(baseTexture);
		NativeImage image;
		try {
			InputStream stream = Minecraft.getInstance().getResourceManager().getResource(baseTexture).get().open();
			image = NativeImage.read(stream);
		} catch (IOException e) {
			e.printStackTrace();
			dynamicTexture = null;
			dynamicRenderType = null;
			return;
		}
		dynamicTexture = new DynamicTexture(image.getWidth(), image.getHeight(), true);
		ResourceLocation dynamicLoc = Minecraft.getInstance().getTextureManager().register(path+"/"+id, dynamicTexture);
		dynamicRenderType = RenderType.text(dynamicLoc);
		System.out.println("DYNAMIC LOC = "+dynamicLoc);
	}
	
	public abstract boolean shouldUpdateTexture(Entity entity);
	protected abstract void updateTexture(Entity entity);
	
	protected void clearPixels() {
		int width = dynamicTexture.getPixels().getWidth();
		int height = dynamicTexture.getPixels().getHeight();
		for (int i = 0; i < width; ++i) for (int j = 0; j < height; ++j) 
			dynamicTexture.getPixels().setPixelRGBA(i, j, 0);
	}
	
	/**
	 * @param color AGBR (alpha, green, blue, red) 0xAAGGBBRR
	 */
	protected void setPixel(int x, int y, int color) {
		int width = dynamicTexture.getPixels().getWidth();
		int height = dynamicTexture.getPixels().getHeight();
		if (x < 0 || y < 0 || x >= width || y >= height) return;
		// setPixelRGBA function name lies. color integer must be in ABGR format.
		dynamicTexture.getPixels().setPixelRGBA(x, y, color);
		//System.out.println("SET PIXEL "+x+" "+y+" "+color);
	}
	
	/**
	 * @param color AGBR (alpha, green, blue, red) 0xAAGGBBRR
	 */
	protected void drawPlus(int x, int y, int r, int color) {
		// TODO 1.2 thickness parameter
		for (int i = x-r; i <= x+r; ++i) setPixel(i, y, color);
		for (int j = y-r; j <= y+r; ++j) setPixel(x, j, color);
	}
	
	/**
	 * @param color AGBR (alpha, green, blue, red) 0xAAGGBBRR
	 */
	protected void drawCross(int x, int y, int r, int color) {
		for (int i = x-r, j = y-r; i <= x+r; ++i) setPixel(i, j++, color);
		for (int i = x-r, j = y+r; i <= x+r; ++i) setPixel(i, j--, color);
	}
	
	/**
	 * @param color AGBR (alpha, green, blue, red) 0xAAGGBBRR
	 */
	protected void drawHollowCircle(int x, int y, int r, int color) {
		float minAngle = (float)Math.atan(1f/(float)r)*0.9f;
		if (minAngle == 0) {
			setPixel(x, y, color);
			return;
		}
		float pi2 =  Mth.PI*2;
		float angle = 0;
		while (angle < pi2) {
			int xr = (int)(r*Mth.cos(angle));
			int yr = (int)(r*Mth.sin(angle));
			setPixel(x+xr, y+yr, color);
			angle += minAngle;
		}
	}
	
	public void draw(Entity entity, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, int packedLight) {
		if (shouldUpdateTexture(entity)) {
			updateTexture(entity);
			dynamicTexture.upload();
		}
		Matrix4f matrix4f = poseStack.last().pose();
		drawTexture(baseRenderType, matrix4f, buffer, packedLight, 0);
        drawTexture(dynamicRenderType, matrix4f, buffer, packedLight, -0.01f);
	}
	
	private void drawTexture(RenderType type, Matrix4f matrix4f, MultiBufferSource buffer, int packedLight, float z) {
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
	
	@Override
	public void close() {
		dynamicTexture.close();
	}

}
