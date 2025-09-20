package com.onewhohears.dscombat.data.forge;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.crafting.VehicleRecipe;
import com.onewhohears.dscombat.crafting.WeaponPartRecipe;
import com.onewhohears.dscombat.crafting.WeaponRecipe;
import com.onewhohears.dscombat.data.parts.PartPresetGenerator;
import com.onewhohears.dscombat.data.vehicle.VehiclePresetGenerator;
import com.onewhohears.dscombat.data.weapon.WeaponPresetGenerator;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetStats;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class DSCRecipeGenerator extends RecipeProvider {

	public DSCRecipeGenerator(DataGenerator pGenerator) {
		super(pGenerator);
	}

	@Override
	protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> finishedRecipeConsumer) {
		genAircraftRecipes(finishedRecipeConsumer);
		genWeaponRecipes(finishedRecipeConsumer);
		genWeaponPartRecipes(finishedRecipeConsumer);
	}

	protected void genAircraftRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
		VehiclePresetGenerator.INSTANCE.GEN_MAP.forEach((key, preset) -> {
			if (!preset.isCraftable()) return;
			finishedRecipeConsumer.accept(new FinishedPresetRecipe(preset, 
					"workbench_vehicle_", VehicleRecipe.Serializer.INSTANCE));
		});
	}
	
	protected void genWeaponRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
		WeaponPresetGenerator.INSTANCE.GEN_MAP.forEach((key, preset) -> {
			finishedRecipeConsumer.accept(new FinishedPresetRecipe(preset, 
					"workbench_weapon_", WeaponRecipe.Serializer.INSTANCE));
		});
	}

	protected void genWeaponPartRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
		PartPresetGenerator.INSTANCE.GEN_MAP.forEach((key, preset) -> {
			if (!preset.isCraftableWeaponPart()) return;
			finishedRecipeConsumer.accept(new FinishedPresetRecipe(preset,
					"workbench_weapon_part_", WeaponPartRecipe.Serializer.INSTANCE));
		});
	}
	
	public static class FinishedPresetRecipe implements FinishedRecipe {
		private final ResourceLocation id;
		private final String presetId;
		private final RecipeSerializer<?> type;
		public FinishedPresetRecipe(JsonPresetStats preset, String prefix, RecipeSerializer<?> type) {
			this.id = ResourceLocation.tryBuild(preset.getNameSpace(), prefix+preset.getId());
			this.presetId = preset.getId();
			this.type = type;
		}
		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty("presetId", presetId);
		}
		@Override
		public @NotNull ResourceLocation getId() {
			return id;
		}
		@Override
		public @NotNull RecipeSerializer<?> getType() {
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
