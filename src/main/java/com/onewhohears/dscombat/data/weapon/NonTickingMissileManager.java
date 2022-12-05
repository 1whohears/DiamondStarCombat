package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.entity.weapon.EntityMissile;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity.RemovalReason;

public class NonTickingMissileManager {
	
	private static final List<EntityMissile> missiles = new ArrayList<>();
	
	public static void serverTick(MinecraftServer server) {
		for (int i = 0; i < missiles.size(); ++i) 
			if (!tickMissile(missiles.get(i))) 
				missiles.remove(i--);
	}
	
	private static boolean tickMissile(EntityMissile missile) {
		int repeat = missile.getTickCountRepeat();
		System.out.println("SERVER TICK MISSILE "+missile+" "+missile.tickCount+" "+repeat);
		if (isRemoved(missile, repeat)) {
			System.out.println("REMOVING MISSILE FROM MANAGER");
			return false;
		}
		if (missile.inEntityTickingRange()) {
			System.out.println("MISSILE IN TICK RANGE");
			if (isUnloaded(missile)) {
				System.out.println("MISSILE UNLOADED");
				//missile.revive();
				//missile.level.addFreshEntity(missile);
			}
			if (repeat == 1) {
				System.out.println("TICK COUNT REPEAT 1");
				//missile.tickOutRange();
				//++missile.tickCount;
				missile.discard();
			} else if (repeat == 2) {
				System.out.println("TICK COUNT REPEAT 2");
				missile.revive();
				missile.level.addFreshEntity(missile);
			}
		} else {
			System.out.println("MISSILE OUT OF TICK RANGE");
			missile.tickOutRange();
			++missile.tickCount;
		}
		return true;
	}
	
	private static boolean isRemoved(EntityMissile missile, int repeat) {
		if (repeat > 0) return false;
		RemovalReason rr = missile.getRemovalReason();
		if (rr == null) return false;
		if (rr == RemovalReason.DISCARDED
				|| rr == RemovalReason.KILLED) {
			return true;
		}
		return false;
	}
	
	private static boolean isUnloaded(EntityMissile missile) {
		RemovalReason rr = missile.getRemovalReason();
		if (rr == null) return false;
		if (rr == RemovalReason.CHANGED_DIMENSION
				|| rr == RemovalReason.UNLOADED_TO_CHUNK
				|| rr == RemovalReason.UNLOADED_WITH_PLAYER) {
			return true;
		}
		return false;
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
