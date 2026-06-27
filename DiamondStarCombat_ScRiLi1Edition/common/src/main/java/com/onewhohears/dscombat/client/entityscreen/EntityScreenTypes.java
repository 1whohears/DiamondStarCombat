package com.onewhohears.dscombat.client.entityscreen;

import java.util.HashMap;
import java.util.Map;

import com.onewhohears.dscombat.client.entityscreen.instance.EntityScreenInstance;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class EntityScreenTypes {
	
	public static final Int2ObjectMap<EntityScreenFactory> screenTypes = new Int2ObjectOpenHashMap<>();
	private static final Map<Integer, Integer> colorsToScreenTypeId = new HashMap<>();
	
	public static void addScreenType(int id, EntityScreenFactory factory, String screenMapColorRGB) {
		screenTypes.put(id, factory);
		if (!screenMapColorRGB.isEmpty())
			colorsToScreenTypeId.put(stringRGBToIntABGR(screenMapColorRGB), id);
	}
	
	public static int getScreenTypeIdByColor(int abgrColor) {
		if (!colorsToScreenTypeId.containsKey(abgrColor)) return -1;
		return colorsToScreenTypeId.get(abgrColor);
	}
	
	public interface EntityScreenFactory {
		EntityScreenInstance create(int id);
	}
	
	public static int stringRGBToIntABGR(String rgb) {
		if (!rgb.startsWith("0x")) rgb = "0x" + rgb;
		int rgba = rgb.substring(2).length() < 8 ? 
		           Long.decode(rgb + "FF").intValue() : 
		           Long.decode(rgb).intValue();
        return Integer.reverseBytes(rgba);
	}
	
}
