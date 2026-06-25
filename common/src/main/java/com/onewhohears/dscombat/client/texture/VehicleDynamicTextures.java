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
	 * creates or updates a dynamic texture that has all vehicle layers combined into one. 
	 * @param vehicle
	 * @param existingTexture if not null, updates this texture in-place instead of creating new
	 * @return the DynamicTexture object (new or updated)
	 */
	public static net.minecraft.client.renderer.texture.DynamicTexture createOrUpdateVehicleDynamicTexture(
			EntityVehicle vehicle, net.minecraft.client.renderer.texture.DynamicTexture existingTexture) {
		ResourceLocation baseTexLoc = vehicle.textureManager.getBaseTexture();
		boolean noLayers = vehicle.textureManager.isAllLayersDisabled();
		float dirtLevel = vehicle.clientDirtLevel;
		boolean noDirt = dirtLevel < 0.005f;
		if (noLayers && noDirt) return null; // no dynamic texture needed
		Minecraft m = Minecraft.getInstance();
		NativeImage baseImage;
		try {
			InputStream stream = m.getResourceManager().getResource(baseTexLoc).get().open();
			baseImage = NativeImage.read(stream);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		net.minecraft.client.renderer.texture.DynamicTexture dynText = 
			existingTexture != null ? existingTexture : new net.minecraft.client.renderer.texture.DynamicTexture(baseImage);
		// If reusing existing texture, copy base image into it
		if (existingTexture != null) {
			for (int x = 0; x < baseImage.getWidth(); ++x) {
				for (int y = 0; y < baseImage.getHeight(); ++y) {
					dynText.getPixels().setPixelRGBA(x, y, baseImage.getPixelRGBA(x, y));
				}
			}
		}
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
				if (getA(textureColor) == 0) continue;
				dynText.getPixels().setPixelRGBA(x, y, 
					blendColors(textureColor, layers[i], blend));
			}
		}
		// Bake dirt overlay into the dynamic texture
		if (!noDirt) {
			String namespace = vehicle.getStats().getNameSpace();
			String assetId = vehicle.getAssetId();
			ResourceLocation dirtTexLoc = new ResourceLocation(namespace,
					"textures/entity/vehicle/" + assetId + "/dirt.png");
			try {
				var resource = m.getResourceManager().getResource(dirtTexLoc);
				if (resource.isEmpty()) { /* no dirt texture, skip */ }
				else {
				InputStream stream = resource.get().open();
				NativeImage dirtImage = NativeImage.read(stream);
				if (dynText.getPixels().getWidth() == dirtImage.getWidth()
						&& dynText.getPixels().getHeight() == dirtImage.getHeight()) {
					for (int x = 0; x < dirtImage.getWidth(); ++x) {
						for (int y = 0; y < dirtImage.getHeight(); ++y) {
							int dirtColor = dirtImage.getPixelRGBA(x, y);
							int dirtA = getA(dirtColor);
							if (dirtA == 0) continue;
							// blend dirt over base using dirtLevel as strength
							float alpha = (dirtA / 255f) * dirtLevel;
							int baseColor = dynText.getPixels().getPixelRGBA(x, y);
							int r = (int)(getR(baseColor) * (1 - alpha) + getR(dirtColor) * alpha);
							int g = (int)(getG(baseColor) * (1 - alpha) + getG(dirtColor) * alpha);
							int b = (int)(getB(baseColor) * (1 - alpha) + getB(dirtColor) * alpha);
							dynText.getPixels().setPixelRGBA(x, y, combine(getA(baseColor), b, g, r));
						}
					}
				}
				} // end else (resource present)
			} catch (IOException e) {
				// dirt.png not found — skip silently
			}
		}
		dynText.upload();
		return dynText;
	}
	
	private static int blendColors(int textureColor, TextureLayer layer, float blend) {
		int red = getR(textureColor);
		int green = getG(textureColor);
		int blue = getB(textureColor);
		if (blend == 0 || (layer.getBlendMode() == BlendMode.ON_WHITE && red == 255 && green == 255 && blue == 255)) 
			return combine(255, layer.getColor().getBlue(),
					layer.getColor().getGreen(), layer.getColor().getRed());
		else if (blend == 1) 
			return textureColor;
		return combine(255,
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

    public static int getA(int i) {
        return i >> 24 & 255;
    }

    public static int getR(int i) {
        return i & 255;
    }

    public static int getG(int i) {
        return i >> 8 & 255;
    }

    public static int getB(int i) {
        return i >> 16 & 255;
    }

    public static int combine(int a, int b, int g, int r) {
        return (a & 255) << 24 | (b & 255) << 16 | (g & 255) << 8 | (r & 255);
    }
	
}
