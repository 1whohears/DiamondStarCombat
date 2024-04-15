package com.onewhohears.dscombat.crafting;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.ModBlocks;
import com.onewhohears.dscombat.util.UtilItem;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class WeaponRecipe implements Recipe<Inventory> {
	
	private final ResourceLocation id;
	private final String presetId;

    public WeaponRecipe(ResourceLocation id, String presetId) {
        this.id = id;
        this.presetId = presetId;
    }
	
	@Override
	public boolean matches(Inventory inventory, Level level) {
		return UtilItem.testRecipe(getIngredients(), inventory);
	}

	@Override
	public ItemStack assemble(Inventory container) {
		return getOutput();
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(Inventory inventory) {
		return UtilItem.getRemainingItemsStackIngredients(inventory, getIngredients());
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
	public ItemStack getToastSymbol() {
		return new ItemStack(ModBlocks.WEAPONS_BLOCK.get());
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
	
	public String getWeaponPresetId() {
		return presetId;
	}
	
	public WeaponData getWeaponData() {
		return WeaponPresets.get().getPresetNonCopy(getWeaponPresetId());
	}
	
	public int getSortFactor() {
		return getWeaponData().getType().ordinal();
	}
	
	public int compare(WeaponRecipe other) {
		if (this.getSortFactor() != other.getSortFactor()) 
			return this.getSortFactor() - other.getSortFactor();
		return this.presetId.compareToIgnoreCase(presetId);
	}
	
	public NonNullList<Ingredient> getIngredients() {
		WeaponData preset = getWeaponData();
		if (preset == null) return NonNullList.create();
		return preset.getIngredients();
	}
	
	public ItemStack getOutput() {
		WeaponData preset = getWeaponData();
		if (preset == null) return ItemStack.EMPTY;
		return preset.getNewItem();
	}
	
	@Override
	public boolean isSpecial() {
		return true;
	}
	
	public static class Type implements RecipeType<WeaponRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "weapon_workbench";
        @Override
        public String toString() {
        	return ID;
        }
    }
	
	public static class Serializer implements RecipeSerializer<WeaponRecipe> {
		public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(DSCombatMod.MODID, "weapon_workbench");
        @Override
		public WeaponRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
			String presetId = serializedRecipe.get("presetId").getAsString();
			return new WeaponRecipe(recipeId, presetId);
		}
		@Override
		public @Nullable WeaponRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			String presetId = buffer.readUtf();
			return new WeaponRecipe(recipeId, presetId);
		}
		@Override
		public void toNetwork(FriendlyByteBuf buffer, WeaponRecipe recipe) {
			buffer.writeUtf(recipe.presetId);
		}
	}

}
