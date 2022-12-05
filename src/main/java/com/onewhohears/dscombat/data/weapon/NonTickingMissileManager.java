package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.entity.weapon.EntityMissile;

import net.minecraft.server.MinecraftServer;

public class NonTickingMissileManager {
	
	private static final List<EntityMissile> missiles = new ArrayList<>();
	
	public static void serverTick(MinecraftServer server) {
		for (int i = 0; i < missiles.size(); ++i) 
			if (!tickMissile(missiles.get(i))) 
				missiles.remove(i--);
	}
	
	private static boolean tickMissile(EntityMissile missile) {
		if (missile.isRemoved()) {
			System.out.println("REMOVING MISSILE FROM MANAGER");
			return false;
		}
		if (missile.inEntityTickingRange()) {
			
		} else {
			missile.tickOutRange();
		}
		return true;
	}
	
	public static void addMissile(EntityMissile missile) {
		if (!missiles.contains(missile)) {
			System.out.println("ADDING MISSILE TO MANAGER "+missile);
			missiles.add(missile);
		} else {
			System.out.println("MISSILE ALREADY ADDED");
		}
	}
	
}
