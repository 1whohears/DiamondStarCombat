package com.onewhohears.dscombat.client.texture;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.vehicle.VehicleTextureManager.BlendMode;
import com.onewhohears.dscombat.data.vehicle.VehicleTextureManager.TextureLayer;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

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
			float blend = calcBlend(layers[i]);
			for (int x = 0; x < layerImage.getWidth(); ++x) for (int y = 0; y < layerImage.getHeight(); ++y) {
				int textureColor = layerImage.getPixelRGBA(x, y);
				if (NativeImage.getA(textureColor) == 0) continue;
				dynText.getPixels().setPixelRGBA(x, y, 
					blendColors(textureColor, layers[i], blend));
			}
		}
		dynText.upload();
		ResourceLocation textLoc = new ResourceLocation(DSCombatMod.MODID, "vehicle_layers_"+vehicle.getId());
		m.getTextureManager().register(textLoc, dynText);
		return textLoc;
	}
	
	private static int blendColors(int textureColor, TextureLayer layer, float blend) {
		int red = NativeImage.getR(textureColor);
		int green = NativeImage.getG(textureColor);
		int blue = NativeImage.getB(textureColor);
		if (blend == 0 || (layer.getBlendMode() == BlendMode.ON_WHITE && red == 255 && green == 255 && blue == 255)) 
			return NativeImage.combine(255, layer.getColor().getBlue(), 
					layer.getColor().getGreen(), layer.getColor().getRed());
		else if (blend == 1) 
			return textureColor;
		return NativeImage.combine(255, 
				blendColorChannel(blue, layer.getColor().getBlue(), blend), 
				blendColorChannel(green, layer.getColor().getGreen(), blend), 
				blendColorChannel(red, layer.getColor().getRed(), blend));
	}
	
	private static int blendColorChannel(int c1, int c2, float blend) {
		return (int)Math.round(c1 * blend + (1f - blend) * c2);
	}
	
	private static float calcBlend(TextureLayer layer) {
		switch (layer.getBlendMode()) {
		case EVEN: return 0.5f;
		case NONE: return 1;
		case ON_ALL: return 0;
		case ON_WHITE: return 1;
		case SCALED:
			Color color = layer.getColor();
			float f = 1f / 765f; // 1/3 * 1/255
			return (float)color.getRed()*f + (float)color.getGreen()*f + (float)color.getBlue()*f;
		}
		return 0;
	}
	
}
