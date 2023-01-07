package com.onewhohears.dscombat.data;

import java.util.HashMap;

import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class AircraftTextures {
	
	private final HashMap<Integer,ResourceLocation> textures = new HashMap<>();
	private int defaultId = 0;
	
	public AircraftTextures(CompoundTag tag) {
		//System.out.println("making textures with "+tag.toString());
		defaultId = tag.getInt("dyecolor");
		if (!tag.contains("textures", 10)) {
			//System.out.println("no textures object");
			return;
		}
		CompoundTag t = tag.getCompound("textures");
		//System.out.println("textures = "+t.toString());
		for (int i = 0; i < DyeColor.values().length; ++i) {
			if (t.contains(""+i, 8)) {
				String tex = t.getString(""+i);
				try { textures.put(i, new ResourceLocation(tex)); } 
				catch(ResourceLocationException e) {  }
			}
		}
	}
	
	public ResourceLocation getTexture(int colorId) {
		ResourceLocation rl = textures.get(colorId);
		if (rl == null) return getDefaultTexture();
		return rl;
	}
	
	public ResourceLocation getDefaultTexture() {
		return textures.get(defaultId);
	}
	
	public boolean hasTexture(int colorId) {
		return textures.containsKey(colorId);
	}
	
	public int getDefaultId() {
		return defaultId;
	}
	
}
