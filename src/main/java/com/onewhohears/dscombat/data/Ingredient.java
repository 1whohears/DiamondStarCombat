package com.onewhohears.dscombat.data;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class Ingredient {
	
	public final String item;
	public final int num;
	
	public Ingredient(String item, int num) {
		this.item = item;
		this.num = num;
	}
	
	public Ingredient(CompoundTag tag) {
		item = tag.getString("item");
		num = tag.getInt("num");
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putString("item", item);
		tag.putInt("num", num);
		return tag;
	}
	
	public Ingredient(FriendlyByteBuf buffer) {
		item = buffer.readUtf();
		num = buffer.readInt();
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUtf(item);
		buffer.writeInt(num);
	}
	
	public ItemStack getItem() {
		Item i;
		try {
			i = ForgeRegistries.ITEMS.getDelegate(new ResourceLocation(item)).get().get();
		} catch (NoSuchElementException e) {
			return ItemStack.EMPTY;
		}
		ItemStack stack = new ItemStack(i);
		stack.setCount(num);
		return stack;
	}
	
	public static List<Ingredient> getIngredients(CompoundTag tag) {
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		ListTag list = tag.getList("ingredients", 10);
		for (int i = 0; i < list.size(); ++i) ingredients.add(new Ingredient(list.getCompound(i)));
		return ingredients;
	}
	
	public static void writeIngredients(List<Ingredient> ingredients, CompoundTag tag) {
		ListTag list = new ListTag();
		for (int i = 0; i < ingredients.size(); ++i) list.add(ingredients.get(i).write());
		tag.put("ingredients", list);
	}
	
	public static List<Ingredient> getIngredients(FriendlyByteBuf buffer) {
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		int size = buffer.readInt();
		for (int i = 0; i < size; ++i) ingredients.add(new Ingredient(buffer));
		return ingredients;
	}
	
	public static void writeIngredients(List<Ingredient> ingredients, FriendlyByteBuf buffer) {
		buffer.writeInt(ingredients.size());
		for (int i = 0; i < ingredients.size(); ++i) ingredients.get(i).write(buffer);
	}
	
}
