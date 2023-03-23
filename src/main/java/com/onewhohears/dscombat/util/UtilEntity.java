package com.onewhohears.dscombat.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

public class UtilEntity {
	
	public static boolean canEntitySeeEntity(Entity e1, Entity e2, int maxCheckDist) {
		return canEntitySeeEntity(e1, e2, maxCheckDist, 0, 0);
	}
	
	public static boolean canEntitySeeEntity(Entity e1, Entity e2, int maxCheckDist, 
			double throWater, double throBlock) {
		Level level = e1.getLevel();
		Vec3 diff = e2.position().subtract(e1.position());
		Vec3 look = diff.normalize();
		double distance = diff.length();
		Vec3 pos; int dist;
		if (distance <= maxCheckDist) {
			dist = (int)distance;
			pos = e1.position();
		} else {
			dist = maxCheckDist;
			pos = e1.position().add(look.scale(distance-maxCheckDist).subtract(look));
		}
		int k = 0;
		while (k++ < dist) {
			pos = pos.add(look);
			BlockPos bp = new BlockPos(pos);
			ChunkPos cp = new ChunkPos(bp);
			if (!level.hasChunk(cp.x, cp.z)) continue;
			BlockState block = level.getBlockState(bp);
			if (block == null || block.isAir()) continue;
			if (!block.getMaterial().blocksMotion() && !block.getMaterial().isLiquid()) continue;
			if (throWater <= 0 && throBlock <= 0) return false;
			if (block.getFluidState().getType().isSame(Fluids.WATER)) {
				if (throWater > 0) {
					--throWater;
					continue;
				} else return false;
			}
			if (throBlock > 0) {
				--throBlock;
				continue;
			} else return false;
		}
		return true;
	}
	
	public static int getDistFromGround(Entity e) {
		Level l = e.getLevel();
		int[] pos = {e.getBlockX(), e.getBlockY(), e.getBlockZ()};
		int dist = 0;
		while (pos[1] >= -64) {
			BlockState block = l.getBlockState(new BlockPos(pos[0], pos[1], pos[2]));
			if (block != null && !block.isAir()) break;
			--pos[1];
			++dist;
		}
		return dist;
	}
	
	public static Vec3 getLookingAtBlockPos(Entity e, int max) {
		Level level = e.level;
		Vec3 look = e.getLookAngle();
		Vec3 pos = e.position();
		for (int i = 0; i < max; ++i) {
			BlockState block = level.getBlockState(new BlockPos(pos));
			if (block != null && !block.isAir()) return pos;
			pos = pos.add(look);
		}
		return pos.add(look);
	}
	
	/**
	 * @param posY
	 * @return between 0 (no air pressure) and 1
	 */
	public static double getAirPressure(double posY) {
		double space = 1000;
		double water = 64;
		double scale = 1;
		if (posY > space) return 0;
		if (posY < water) return scale;
		return scale/(water-space) * (posY-water) + scale;
	}
	
	public static boolean isOnGroundOrWater(Entity entity) {
		Entity rv = entity.getRootVehicle();
		if (rv != null) {
			if (rv instanceof Boat) return true;
			if (rv instanceof Minecart) return true;
			if (rv.isOnGround() || rv.isInWater()) return true;
		}
		if (entity instanceof Player p) {
			if (p.isFallFlying()) return false;
			if (p.isSprinting()) return true;
		}
		if (entity.isOnGround() || entity.isInWater()) return true;
		return false;
	}
	
}
