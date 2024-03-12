package com.onewhohears.dscombat.crafting;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.aircraft.AircraftPresets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class AircraftRecipe implements Recipe<Inventory> {
	
	private final ResourceLocation id;
    private final String presetId;

    public AircraftRecipe(ResourceLocation id, String presetId) {
        this.id = id;
        this.presetId = presetId;
    }
	
	@Override
	public boolean matches(Inventory container, Level level) {
		return DSCIngredient.hasIngredients(getDSCIngredients(), container);
	}

	@Override
	public ItemStack assemble(Inventory container) {
		return getOutput();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
		return getOutput().copy();
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	@Override
	public RecipeType<?> getType() {
		return Type.INSTANCE;
	}
	
	public List<DSCIngredient> getDSCIngredients() {
		AircraftPreset preset = AircraftPresets.get().getPreset(presetId);
		if (preset == null) return DSCIngredient.NONE;
		return preset.getIngredients();
	}
	
	public ItemStack getOutput() {
		AircraftPreset preset = AircraftPresets.get().getPreset(presetId);
		if (preset == null) return ItemStack.EMPTY;
		return preset.getItem();
	}
	
	public static class Type implements RecipeType<AircraftRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "aircraft_workbench";
        @Override
        public String toString() {
        	return ID;
        }
    }
	
	public static class Serializer implements RecipeSerializer<AircraftRecipe> {
		public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(DSCombatMod.MODID, "aircraft_workbench");
		@Override
		public AircraftRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
			String presetId = serializedRecipe.get("presetId").getAsString();
			return new AircraftRecipe(recipeId, presetId);
		}
		@Override
		public @Nullable AircraftRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			String presetId = buffer.readUtf();
			return new AircraftRecipe(recipeId, presetId);
		}
		@Override
		public void toNetwork(FriendlyByteBuf buffer, AircraftRecipe recipe) {
			buffer.writeUtf(recipe.presetId);
		}
	}

}
