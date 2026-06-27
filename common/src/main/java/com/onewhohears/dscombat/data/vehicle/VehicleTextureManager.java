package com.onewhohears.dscombat.data.vehicle;

import com.onewhohears.dscombat.client.texture.VehicleDynamicTextures;
import com.onewhohears.dscombat.common.network.toserver.ToServerVehicleTexture;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class VehicleTextureManager {
	
	public final EntityVehicle parent;
	private ResourceLocation[] baseTextures;
	private TextureLayer[] textureLayers;
	private int baseTextureIndex = 0;
	private boolean changed;
	private ResourceLocation dynamicTexture;
	private net.minecraft.client.renderer.texture.DynamicTexture dynamicTextureObject;
	
	public VehicleTextureManager(EntityVehicle parent) {
		this.parent = parent;
		setupTextureLocations();
		dynamicTexture = getBaseTexture();
	}
	
	public void setupTextureLocations() {
		baseTextures = new ResourceLocation[parent.getStats().baseTextureVariants];
		textureLayers = new TextureLayer[parent.getStats().textureLayers];
		String namespace = parent.getStats().getNameSpace();
		for (int i = 0; i < baseTextures.length; ++i) 
			baseTextures[i] = new ResourceLocation(namespace+":textures/entity/vehicle/"+parent.getAssetId()+"/base"+i+".png");
		for (int i = 0; i < textureLayers.length; ++i) {
			textureLayers[i] = new TextureLayer(namespace+":textures/entity/vehicle/"+parent.getAssetId()+"/layer"+i+".png");
		}
	}
	/**
	 * CLIENT ONLY
	 */
	public void setupDynamicTexture() {
		if (!parent.isClientSide()) return;
		net.minecraft.client.renderer.texture.DynamicTexture newTexture = 
			VehicleDynamicTextures.createOrUpdateVehicleDynamicTexture(parent, dynamicTextureObject);
		if (newTexture == null) {
			// No layers and no dirt — use base texture
			dynamicTexture = getBaseTexture();
			dynamicTextureObject = null;
		} else {
			// Register or update the dynamic texture
			if (dynamicTextureObject == null) {
				// First time creating dynamic texture
				dynamicTextureObject = newTexture;
				ResourceLocation textLoc = new ResourceLocation(
					com.onewhohears.dscombat.DSCombatMod.MODID, "vehicle_layers_" + parent.getId());
				com.onewhohears.dscombat.client.util.UtilClientSafeSounds.registerDynamicTexture(textLoc, dynamicTextureObject);
				dynamicTexture = textLoc;
			} else {
				// Texture already registered, just uploaded new pixels
				dynamicTextureObject = newTexture;
			}
		}
	}
	/**
	 * CLIENT ONLY
	 */
	@Nullable
	public ResourceLocation getDynamicTexture() {
		return dynamicTexture;
	}
	
	public void onTick() {
		if (parent.isClientSide()) clientTick();
		else serverTick();
	}
	
	public void clientTick() {
		if (isChanged()) {
            new ToServerVehicleTexture(parent).sendToServer();
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
	
	public enum BlendMode {
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
		public String getTranslatable() {
			return "blendmode.dscombat."+name().toLowerCase();
		}
	}
	
}
