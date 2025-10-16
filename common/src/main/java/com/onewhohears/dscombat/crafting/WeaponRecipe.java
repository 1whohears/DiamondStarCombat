package com.onewhohears.dscombat.crafting;

import com.mojang.logging.LogUtils;
import com.onewhohears.onewholibs.data.crafting.IngredientStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.init.ModBlocks;
import com.onewhohears.onewholibs.util.UtilItem;

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
import org.slf4j.Logger;

public class WeaponRecipe implements Recipe<Inventory> {
	
	private final ResourceLocation id;
	private final String presetId;
    @Nullable private NonNullList<Ingredient> ingredients;

    public WeaponRecipe(ResourceLocation id, String presetId, @Nullable NonNullList<Ingredient> ingredients) {
        this.id = id;
        this.presetId = presetId;
        this.ingredients = ingredients;
    }
	
	@Override
	public boolean matches(Inventory inventory, Level level) {
		return UtilItem.testRecipe(getIngredients(), inventory);
	}

	@Override
	public @NotNull ItemStack assemble(Inventory container) {
		return getOutput();
	}
	
	@Override
	public @NotNull NonNullList<ItemStack> getRemainingItems(Inventory inventory) {
		return UtilItem.getRemainingItemsStackIngredients(inventory, getIngredients());
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public @NotNull ItemStack getResultItem() {
		return getOutput().copy();
	}

	@Override
	public @NotNull ItemStack getToastSymbol() {
		return new ItemStack(ModBlocks.WEAPONS_BLOCK.get());
	}
	
	@Override
	public @NotNull ResourceLocation getId() {
		return id;
	}

	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	@Override
	public @NotNull RecipeType<?> getType() {
		return Type.INSTANCE;
	}
	
	public String getWeaponPresetId() {
		return presetId;
	}
	
	public WeaponStats getWeaponData() {
		return WeaponPresets.get().get(getWeaponPresetId());
	}
	
	public int compare(WeaponRecipe other) {
		WeaponStats me = this.getWeaponData();
		WeaponStats you = other.getWeaponData();
		if (me == null || you == null) return 0;
		return me.compare(you);
	}
	
	public @NotNull NonNullList<Ingredient> getIngredients() {
        if (ingredients == null) {
            WeaponStats preset = getWeaponData();
            if (preset == null) ingredients = NonNullList.create();
            else ingredients = preset.getIngredients();
        }
        return ingredients;
	}
	
	public ItemStack getOutput() {
		WeaponStats preset = getWeaponData();
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
        private static final Logger LOGGER = LogUtils.getLogger();
        @Override
		public @NotNull WeaponRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
			String presetId = serializedRecipe.get("presetId").getAsString();
			return new WeaponRecipe(recipeId, presetId, null);
		}
		@Override
		public @NotNull WeaponRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			String presetId = buffer.readUtf();
            int size = buffer.readInt();
            NonNullList<Ingredient> ingredients = NonNullList.create();
            LOGGER.info("RECEIVING WEAPON RECIPE "+presetId);
            for (int i = 0; i < size; ++i) {
                IngredientStack is = IngredientStack.fromNetwork(buffer);
                LOGGER.info("cost = "+is.cost);
                ingredients.add(is);
            }
			return new WeaponRecipe(recipeId, presetId, ingredients);
		}
		@Override
		public void toNetwork(FriendlyByteBuf buffer, WeaponRecipe recipe) {
			buffer.writeUtf(recipe.presetId);
            buffer.writeInt(recipe.getIngredients().size());
            LOGGER.info("SENDING WEAPON RECIPE "+recipe.presetId);
            for (Ingredient i : recipe.getIngredients()) {
                IngredientStack.toNetwork(buffer, i);
                if (i instanceof IngredientStack is) {
                    LOGGER.info("cost = "+is.cost);
                }
            }
		}
	}

}
