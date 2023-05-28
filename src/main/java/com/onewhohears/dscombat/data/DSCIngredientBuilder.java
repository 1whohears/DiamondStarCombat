package com.onewhohears.dscombat.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.JsonPreset.JsonPresetFactory;

import net.minecraft.resources.ResourceLocation;

public abstract class DSCIngredientBuilder<C extends DSCIngredientBuilder<C>> extends PresetBuilder<C> {

	protected DSCIngredientBuilder(String namespace, String name, JsonPresetFactory<? extends JsonPreset> sup) {
		super(namespace, name, sup);
	}
	
	protected DSCIngredientBuilder(String namespace, String name, JsonPresetFactory<? extends JsonPreset> sup, JsonObject copy) {
		super(namespace, name, sup, copy);
	}
	
	public C addIngredient(ResourceLocation item, int num) {
		if (!getData().has("ingredients")) {
			getData().add("ingredients", new JsonArray());
		}
		JsonObject i = new JsonObject();
		i.addProperty("item", item.toString());
		i.addProperty("num", num);
		getData().get("ingredients").getAsJsonArray().add(i);
		return (C) this;
	}
	
	public C addIngredient(String itemId, int num) {
		if (!getData().has("ingredients")) {
			getData().add("ingredients", new JsonArray());
		}
		JsonObject i = new JsonObject();
		i.addProperty("item", itemId);
		i.addProperty("num", num);
		getData().get("ingredients").getAsJsonArray().add(i);
		return (C) this;
	}
	
	public C addIngredient(ResourceLocation item) {
		return addIngredient(item, 1);
	}
	
	public C addIngredient(String itemId) {
		return addIngredient(itemId, 1);
	}

}
