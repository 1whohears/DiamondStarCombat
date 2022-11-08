package com.onewhohears.dscombat.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class UtilEntity {
	
	public static boolean canEntitySeeEntity(Entity e1, Entity e2) {
		//System.out.println("can "+e1+" see "+e2);
		Level level = e1.getLevel();
		Vec3 diff = e2.position().subtract(e1.position());
		Vec3 look = diff.normalize();
		int dist = (int)diff.length();
		Vec3 pos = e1.position();
		int k = 0;
		while (k++ < dist) {
			BlockState block = level.getBlockState(new BlockPos(pos));
			//System.out.println(k+" block "+block);
			if (block != null && !block.isAir()) {
				//System.out.println(e1+" can't see "+e2+" because "+block.toString());
				return false;
			}
			pos = pos.add(look);
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
}
