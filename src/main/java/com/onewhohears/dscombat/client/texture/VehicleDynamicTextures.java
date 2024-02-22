package com.onewhohears.dscombat.client.texture;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.VehicleTextureManager.TextureLayer;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

public class VehicleDynamicTextures {
	
	private static final Logger LOGGER = LogUtils.getLogger();
	
	/**
	 * CALL ON CLIENT ONLY!
	 * creates a new texture that has all vehicle layers combined into one. 
	 * previous layer rendering involved rending the same model on top of itself (extremely slow). 
	 * @param vehicle
	 * @return the location of a texture with all layers combined into one
	 */
	public static ResourceLocation createVehicleDynamicTexture(EntityVehicle vehicle) {
		ResourceLocation baseTexLoc = vehicle.textureManager.getBaseTexture();
		if (vehicle.textureManager.isAllLayersDisabled()) return baseTexLoc;
		Minecraft m = Minecraft.getInstance();
		NativeImage baseImage;
		try {
			InputStream stream = m.getResourceManager().getResource(baseTexLoc).get().open();
			baseImage = NativeImage.read(stream);
		} catch (IOException e) {
			e.printStackTrace();
			return baseTexLoc;
		}
		DynamicTexture dynText = new DynamicTexture(baseImage);
		TextureLayer[] layers = vehicle.textureManager.getTextureLayers();
		for (int i = 0; i < layers.length; ++i) {
			if (!layers[i].canRender()) continue;
			NativeImage layerImage;
			try {
				InputStream stream = m.getResourceManager().getResource(layers[i].getTexture()).get().open();
				layerImage = NativeImage.read(stream);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			if (dynText.getPixels().getWidth() != layerImage.getWidth() || dynText.getPixels().getHeight() != layerImage.getHeight()) {
				LOGGER.error(baseTexLoc+" and "+layers[i].getTexture()+" do not have the same dimensions! This layer will not render!");
				continue;
			}
			float alpha = calcAlpha(layers[i].getColor());
			for (int x = 0; x < layerImage.getWidth(); ++x) for (int y = 0; y < layerImage.getHeight(); ++y) {
				int textureColor = layerImage.getPixelRGBA(x, y);
				if (NativeImage.getA(textureColor) == 0) continue;
				dynText.getPixels().setPixelRGBA(x, y, 
					blendColors(textureColor, layers[i].getColor(), alpha));
			}
		}
		dynText.upload();
		ResourceLocation textLoc = new ResourceLocation(DSCombatMod.MODID, "vehicle_layers_"+vehicle.getId());
		m.getTextureManager().register(textLoc, dynText);
		return textLoc;
	}
	
	public static int blendColors(int textureColor, Color layerColor, float alpha) {
		return NativeImage.combine(
				blendColorChannel(NativeImage.getA(textureColor), layerColor.getAlpha(), alpha), 
				blendColorChannel(NativeImage.getB(textureColor), layerColor.getBlue(), alpha), 
				blendColorChannel(NativeImage.getG(textureColor), layerColor.getGreen(), alpha), 
				blendColorChannel(NativeImage.getR(textureColor), layerColor.getRed(), alpha));
	}
	
	public static int blendColorChannel(int c1, int c2, float alpha) {
		return (int)Math.round(c1 * alpha + (1f - alpha) * c2);
	}
	
	private static float calcAlpha(Color color) {
		float f = 1f / 765f; // 1/3 * 1/255
		return (float)color.getRed()*f + (float)color.getGreen()*f + (float)color.getBlue()*f;
	}
	
}
