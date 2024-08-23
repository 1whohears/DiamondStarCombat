package com.onewhohears.dscombat.entity.vehicle;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ClientSideHitboxStuckFixer {
	
	private static final int MAX_REPEATS = 4;
	private static List<Vec3> pos = new ArrayList<>();
	
	public static void onCollide(Entity entity, boolean isInside) {
		if (!isInside) return;
		Minecraft m = Minecraft.getInstance();
		if (entity.getId() != m.player.getId()) return;
		pos.add(0, entity.position());
		while (pos.size() > MAX_REPEATS) 
			pos.remove(pos.size()-1);
		if (isRepeating()) {
			entity.moveTo(entity.position().add(0, 10, 0));
		}
	}
	
	private static boolean isRepeating() {
		if (pos.size() < MAX_REPEATS) return false;
		for (int i = 1; i < pos.size(); ++i) 
			if (!pos.get(i).equals(pos.get(0))) 
				return false;
		return true;
	}
	
}
