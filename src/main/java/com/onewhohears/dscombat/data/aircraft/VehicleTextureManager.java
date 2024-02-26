package com.onewhohears.dscombat.data.aircraft;

import java.awt.Color;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.client.texture.VehicleDynamicTextures;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerVehicleTexture;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.UtilEntity;

import io.netty.buffer.ByteBuf;
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
	private ResourceLocation dynamicTexture;
	private ResourceLocation screenMap;
	
	public VehicleTextureManager(EntityVehicle parent) {
		this.parent = parent;
		this.baseTextures = new ResourceLocation[parent.getVehicleStats().baseTextureVariants];
		this.textureLayers = new TextureLayer[parent.getVehicleStats().textureLayers];
		setupTextureLocations();
		dynamicTexture = getBaseTexture();
	}
	
	private void setupTextureLocations() {
		String[] encodeIds = UtilEntity.getSplitEncodeId(parent);
		String modId = encodeIds[0], entityId = encodeIds[1];
		for (int i = 0; i < baseTextures.length; ++i) 
			baseTextures[i] = new ResourceLocation(modId+":textures/entity/vehicle/"+entityId+"/base"+i+".png");
		for (int i = 0; i < textureLayers.length; ++i) {
			textureLayers[i] = new TextureLayer(modId+":textures/entity/vehicle/"+entityId+"/layer"+i+".png");
		}
		screenMap = new ResourceLocation(modId+":textures/entity/vehicle/"+entityId+"/screens.png");
	}
	/**
	 * CLIENT ONLY
	 */
	private void setupDynamicTexture() {
		if (!parent.level.isClientSide) return;
		dynamicTexture = VehicleDynamicTextures.createVehicleDynamicTexture(parent);
	}
	/**
	 * CLIENT ONLY
	 */
	@Nullable
	public ResourceLocation getDynamicTexture() {
		return dynamicTexture;
	}
	
	public ResourceLocation getScreenMap() {
		return screenMap;
	}
	
	public void onTick() {
		if (parent.level.isClientSide) clientTick();
		else serverTick();
	}
	
	public void clientTick() {
		if (isChanged()) {
			PacketHandler.INSTANCE.sendToServer(new ToServerVehicleTexture(parent));
			setupDynamicTexture();
			resetChanged();
		}
	}
	
	public void serverTick() {
		
	}
	
	public void read(CompoundTag entityNbt) {
		if (!entityNbt.contains("textures")) return;
		setupTextureLocations();
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
	
	public void read(ByteBuf buffer) {
		setupTextureLocations();
		setBaseTexture(buffer.readInt());
		int layers = buffer.readInt();
		for (int i = 0; i < layers && i < textureLayers.length; ++i) 
			textureLayers[i].read(buffer);
		setupDynamicTexture();
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
	
	public int getBaseTextureNum() {
		return baseTextures.length;
	}
	
	public int getBaseTextureIndex() {
		return baseTextureIndex;
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
	
	public boolean isAllLayersDisabled() {
		for (int i = 0; i < getTextureLayers().length; ++i) 
			if (getTextureLayers()[i].canRender())
				return false;
		return true;
 	}
	
	public boolean isChanged() {
		if (changed) return true;
		for (int i = 0; i < textureLayers.length; ++i) 
			if (textureLayers[i].isChanged()) 
				return true;
		return false;
	}
	
	public void resetChanged() {
		changed = false;
		for (int i = 0; i < textureLayers.length; ++i) 
			textureLayers[i].resetChanged();
	}
	
	public static class TextureLayer {
		private final ResourceLocation texture;
		private BlendMode blendMode = BlendMode.ON_WHITE;
		private int colorInt;
		private Color color;
		private boolean enabled, changed;
		public TextureLayer(String texture) {
			this.texture = new ResourceLocation(texture);
			setColor(UtilEntity.getRandomColor());
			changed = false;
		}
		public void read(CompoundTag tag) {
			if (tag.contains("color")) setColor(tag.getInt("color"));
			enabled = tag.getBoolean("enabled");
			if (tag.contains("blendMode")) blendMode = BlendMode.getByName(tag.getString("blendMode"));
			changed = false;
		}
		public CompoundTag write() {
			CompoundTag tag = new CompoundTag();
			tag.putInt("color", colorInt);
			tag.putBoolean("enabled", enabled);
			tag.putString("blendMode", blendMode.name());
			return tag;
		}
		public void read(ByteBuf buffer) {
			setColor(buffer.readInt());
			enabled = buffer.readBoolean();
			blendMode = BlendMode.values()[buffer.readInt()];
			changed = false;
		}
		public void write(FriendlyByteBuf buffer) {
			buffer.writeInt(colorInt);
			buffer.writeBoolean(enabled);
			buffer.writeInt(blendMode.ordinal());
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
		public void setColor(String color) {
			try { setColor(Color.decode("0x"+color).getRGB()); }
			catch (NumberFormatException e) {}
		}
		public boolean canRender() {
			return enabled;
		}
		public void setCanRender(boolean render) {
			enabled = render;
			changed = true;
		}
		public boolean isChanged() {
			return changed;
		}
		public void resetChanged() {
			changed = false;
		}
		public BlendMode getBlendMode() {
			return blendMode;
		}
		public void setBlendMode(BlendMode mode) {
			blendMode = mode;
			changed = true;
		}
	}
	
	public static enum BlendMode {
		NONE,
		ON_WHITE,
		ON_ALL,
		SCALED,
		EVEN;
		public static BlendMode getByName(String name) {
			for (BlendMode mode : BlendMode.values()) 
				if (mode.name().equals(name)) 
					return mode;
			return NONE;
		}
	}
	
}
