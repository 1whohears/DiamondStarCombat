package com.onewhohears.dscombat.data.recipe;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.crafting.IngredientStack;
import com.onewhohears.dscombat.data.jsonpreset.JsonPreset;
import com.onewhohears.dscombat.data.jsonpreset.PresetBuilder;
import com.onewhohears.dscombat.data.jsonpreset.JsonPreset.JsonPresetFactory;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public abstract class DSCIngredientBuilder<C extends DSCIngredientBuilder<C>> extends PresetBuilder<C> {
	
	public static NonNullList<Ingredient> getIngredients(JsonObject jsonPresetData) {
		NonNullList<Ingredient> ingredients = NonNullList.create();
		if (!jsonPresetData.has("ingredients")) return ingredients;
		JsonArray ja = jsonPresetData.get("ingredients").getAsJsonArray();
		for (int i = 0; i < ja.size(); ++i) {
			JsonObject jo = ja.get(i).getAsJsonObject();
			int cost = jo.get("num").getAsInt();
			if (jo.has("tag")) ingredients.add(IngredientStack.fromTag(jo.get("tag").getAsString(), cost));
			else if (jo.has("item")) ingredients.add(IngredientStack.fromItem(jo.get("item").getAsString(), cost));
		}
		return ingredients;
	}
	
	protected DSCIngredientBuilder(String namespace, String name, JsonPresetFactory<? extends JsonPreset> sup) {
		super(namespace, name, sup);
	}
	
	protected DSCIngredientBuilder(String namespace, String name, JsonPresetFactory<? extends JsonPreset> sup, JsonObject copy) {
		super(namespace, name, sup, copy);
	}
	
	protected C addIngredient(@Nullable String itemId, @Nullable String tagId, int num) {
		if (!getData().has("ingredients")) {
			getData().add("ingredients", new JsonArray());
		}
		JsonObject i = new JsonObject();
		if (itemId != null) i.addProperty("item", itemId);
		if (tagId != null) i.addProperty("tag", tagId);
		i.addProperty("num", num);
		getData().get("ingredients").getAsJsonArray().add(i);
		return (C) this;
	}
	
	public C addIngredient(String itemId, int num) {
		return addIngredient(itemId, null, num);
	}
	
	public C addIngredient(String itemId) {
		return addIngredient(itemId, 1);
	}
	
	public C addIngredient(ResourceLocation item, int num) {
		return addIngredient(item.toString(), num);
	}
	
	public C addIngredient(ResourceLocation item) {
		return addIngredient(item, 1);
	}
	
	public C addIngredientTag(String tagId, int num) {
		return addIngredient(null, tagId, num);
	}
	
	public C addIngredientTag(String tagId) {
		return addIngredientTag(tagId, 1);
	}
	
	public C addIngredientTag(ResourceLocation tagId, int num) {
		return addIngredientTag(tagId.toString(), num);
	}
	
	public C addIngredientTag(ResourceLocation tagId) {
		return addIngredientTag(tagId.toString(), 1);
	}

}
