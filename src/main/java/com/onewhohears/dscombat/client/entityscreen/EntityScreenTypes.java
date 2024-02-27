package com.onewhohears.dscombat.client.entityscreen;

import com.onewhohears.dscombat.client.entityscreen.instance.AirRadarScreenInstance;
import com.onewhohears.dscombat.client.entityscreen.instance.EntityScreenInstance;
import com.onewhohears.dscombat.client.entityscreen.instance.FuelScreenInstance;
import com.onewhohears.dscombat.client.entityscreen.instance.GroundRadarScreenInstance;
import com.onewhohears.dscombat.client.entityscreen.instance.HudScreenInstance;
import com.onewhohears.dscombat.client.entityscreen.instance.RWRScreenInstance;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class EntityScreenTypes {
	
	public static final Int2ObjectMap<EntityScreenFactory> screenTypes = new Int2ObjectOpenHashMap<>();
	
	static {
		// TODO 1.1 give every vehicle that needs it a radar screen (show radar mode)
		screenTypes.put(EntityScreenIds.AIR_RADAR_SCREEN, AirRadarScreenInstance::new);
		screenTypes.put(EntityScreenIds.GROUND_RADAR_SCREEN, GroundRadarScreenInstance::new);
		// TODO 1.2 give every vehicle fuel screen
		screenTypes.put(EntityScreenIds.FUEL_SCREEN, FuelScreenInstance::new);
		// TODO 1.3 give every vehicle that needs it a hud screen
		screenTypes.put(EntityScreenIds.HUD_SCREEN, HudScreenInstance::new);
		// TODO 1.4 give every vehicle that needs it an rwr screen
		screenTypes.put(EntityScreenIds.RWR_SCREEN, RWRScreenInstance::new);
		// TODO 1.5 give every vehicle compass screen
		// TODO 1.6 give every vehicle that needs it turn coordinator screen
		// TODO 1.7 give every vehicle that needs it an attitude indicator
		// TODO 1.8 give every vehicle that needs it flare count screen
		// TODO 1.9 give every vehicle that needs it altitude screen
	}
	
	public interface EntityScreenFactory {
		EntityScreenInstance create(int id);
	}
	
}
