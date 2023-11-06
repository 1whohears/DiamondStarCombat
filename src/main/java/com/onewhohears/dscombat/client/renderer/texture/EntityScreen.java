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
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EntityScreen implements AutoCloseable {
	
	protected final DynamicTexture texture;
    private final RenderType renderType;
    private boolean requiresUpload = true;
	
	public EntityScreen(String path, ResourceLocation baseTexture) {
		// TODO 1.5 need a background render type (texture doesn't change) and a dynamic render type (cleared before edited every frame)
		// also need to make a render screen instance for every vehicle entity. the current setup is changing the texture of every radar dash.
		// I'll simply replicate the map renderer MapInstance id system.
		NativeImage image = null;
		try {
			InputStream stream = Minecraft.getInstance().getResourceManager().getResource(baseTexture).get().open();
			image = NativeImage.read(stream);
		} catch (IOException e) {
			texture = null;
			renderType = null;
			e.printStackTrace();
			return;
		}
		texture = new DynamicTexture(image);
		ResourceLocation dynamicLoc = Minecraft.getInstance().getTextureManager().register(path, texture);
		renderType = RenderType.text(dynamicLoc);
	}
	
	public void forceUpload() {
		requiresUpload = true;
	}
	
	public boolean requiresUpload() {
		return requiresUpload;
	}
	
	protected void updateTexture(Entity entity) {
	}
	
	protected void setPixel(int x, int y, int color) {
		int width = texture.getPixels().getWidth();
		int height = texture.getPixels().getHeight();
		if (x >= width) return;
		else if (y >= height) return;
		texture.getPixels().setPixelRGBA(x, y, color);
	}
	
	protected void drawCircle(int x, int y, int r, int color) {
		// TODO 1.4 slow draw circle code causes crash?
		/*double minAngle = Math.acos(1-1/r)*0.9;
		for (double angle = 0; angle < Math.PI*2; angle += minAngle) {
			int xr = (int)(r*Math.cos(angle));
			int yr = (int)(r*Math.sin(angle));
			setPixel(x+xr, y+yr, color);
		}*/
		setPixel(x, y, color);
	}
	
	public void draw(Entity entity, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, int packedLight) {
		if (requiresUpload()) {
			updateTexture(entity);
			texture.upload();
			requiresUpload = false;
		}
		Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
        vertexconsumer.vertex(matrix4f, 0, 1, -0.01F)
        	.color(255, 255, 255, 255)
        	.uv(0.0F, 1.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(matrix4f, 1, 1, -0.01F)
        	.color(255, 255, 255, 255)
        	.uv(1.0F, 1.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(matrix4f, 1, 0, -0.01F)
        	.color(255, 255, 255, 255)
        	.uv(1.0F, 0.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(matrix4f, 0, 0, -0.01F)
        	.color(255, 255, 255, 255)
        	.uv(0.0F, 0.0F).uv2(packedLight).endVertex();
	}
	
	@Override
	public void close() throws Exception {
		texture.close();
	}

}
