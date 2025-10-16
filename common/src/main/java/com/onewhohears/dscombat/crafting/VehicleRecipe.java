package com.onewhohears.dscombat.crafting;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.vehicle.VehiclePresets;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModBlocks;
import com.onewhohears.onewholibs.data.crafting.IngredientStack;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleRecipe implements Recipe<Inventory> {
	
	private final ResourceLocation id;
    private final String presetId;
    @Nullable private NonNullList<Ingredient> ingredients;

    public VehicleRecipe(ResourceLocation id, String presetId, @Nullable NonNullList<Ingredient> ingredients) {
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
		return new ItemStack(ModBlocks.AIRCRAFT_BLOCK.get());
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
	
	public String getVehiclePresetId() {
		return presetId;
	}
	
	public VehicleStats getVehicleStats() {
		return VehiclePresets.get().get(getVehiclePresetId());
	}
	
	public int getSortFactor() {
		VehicleStats preset = getVehicleStats();
		if (preset == null) return 0;
		return preset.getSortFactor();
	}
	
	public int compare(VehicleRecipe other) {
		if (this.getSortFactor() != other.getSortFactor()) 
			return this.getSortFactor() - other.getSortFactor();
		return this.presetId.compareToIgnoreCase(other.presetId);
	}

    public @NotNull NonNullList<Ingredient> getIngredients() {
        if (ingredients == null) {
            VehicleStats preset = getVehicleStats();
            if (preset == null) ingredients = NonNullList.create();
            else ingredients = preset.getIngredients();
        }
        return ingredients;
    }
	
	public ItemStack getOutput() {
		VehicleStats preset = getVehicleStats();
		if (preset == null) return ItemStack.EMPTY;
		return preset.getItem();
	}
	
	@Override
	public boolean isSpecial() {
		return true;
	}
	
	public static class Type implements RecipeType<VehicleRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "aircraft_workbench";
        @Override
        public String toString() {
        	return ID;
        }
    }
	
	public static class Serializer implements RecipeSerializer<VehicleRecipe> {
		public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(DSCombatMod.MODID, "aircraft_workbench");
        @Override
        public @NotNull VehicleRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            String presetId = serializedRecipe.get("presetId").getAsString();
            return new VehicleRecipe(recipeId, presetId, null);
        }
        @Override
        public @NotNull VehicleRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String presetId = buffer.readUtf();
            int size = buffer.readInt();
            NonNullList<Ingredient> ingredients = NonNullList.create();
            for (int i = 0; i < size; ++i) ingredients.add(IngredientStack.fromNetwork(buffer));
            return new VehicleRecipe(recipeId, presetId, ingredients);
        }
        @Override
        public void toNetwork(FriendlyByteBuf buffer, VehicleRecipe recipe) {
            buffer.writeUtf(recipe.presetId);
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient i : recipe.getIngredients()) IngredientStack.toNetwork(buffer, i);
        }
	}

}
