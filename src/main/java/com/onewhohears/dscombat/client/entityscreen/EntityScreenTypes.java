package com.onewhohears.dscombat.client.entityscreen;

import java.util.HashMap;
import java.util.Map;

import com.onewhohears.dscombat.client.entityscreen.instance.AirRadarScreenInstance;
import com.onewhohears.dscombat.client.entityscreen.instance.EntityScreenInstance;
import com.onewhohears.dscombat.client.entityscreen.instance.FuelScreenInstance;
import com.onewhohears.dscombat.client.entityscreen.instance.GroundRadarScreenInstance;
import com.onewhohears.dscombat.client.entityscreen.instance.HeadingScreenInstance;
import com.onewhohears.dscombat.client.entityscreen.instance.HudScreenInstance;
import com.onewhohears.dscombat.client.entityscreen.instance.RWRScreenInstance;
import com.onewhohears.dscombat.client.entityscreen.instance.SpeedScreenInstance;

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
	
	static {
		// TODO 1.0 give every vehicle that needs it a radar screen (show radar mode)
		addScreenType(EntityScreenIds.AIR_RADAR_SCREEN, AirRadarScreenInstance::new, "00FFFF");
		addScreenType(EntityScreenIds.GROUND_RADAR_SCREEN, GroundRadarScreenInstance::new, "4CFF00");
		// TODO 1.1 give every vehicle fuel screen
		addScreenType(EntityScreenIds.FUEL_SCREEN, FuelScreenInstance::new, "7F0000");
		// TODO 1.2 give every vehicle that needs it a hud screen
		addScreenType(EntityScreenIds.HUD_SCREEN, HudScreenInstance::new, "");
		// TODO 1.3 give every vehicle that needs it an rwr screen
		addScreenType(EntityScreenIds.RWR_SCREEN, RWRScreenInstance::new, "FF00DC");
		// TODO 1.4 give every vehicle that needs it heading screen
		addScreenType(EntityScreenIds.HEADING_SCREEN, HeadingScreenInstance::new, "0026FF");
		// TODO 1.6.1 give every vehicle that needs it turn coordinator screen
		// TODO 1.6.2 give every vehicle that needs it attitude indicator
		// TODO 1.6.3 give every vehicle that needs it aoa meter
		// TODO 1.7 give every vehicle that needs it altitude screen
		// TODO 1.8 give every vehicle that needs it air speed screen
		addScreenType(EntityScreenIds.AIR_SPEED_SCREEN, SpeedScreenInstance::new, "FFD800");
		// TODO 1.9 give every vehicle that needs it vertical speed screen
	}
	
	public interface EntityScreenFactory {
		EntityScreenInstance create(int id);
	}
	
	public static int stringRGBToIntABGR(String rgb) {
		if (!rgb.startsWith("0x")) rgb = "0x" + rgb;
		int rgba = rgb.substring(2).length() < 8 ? 
		           Long.decode(rgb + "FF").intValue() : 
		           Long.decode(rgb       ).intValue();
		int reverse = Integer.reverseBytes(rgba);
		return reverse;
	}
	
}
