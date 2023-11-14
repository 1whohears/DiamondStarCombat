package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.UtilEntity;

import net.minecraft.resources.ResourceLocation;

public class VehicleTextureManager {
	
	public final EntityVehicle parent;
	private final ResourceLocation[] baseTextures;
	private final TextureLayer[] textureLayers;
	private int baseTextureIndex = 0;
	
	public VehicleTextureManager(EntityVehicle parent) {
		this.parent = parent;
		this.baseTextures = new ResourceLocation[parent.vehicleData.baseTextureVariants];
		this.textureLayers = new TextureLayer[parent.vehicleData.textureLayers];
		String[] encodeIds = UtilEntity.getSplitEncodeId(parent);
		String modId = encodeIds[0], entityId = encodeIds[1];
		for (int i = 0; i < baseTextures.length; ++i) 
			baseTextures[i] = new ResourceLocation(modId+":textures/entity/vehicle/"+entityId+"/base"+i+".png");
		for (int i = 0; i < baseTextures.length; ++i) {
			textureLayers[i] = new TextureLayer(modId+":textures/entity/vehicle/"+entityId+"/layer"+i+".png");
		}
		// TODO 1.2 vehicles get some base texture variants and 3-4 different layer textures that can be any color
	}
	
	public ResourceLocation getBaseTexture() {
		return baseTextures[baseTextureIndex];
	}
	
	public int getLayerNum() {
		return textureLayers.length;
	}
	
	public TextureLayer[] getTextureLayers() {
		return textureLayers;
	}
	
	public static class TextureLayer {
		private final ResourceLocation texture;
		private int color = 0xffffff;
		private boolean enabled = false;
		public TextureLayer(String texture) {
			this.texture = new ResourceLocation(texture);
		}
		public ResourceLocation getTexture() {
			return texture;
		}
		public int getColor() {
			return color;
		}
		public void setColor(int color) {
			this.color = color;
		}
		public boolean canRender() {
			return enabled;
		}
		public void showLayer() {
			enabled = true;
		}
		public void hideLayer() {
			enabled = false;
		}
	}
	
}
