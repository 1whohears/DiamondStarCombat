package com.onewhohears.dscombat.crafting;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.init.ModRecipes;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.onewholibs.util.UtilItem;

import com.onewhohears.dscombat.util.UtilPresetParse;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class VehiclePartRepairRecipe extends CustomRecipe {

	public VehiclePartRepairRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		if (getRepairTool(container) == null) return false;
		ItemStack partItem = getVehiclePart(container);
		if (partItem == null) return false;
		if (partItem.getCount() != 1) return false;
		PartInstance<?> part = getPart(partItem);
		if (part == null) return false;
		if (!part.isDamaged()) return false;
		return UtilItem.testRecipe(part.getStats().getRepairCost(), container);
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		ItemStack partItem = getVehiclePart(container).copy();
		partItem.getOrCreateTag().putBoolean("damaged", false);
		return partItem;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		int repairIndex = getRepairToolIndex(container);
		ItemStack repairTool = container.getItem(repairIndex).copy();
		repairTool.setDamageValue(repairTool.getDamageValue()+5);
		ItemStack partItem = getVehiclePart(container);
		PartInstance<?> part = getPart(partItem);
		partItem.setCount(0);
		NonNullList<ItemStack> rem = UtilItem.getRemainingItemsStackIngredients(container, part.getStats().getRepairCost());
		rem.set(repairIndex, repairTool);
		container.clearContent();
		return rem;
	}
	
	@Nullable
	public ItemStack getRepairTool(CraftingContainer container) {
		for (int i = 0; i < container.getContainerSize(); ++i) 
			if (isRepairTool(container.getItem(i))) 
				return container.getItem(i);
		return null;
	}
	
	protected int getRepairToolIndex(CraftingContainer container) {
		for (int i = 0; i < container.getContainerSize(); ++i) 
			if (isRepairTool(container.getItem(i))) 
				return i;
		return -1;
	}
	
	@Nullable
	public ItemStack getVehiclePart(CraftingContainer container) {
		ItemStack part = null;
		for (int i = 0; i < container.getContainerSize(); ++i) {
			if (isVehiclePart(container.getItem(i))) {
				if (part == null) part = container.getItem(i);
				else return null;
			}
		}
		return part;
	}
	
	public boolean isRepairTool(ItemStack stack) {
		return stack.is(ModTags.Items.VEHICLE_REPAIR_TOOL);
	}
	
	public boolean isVehiclePart(ItemStack stack) {
		return stack.is(ModTags.Items.VEHICLE_PART);
	}
	
	@Nullable
	public PartInstance<?> getPart(ItemStack stack) {
		return UtilPresetParse.parsePartFromItem(stack);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.VEHICLE_PART_REPAIR.get();
	}
	
}
