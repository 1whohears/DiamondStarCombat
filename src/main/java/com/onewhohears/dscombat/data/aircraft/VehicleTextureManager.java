package com.onewhohears.dscombat.data.aircraft;

import java.awt.Color;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.UtilEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class VehicleTextureManager {
	
	public final EntityVehicle parent;
	private final ResourceLocation[] baseTextures;
	private final TextureLayer[] textureLayers;
	private int baseTextureIndex = 0;
	private boolean changed;
	
	public VehicleTextureManager(EntityVehicle parent) {
		this.parent = parent;
		this.baseTextures = new ResourceLocation[parent.vehicleData.baseTextureVariants];
		this.textureLayers = new TextureLayer[parent.vehicleData.textureLayers];
		String[] encodeIds = UtilEntity.getSplitEncodeId(parent);
		String modId = encodeIds[0], entityId = encodeIds[1];
		for (int i = 0; i < baseTextures.length; ++i) 
			baseTextures[i] = new ResourceLocation(modId+":textures/entity/vehicle/"+entityId+"/base"+i+".png");
		for (int i = 0; i < textureLayers.length; ++i) {
			textureLayers[i] = new TextureLayer(modId+":textures/entity/vehicle/"+entityId+"/layer"+i+".png");
		}
		// TODO 1.4 make a paint job item and gui allowing players to customize their vehicle layers and colors
		// TODO 1.5 sync all this data with clients
	}
	
	public void read(CompoundTag entityNbt) {
		if (!entityNbt.contains("textures")) return;
		CompoundTag textures = entityNbt.getCompound("textures");
		setBaseTexture(textures.getInt("baseTexture"));
		if (textures.contains("layers")) {
			ListTag layers = textures.getList("layers", 10);
			for (int i = 0; i < textureLayers.length && i < layers.size(); ++i)
				textureLayers[i].read(layers.getCompound(i));
		}
		changed = false;
	}
	
	public void write(CompoundTag entityNbt) {
		CompoundTag textures = new CompoundTag();
		textures.putInt("baseTexture", baseTextureIndex);
		ListTag layers = new ListTag();
		for (TextureLayer layer : getTextureLayers()) layers.add(layer.write());
		textures.put("layers", layers);
		entityNbt.put("textures", textures);
	}
	
	public void read(FriendlyByteBuf buffer) {
		setBaseTexture(buffer.readInt());
		int layers = buffer.readInt();
		for (int i = 0; i < layers && i < textureLayers.length; ++i) 
			textureLayers[i].read(buffer);
		changed = false;
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(baseTextureIndex);
		buffer.writeInt(textureLayers.length);
		for (int i = 0; i < textureLayers.length; ++i) 
			textureLayers[i].write(buffer);
	}
	
	public ResourceLocation getBaseTexture() {
		return baseTextures[baseTextureIndex];
	}
	
	public void setBaseTexture(int index) {
		if (index >= baseTextures.length || index < 0) index = 0;
		baseTextureIndex = index;
		changed = true;
	}
	
	public int getLayerNum() {
		return textureLayers.length;
	}
	
	public TextureLayer[] getTextureLayers() {
		return textureLayers;
	}
	
	public boolean isChanged() {
		if (changed) return true;
		for (int i = 0; i < textureLayers.length; ++i) 
			if (textureLayers[i].isChanged()) 
				return true;
		return false;
	}
	
	public static class TextureLayer {
		private final ResourceLocation texture;
		private int colorInt;
		private Color color;
		private boolean enabled, changed;
		public TextureLayer(String texture) {
			this.texture = new ResourceLocation(texture);
			setColor(UtilEntity.getRandomColor());
			showLayer();
			changed = false;
		}
		public void read(CompoundTag tag) {
			if (tag.contains("color")) setColor(tag.getInt("color"));
			enabled = tag.getBoolean("enabled");
			changed = false;
		}
		public CompoundTag write() {
			CompoundTag tag = new CompoundTag();
			tag.putInt("color", colorInt);
			tag.putBoolean("enabled", enabled);
			return tag;
		}
		public void read(FriendlyByteBuf buffer) {
			setColor(buffer.readInt());
			enabled = buffer.readBoolean();
		}
		public void write(FriendlyByteBuf buffer) {
			buffer.writeInt(colorInt);
			buffer.writeBoolean(enabled);
		}
		public ResourceLocation getTexture() {
			return texture;
		}
		public Color getColor() {
			return color;
		}
		public int getColorInt() {
			return colorInt;
		}
		public void setColor(int color) {
			this.colorInt = color;
			this.color = new Color(colorInt);
			changed = true;
		}
		public boolean canRender() {
			return enabled;
		}
		public void showLayer() {
			enabled = true;
			changed = true;
		}
		public void hideLayer() {
			enabled = false;
			changed = true;
		}
		public boolean isChanged() {
			return changed;
		}
	}
	
}
