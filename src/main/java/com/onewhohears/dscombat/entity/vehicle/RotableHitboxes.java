package com.onewhohears.dscombat.entity.vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class RotableHitboxes {
	
	private static final Map<ResourceKey<Level>, List<RotableHitbox>> globalHitboxMap = new HashMap<>();
	
	public static List<RotableHitbox> getHitboxes(ResourceKey<Level> dimension) {
		if (!globalHitboxMap.containsKey(dimension)) globalHitboxMap.put(dimension, new ArrayList<>());
		return globalHitboxMap.get(dimension);
	}
	
	public static void onServerStop() {
		globalHitboxMap.clear();
	}
	
	public static void addHitbox(RotableHitbox hitbox) {
		List<RotableHitbox> hitboxes = getHitboxes(hitbox.level().dimension());
		if (hitboxes.contains(hitbox)) return;
		hitboxes.add(hitbox);
	}
	
	public static void removeHitbox(RotableHitbox hitbox) {
		List<RotableHitbox> hitboxes = getHitboxes(hitbox.level().dimension());
		hitboxes.remove(hitbox);
	}
	
}
