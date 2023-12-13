package com.onewhohears.dscombat.client.entityscreen;

import java.io.IOException;
import java.io.InputStream;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public abstract class EntityDynamicScreenInstance extends EntityScreenInstance {
	
	protected final DynamicTexture dynamicTexture;
	protected final RenderType dynamicRenderType;
	protected int prevUpdateTickCount;
	
	public EntityDynamicScreenInstance(String path, int id, ResourceLocation baseTexture) {
		super(id, baseTexture);
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
	}
	
	public EntityDynamicScreenInstance(String path, int id, ResourceLocation baseTexture, int width, int height) {
		super(id, baseTexture);
		dynamicTexture = new DynamicTexture(width, height, true);
		ResourceLocation dynamicLoc = Minecraft.getInstance().getTextureManager().register(path+"/"+id, dynamicTexture);
		dynamicRenderType = RenderType.text(dynamicLoc);
	}
	
	public EntityDynamicScreenInstance(String path, int id, int width, int height) {
		this(path, id, null, width, height);
	}
	
	public abstract boolean shouldUpdateTexture(Entity entity);
	protected abstract void updateTexture(Entity entity);
	/**
	 * sets all pixels to transparent in dynamic texture
	 */
	protected void clearDynamicPixels() {
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
	 * @param r radius
	 * @param t thickness
	 * @param color AGBR (alpha, green, blue, red) 0xAAGGBBRR
	 */
	protected void drawPlus(int x, int y, int r, int t, int color) {
		double half = t*0.5d;
		int kMin = -(int)Math.floor(half);
		int kMax = (int)Math.ceil(half);
		for (int i = x-r; i <= x+r; ++i) for (int k = kMin; k < kMax; ++k) setPixel(i, y+k, color);
		for (int j = y-r; j <= y+r; ++j) for (int k = kMin; k < kMax; ++k) setPixel(x+k, j, color);
	}
	/**
	 * @param r radius
	 * @param color AGBR (alpha, green, blue, red) 0xAAGGBBRR
	 */
	protected void drawPlus(int x, int y, int r, int color) {
		drawPlus(x, y, r, 1, color);
	}
	/**
	 * @param r radius
	 * @param t thickness
	 * @param color AGBR (alpha, green, blue, red) 0xAAGGBBRR
	 */
	protected void drawCross(int x, int y, int r, int t, int color) {
		double half = t*0.5d;
		int kMin = -(int)Math.floor(half);
		int kMax = (int)Math.ceil(half);
		for (int i = x-r, j = y-r; i <= x+r; ++i, ++j) for (int k = kMin; k < kMax; ++k) setPixel(i+k, j, color);
		for (int i = x-r, j = y+r; i <= x+r; ++i, --j) for (int k = kMin; k < kMax; ++k) setPixel(i+k, j, color);
	}
	/**
	 * @param r radius
	 * @param color AGBR (alpha, green, blue, red) 0xAAGGBBRR
	 */
	protected void drawCross(int x, int y, int r, int color) {
		drawCross(x, y, r, 1, color);
	}
	/**
	 * @param r radius
	 * @param t thickness
	 * @param color AGBR (alpha, green, blue, red) 0xAAGGBBRR
	 */
	protected void drawHollowCircle(int x, int y, int r, int t, int color) {
		if (t > r) return;
		float pi2 =  Mth.PI*2;
		for (int rad = r; rad > r-t; --rad) {
			float minAngle = (float)Math.atan(1f/(float)rad)*0.65f;
			if (minAngle == 0) {
				setPixel(x, y, color);
				return;
			}
			float angle = 0;
			while (angle < pi2) {
				int xr = (int)(rad*Mth.cos(angle));
				int yr = (int)(rad*Mth.sin(angle));
				setPixel(x+xr, y+yr, color);
				angle += minAngle;
			}
		}
	}
	/**
	 * @param r radius
	 * @param color AGBR (alpha, green, blue, red) 0xAAGGBBRR
	 */
	protected void drawHollowCircle(int x, int y, int r, int color) {
		drawHollowCircle(x, y, r, 1, color);
	}
	
	protected void drawDiamond(int x, int y, int r, int t, int color) {
		double half = t*0.5d;
		int kMin = -(int)Math.floor(half);
		int kMax = (int)Math.ceil(half);
		for (int i = x-r, j = y; i <= x; ++i, ++j) for (int k = kMin; k < kMax; ++k) setPixel(i+k, j, color);
		for (int i = x-r, j = y; i <= x; ++i, --j) for (int k = kMin; k < kMax; ++k) setPixel(i+k, j, color);
		for (int i = x+r, j = y; i >= x; --i, ++j) for (int k = kMin; k < kMax; ++k) setPixel(i+k, j, color);
		for (int i = x+r, j = y; i >= x; --i, --j) for (int k = kMin; k < kMax; ++k) setPixel(i+k, j, color);
	}
	
	@Override
	public void draw(Entity entity, Matrix4f matrix4f, MultiBufferSource buffer, float partialTicks, int packedLight,
			float worldWidth, float worldHeight) {
		super.draw(entity, matrix4f, buffer, partialTicks, packedLight, worldWidth, worldHeight);
		if (shouldUpdateTexture(entity)) {
			updateTexture(entity);
			dynamicTexture.upload();
			prevUpdateTickCount = entity.tickCount;
		}
		drawTextureCentered(dynamicRenderType, matrix4f, buffer, packedLight, -0.001f);
	}
	
	@Override
	public void close() {
		super.close();
		dynamicTexture.close();
	}

}
