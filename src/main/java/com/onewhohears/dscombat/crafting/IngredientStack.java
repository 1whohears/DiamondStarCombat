package com.onewhohears.dscombat.crafting;

import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.util.UtilItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class IngredientStack extends Ingredient {
	
	public static IngredientStack fromItem(String itemId, int cost) {
		return new IngredientStack(new ItemValue(UtilItem.getItem(itemId).getDefaultInstance()), cost);
	}
	
	public static IngredientStack fromTag(String tagId, int cost) {
		return new IngredientStack(new TagValue(ItemTags.create(new ResourceLocation(tagId))), cost);
	}
	
	public final int cost;
	
	protected IngredientStack(Value value, int cost) {
		super(Stream.of(value));
		this.cost = cost;
	}
	
	@Override
	public boolean test(@Nullable ItemStack stack) {
		if (stack == null) return false;
		ItemStack[] items = getItems();
		if (items.length == 0) return stack.isEmpty();
		for(ItemStack itemstack : items) 
			if (itemstack.is(stack.getItem()) && stack.getCount() >= cost) 
				return true;
		return false;
	}
	
	@Override
	public ItemStack[] getItems() {
		ItemStack[] items = super.getItems();
		for (ItemStack item : items) item.setCount(cost);
		return items;
	}

}
