package com.onewhohears.dscombat.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.onewholibs.util.UtilItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BucketConvertRecipe extends CustomRecipe {
	
	private final NonNullList<Ingredient> allIngredients = NonNullList.create();
	private final Ingredient converter;
	private final NonNullList<Ingredient> ingredients;
	private final ItemStack output;
	
	public BucketConvertRecipe(ResourceLocation pId, Ingredient converter, NonNullList<Ingredient> ingredients, ItemStack output) {
		super(pId, CraftingBookCategory.MISC);
		this.allIngredients.add(Ingredient.of(Items.BUCKET));
		this.allIngredients.add(converter);
		this.allIngredients.addAll(ingredients);
		this.converter = converter;
		this.ingredients = ingredients;
		this.output = output;
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		return UtilItem.testRecipe(getIngredients(), container);
	}
	
	@Override
	public @NotNull NonNullList<Ingredient> getIngredients() {
		return allIngredients;
	}
	
	public NonNullList<Ingredient> getFluidIngredients() {
		return ingredients;
	}
	
	@Override
	public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
		for(int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack item = container.getItem(i);
			if (converter.test(item)) {
				ItemStack item2 = item.copy();
				item2.setDamageValue(item.getDamageValue()+1);
				nonnulllist.set(i, item2);
			} else if (UtilItem.hasCraftingRemainingItem(item)) {
				nonnulllist.set(i, UtilItem.getCraftingRemainingItem(item));
			}
		}
		return nonnulllist;
	}

	@Override
	public @NotNull ItemStack assemble(CraftingContainer container, RegistryAccess registry) {
		return getOutput();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}
	
	@Override
	public boolean isSpecial() {
		return false; // must be false to be visible in jei
	}

	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}
	
	@Override
	public @NotNull ItemStack getResultItem(RegistryAccess registry) {
		return getOutput();
	}
	
	public ItemStack getOutput() {
		return output.copy();
	}
	
	public Ingredient getConverter() {
		return converter;
	}
	
	public static class Serializer implements RecipeSerializer<BucketConvertRecipe> {
		public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(DSCombatMod.MODID, "bucket_convert");
		@Override
		public @NotNull BucketConvertRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
			JsonObject converter = serializedRecipe.get("converter").getAsJsonObject();
			JsonArray ingredientsJson = serializedRecipe.get("ingredients").getAsJsonArray();
			NonNullList<Ingredient> ingredients = NonNullList.create();
			for (int i = 0; i < ingredientsJson.size(); ++i) 
				ingredients.add(Ingredient.fromJson(ingredientsJson.get(i).getAsJsonObject()));
			String output = serializedRecipe.get("output").getAsString();
			return new BucketConvertRecipe(recipeId, Ingredient.fromJson(converter), 
					ingredients, UtilItem.getItem(output).getDefaultInstance());
		}
		@Override
		public @NotNull BucketConvertRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			Ingredient converter = Ingredient.fromNetwork(buffer);
			NonNullList<Ingredient> ingredients = buffer.readCollection(size -> NonNullList.create(),
                    Ingredient::fromNetwork);
			ItemStack output = buffer.readItem();
			return new BucketConvertRecipe(recipeId, converter, ingredients, output);
		}
		@Override
		public void toNetwork(FriendlyByteBuf buffer, BucketConvertRecipe recipe) {
			recipe.getConverter().toNetwork(buffer);
			buffer.writeCollection(recipe.getFluidIngredients(), (buff, ing) -> ing.toNetwork(buff));
			buffer.writeItem(recipe.getOutput());
		}
	}

}
