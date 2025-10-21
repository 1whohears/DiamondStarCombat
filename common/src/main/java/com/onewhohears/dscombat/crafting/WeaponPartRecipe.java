package com.onewhohears.dscombat.crafting;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartPresets;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.init.ModBlocks;
import com.onewhohears.onewholibs.data.crafting.IngredientStack;
import com.onewhohears.onewholibs.util.UtilItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
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

public class WeaponPartRecipe implements Recipe<Inventory> {

	private final ResourceLocation id;
	private final String presetId;
    @Nullable private NonNullList<Ingredient> ingredients;

    public WeaponPartRecipe(ResourceLocation id, String presetId, @Nullable NonNullList<Ingredient> ingredients) {
        this.id = id;
        this.presetId = presetId;
        this.ingredients = ingredients;
    }
	
	@Override
	public boolean matches(Inventory inventory, Level level) {
		return UtilItem.testRecipe(getIngredients(), inventory);
	}

	@Override
	public @NotNull ItemStack assemble(Inventory container, RegistryAccess registry) {
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
	public @NotNull ItemStack getResultItem(RegistryAccess registry) {
		return getOutput().copy();
	}

	@Override
	public @NotNull ItemStack getToastSymbol() {
		return new ItemStack(ModBlocks.WEAPON_PARTS_BLOCK.get());
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
	
	public String getPartPresetId() {
		return presetId;
	}
	
	public PartStats getPartData() {
		return PartPresets.get().get(getPartPresetId());
	}
	
	public int compare(WeaponPartRecipe other) {
		PartStats me = this.getPartData();
		PartStats you = other.getPartData();
		if (me == null || you == null) return 0;
		return me.compare(you);
	}

    public @NotNull NonNullList<Ingredient> getIngredients() {
        if (ingredients == null) {
            PartStats preset = getPartData();
            if (preset == null) ingredients = NonNullList.create();
            else ingredients = preset.getIngredients();
        }
        return ingredients;
    }
	
	public ItemStack getOutput() {
		PartStats preset = getPartData();
		if (preset == null) return ItemStack.EMPTY;
		return preset.createPartInstance().getNewItemStack();
	}
	
	@Override
	public boolean isSpecial() {
		return true;
	}
	
	public static class Type implements RecipeType<WeaponPartRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "weapon_parts_workbench";
        @Override
        public String toString() {
        	return ID;
        }
    }
	
	public static class Serializer implements RecipeSerializer<WeaponPartRecipe> {
		public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(DSCombatMod.MODID, "weapon_parts_workbench");
        @Override
        public @NotNull WeaponPartRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            String presetId = serializedRecipe.get("presetId").getAsString();
            return new WeaponPartRecipe(recipeId, presetId, null);
        }
        @Override
        public @NotNull WeaponPartRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String presetId = buffer.readUtf();
            int size = buffer.readInt();
            NonNullList<Ingredient> ingredients = NonNullList.create();
            for (int i = 0; i < size; ++i) ingredients.add(IngredientStack.fromNetwork(buffer));
            return new WeaponPartRecipe(recipeId, presetId, ingredients);
        }
        @Override
        public void toNetwork(FriendlyByteBuf buffer, WeaponPartRecipe recipe) {
            buffer.writeUtf(recipe.presetId);
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient i : recipe.getIngredients()) IngredientStack.toNetwork(buffer, i);
        }
	}

}
