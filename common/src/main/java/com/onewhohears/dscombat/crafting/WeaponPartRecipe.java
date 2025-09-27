package com.onewhohears.dscombat.crafting;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartPresets;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
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
import org.jetbrains.annotations.Nullable;

public class WeaponPartRecipe implements Recipe<Inventory> {

	private final ResourceLocation id;
	private final String presetId;

    public WeaponPartRecipe(ResourceLocation id, String presetId) {
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
		return new ItemStack(ModBlocks.WEAPON_PARTS_BLOCK.get());
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
	
	public NonNullList<Ingredient> getIngredients() {
		PartStats preset = getPartData();
		if (preset == null) return NonNullList.create();
		return preset.getIngredients();
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
		public WeaponPartRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
			String presetId = serializedRecipe.get("presetId").getAsString();
			return new WeaponPartRecipe(recipeId, presetId);
		}
		@Override
		public @Nullable WeaponPartRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			String presetId = buffer.readUtf();
			return new WeaponPartRecipe(recipeId, presetId);
		}
		@Override
		public void toNetwork(FriendlyByteBuf buffer, WeaponPartRecipe recipe) {
			buffer.writeUtf(recipe.presetId);
		}
	}

}
