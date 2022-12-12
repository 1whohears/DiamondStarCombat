package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.onewhohears.dscombat.entity.weapon.EntityMissile;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.level.ChunkPos;

/**
 * {@link EntityMissile} constructor adds itself to this manager on the server side. Every server tick this class 
 * checks of the missile is being loaded or "ticked". If not the missile entity is discarded but the entity Object is still saved
 * and it's position is updated until it reaches a loaded chunk. Then the missile is added to the world again. 
 * @author 1whoh
 */
public class NonTickingMissileManager {
	
	private static final List<EntityMissile> missiles = new ArrayList<>();
	
	public static void serverTick(MinecraftServer server) {
		for (int i = 0; i < missiles.size(); ++i) 
			if (!tickMissile(missiles.get(i))) 
				missiles.remove(i--);
	}
	
	private static boolean tickMissile(EntityMissile missile) {
		//System.out.println("SERVER TICK MISSILE "+missile+" "+missile.tickCount);
		if (isKilled(missile)) {
			//System.out.println("REMOVING MISSILE FROM MANAGER");
			return false;
		}
		ChunkPos cp = missile.chunkPosition();
		boolean hasChunk = missile.level.hasChunk(cp.x, cp.z);
		boolean inTickRange = missile.inEntityTickingRange();
		int repeats = missile.getTickCountRepeats();
		//System.out.println(hasChunk+" "+inTickRange+" "+repeats);
		if (hasChunk && inTickRange && repeats < 5) {
			//System.out.println("MISSILE IN TICK RANGE");
			if (isUnloaded(missile)) {
				//System.out.println("MISSILE UNLOADED");
				missile.revive();
				missile.setUUID(UUID.randomUUID());
				missile.level.addFreshEntity(missile);
			}
		} else {
			//System.out.println("MISSILE OUT OF TICK RANGE");
			if (!missile.isRemoved()) missile.discardButTick();
			missile.tickOutRange();
			++missile.tickCount;
		}
		//System.out.println("FINISHED TICK MISSILE\n");
		return true;
	}
	
	private static boolean isKilled(EntityMissile missile) {
		if (missile.isDiscardedButTicking()) return false;
		RemovalReason rr = missile.getRemovalReason();
		if (rr == null) return false;
		if (rr == RemovalReason.KILLED || rr == RemovalReason.DISCARDED) {
			return true;
		}
		return false;
	}
	
	private static boolean isUnloaded(EntityMissile missile) {
		if (missile.isDiscardedButTicking()) return true;
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
		//System.out.println("ADDING MISSILE TO MANAGER "+missile);
		missiles.add(missile);
	}
	
}
