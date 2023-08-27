package com.onewhohears.dscombat.data.aircraft;

import java.util.HashMap;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class AircraftTextures {
	
	private final HashMap<Integer,ResourceLocation> textures = new HashMap<>();
	private final int defaultId = 0;
	private String defaultTexturePath = "dscombat:textures/entity/";
	
	public AircraftTextures(JsonObject json) {
		JsonObject texJson = json.get("textures").getAsJsonObject();
		defaultTexturePath = texJson.get("default_texture_path").getAsString();
		JsonObject overrides = texJson.get("overrides").getAsJsonObject();
		for (int i = 0; i < DyeColor.values().length; ++i) {
			DyeColor dc = DyeColor.byId(i);
			String tex;
			if (overrides.has(dc.getName())) tex = overrides.get(dc.getName()).getAsString()+".png";
			else tex = defaultTexturePath+dc.getName()+".png";
			textures.put(i, new ResourceLocation(tex));
		}
	}
	
	public ResourceLocation getTexture(int colorId) {
		ResourceLocation rl = textures.get(colorId);
		if (rl == null) return textures.get(defaultId);
		return rl;
	}
	
	public boolean hasTexture(int colorId) {
		return textures.containsKey(colorId);
	}
	
}
