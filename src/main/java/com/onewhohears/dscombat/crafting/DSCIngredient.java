package com.onewhohears.dscombat.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class DSCIngredient {
	
	public final int cost;
	private final String displayItemId;
	private ItemStack stack;
	
	public DSCIngredient(String displayItemId, int cost) {
		this.displayItemId = displayItemId;
		this.cost = cost;
	}
	
	public DSCIngredient(CompoundTag tag) {
		displayItemId = tag.getString("displayItemId");
		cost = tag.getInt("cost");
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putString("displayItemId", displayItemId);
		tag.putInt("cost", cost);
		return tag;
	}
	
	public DSCIngredient(FriendlyByteBuf buffer) {
		displayItemId = buffer.readUtf();
		cost = buffer.readInt();
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUtf(displayItemId);
		buffer.writeInt(cost);
	}
	
	public ItemStack getDisplayItem() {
		if (stack == null) {
			try {
				stack = new ItemStack(ForgeRegistries.ITEMS.getDelegate(
						new ResourceLocation(displayItemId)).get().get());
			} catch(NoSuchElementException e) {
				stack = ItemStack.EMPTY;
			}
		}
		return stack;
	}
	
	public boolean hasIngredient(Container playerInv) {
		int count = 0;
		for (int i = 0; i < playerInv.getContainerSize(); ++i) {
			ItemStack stack = playerInv.getItem(i);
			if (stack.isEmpty()) continue;
			if (isSameItem(stack)) count += stack.getCount();
		}
		return cost <= count;
	}
	
	public void consumeIngredient(Container playerInv) {
		int amount = cost;
		for (int i = 0; i < playerInv.getContainerSize(); ++i) {
			if (amount <= 0) return;
			ItemStack stack = playerInv.getItem(i);
			if (stack.isEmpty()) continue;
			if (isSameItem(stack)) {
				if (stack.getCount() < amount) {
					amount -= stack.getCount();
					playerInv.setItem(i, ItemStack.EMPTY);
				} else {
					stack.shrink(amount);
					return;
				}
			}
		}
	}
	
	public boolean isSameItem(ItemStack item) {
		return stack.sameItem(getDisplayItem());
	}
	
	public static List<DSCIngredient> getIngredients(CompoundTag tag) {
		List<DSCIngredient> ingredients = new ArrayList<DSCIngredient>();
		ListTag list = tag.getList("ingredients", 10);
		for (int i = 0; i < list.size(); ++i) ingredients.add(new DSCIngredient(list.getCompound(i)));
		return ingredients;
	}
	
	public static void writeIngredients(List<DSCIngredient> ingredients, CompoundTag tag) {
		ListTag list = new ListTag();
		for (int i = 0; i < ingredients.size(); ++i) list.add(ingredients.get(i).write());
		tag.put("ingredients", list);
	}
	
	public static List<DSCIngredient> getIngredients(FriendlyByteBuf buffer) {
		List<DSCIngredient> ingredients = new ArrayList<DSCIngredient>();
		int size = buffer.readInt();
		for (int i = 0; i < size; ++i) ingredients.add(new DSCIngredient(buffer));
		return ingredients;
	}
	
	public static void writeIngredients(List<DSCIngredient> ingredients, FriendlyByteBuf buffer) {
		buffer.writeInt(ingredients.size());
		for (int i = 0; i < ingredients.size(); ++i) ingredients.get(i).write(buffer);
	}
	
	public static boolean hasIngredients(List<DSCIngredient> ingredients, Container playerInv) {
		for (int i = 0; i < ingredients.size(); ++i) 
			if (!ingredients.get(i).hasIngredient(playerInv)) 
				return false;
		return true;
	}
	
	public static void consumeIngredients(List<DSCIngredient> ingredients, Container playerInv) {
		for (int i = 0; i < ingredients.size(); ++i) ingredients.get(i).consumeIngredient(playerInv);
	}
	
}
