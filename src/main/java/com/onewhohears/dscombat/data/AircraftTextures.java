package com.onewhohears.dscombat.data;

import java.util.HashMap;

import javax.annotation.Nullable;

import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class AircraftTextures {
	
	private final HashMap<Integer,ResourceLocation> textures = new HashMap<>();
	private int defaultId = 0;
	
	public AircraftTextures(CompoundTag tag) {
		if (!tag.contains("textures", 10)) return;
		CompoundTag t = tag.getCompound("textures");
		for (int i = 0; i < DyeColor.values().length; ++i) {
			if (!t.contains(""+i, 8)) {
				String tex = t.getString(""+i);
				try { textures.put(i, new ResourceLocation(tex)); } 
				catch(ResourceLocationException e) {  }
			}
		}
		defaultId = t.getInt("defaultId");
	}
	
	@Nullable
	public ResourceLocation getTexture(int colorId) {
		return textures.get(colorId);
	}
	
	public ResourceLocation getDefaultTexture() {
		return textures.get(defaultId);
	}
	
}
