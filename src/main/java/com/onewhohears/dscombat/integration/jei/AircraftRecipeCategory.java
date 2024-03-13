package com.onewhohears.dscombat.integration.jei;

import java.util.List;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.crafting.AircraftRecipe;
import com.onewhohears.dscombat.crafting.DSCIngredient;
import com.onewhohears.dscombat.init.ModBlocks;
import com.onewhohears.dscombat.util.UtilMCText;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class AircraftRecipeCategory implements IRecipeCategory<AircraftRecipe> {
	
	public static final ResourceLocation UID = new ResourceLocation(DSCombatMod.MODID, "aircraft_workbench");
	public static final RecipeType<AircraftRecipe> TYPE = RecipeType.create(UID.getNamespace(), UID.getPath(), AircraftRecipe.class);
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(DSCombatMod.MODID,
			"textures/ui/vehicle_forge_ui.png");
	
	private final IDrawable background;
	private final IDrawable icon;
	
	public AircraftRecipeCategory(IGuiHelper helper) {
		background = helper.drawableBuilder(TEXTURE, 64, 27, 224, 129)
				.setTextureSize(512, 512).build();
		icon = helper.createDrawableItemStack(ModBlocks.AIRCRAFT_BLOCK.get().asItem().getDefaultInstance());
	}
	
	@Override
	public RecipeType<AircraftRecipe> getRecipeType() {
		return TYPE;
	}

	@Override
	public Component getTitle() {
		return UtilMCText.translatable("container.dscombat.aircraft_block_menu");
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, AircraftRecipe recipe, IFocusGroup focuses) {
		List<DSCIngredient> ingredients = recipe.getDSCIngredients();
		int startX = 33, startY = 95;
		int ix = startX, iy = startY;
		int space = 18;
		for (int i = 0; i < ingredients.size(); ++i) {
			if (i == 9) {
				ix = startX;
				iy += space;
			}
			ItemStack stack = ingredients.get(i).getDisplayItem();
			builder.addSlot(RecipeIngredientRole.INPUT, ix, iy).addItemStack(stack);
			ix += space;
		}
		builder.addSlot(RecipeIngredientRole.OUTPUT, 105, 61).addItemStack(recipe.getResultItem());
	}

}
