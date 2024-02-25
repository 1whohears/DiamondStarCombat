package com.onewhohears.dscombat.util;

import java.util.NoSuchElementException;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilItem {

	public static Item getItem(String itemKey, Item alt) {
		try {
			return ForgeRegistries.ITEMS.getDelegate(
				new ResourceLocation(itemKey)).get().get();
		} catch(NoSuchElementException e) { return alt; }
	}
	
	public static Item getItem(String itemKey) {
		return getItem(itemKey, Items.AIR);
	}
	
	public static ResourceLocation getItemKey(Item item) {
		return ForgeRegistries.ITEMS.getKey(item);
	}
	
	public static String getItemKeyString(Item item) {
		return getItemKey(item).toString();
	}
	
}
