package com.onewhohears.dscombat.client.renderer.texture;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class EntityScreenTypes {
	
	public static final Int2ObjectMap<EntityScreenFactory> screenTypes = new Int2ObjectOpenHashMap<>();
	public static final int RADAR_SCREEN = 0;
	
	static {
		screenTypes.put(RADAR_SCREEN, (id) -> new RadarScreenInstance(id));
	}
	
	public interface EntityScreenFactory {
		EntityScreenInstance create(int id);
	}
	
}
