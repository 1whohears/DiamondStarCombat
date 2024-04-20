package com.onewhohears.dscombat.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

/**
 * this util class needs to exist if I want to have any hair left when down porting to 1.18.2
 * @author 1whoh
 */
public class UtilMCText {
	
	public static MutableComponent translatable(String text) {
		return Component.translatable(text);
	}
	
	public static MutableComponent translatable(String text, Object... args) {
		return Component.translatable(text, args);
	}
	
	public static MutableComponent literal(String text) {
		return Component.literal(text);
	}
	
	public static MutableComponent empty() {
		return Component.empty();
	}
	
	public static Component getItemName(String itemId) {
		Item item = UtilItem.getItem(itemId);
		if (item == null) return translatable(itemId);
		return translatable(item.getDescriptionId());
	}
	
}
