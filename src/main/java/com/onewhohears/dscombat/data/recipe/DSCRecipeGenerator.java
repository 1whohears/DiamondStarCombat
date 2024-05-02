package com.onewhohears.dscombat.data.recipe;

import java.util.function.Consumer;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.crafting.AircraftRecipe;
import com.onewhohears.dscombat.crafting.WeaponRecipe;
import com.onewhohears.dscombat.data.aircraft.AircraftPresetGenerator;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetStats;
import com.onewhohears.dscombat.data.weapon.WeaponPresetGenerator;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class DSCRecipeGenerator extends RecipeProvider {

	public DSCRecipeGenerator(DataGenerator pGenerator) {
		super(pGenerator);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
		genAircraftRecipes(finishedRecipeConsumer);
		genWeaponRecipes(finishedRecipeConsumer);
	}

	protected void genAircraftRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
		AircraftPresetGenerator.INSTANCE.GEN_MAP.forEach((key, preset) -> {
			if (!preset.isCraftable()) return;
			finishedRecipeConsumer.accept(new FinishedPresetRecipe(preset, 
					"workbench_vehicle_", AircraftRecipe.Serializer.INSTANCE));
		});
	}
	
	protected void genWeaponRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
		WeaponPresetGenerator.INSTANCE.GEN_MAP.forEach((key, preset) -> {
			finishedRecipeConsumer.accept(new FinishedPresetRecipe(preset, 
					"workbench_weapon_", WeaponRecipe.Serializer.INSTANCE));
		});
	}
	
	public static class FinishedPresetRecipe implements FinishedRecipe {
		private final ResourceLocation id;
		private final String presetId;
		private final RecipeSerializer<?> type;
		public FinishedPresetRecipe(JsonPresetStats preset, String prefix, RecipeSerializer<?> type) {
			this.id = new ResourceLocation(preset.getNameSpace(), prefix+preset.getId());
			this.presetId = preset.getId();
			this.type = type;
		}
		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty("presetId", presetId);
		}
		@Override
		public ResourceLocation getId() {
			return id;
		}
		@Override
		public RecipeSerializer<?> getType() {
			return type;
		}
		@Override
		public JsonObject serializeAdvancement() {
			return null;
		}
		@Override
		public ResourceLocation getAdvancementId() {
			return null;
		}
		
	}
	
}
