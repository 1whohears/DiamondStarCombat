package com.onewhohears.dscombat.util.math;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class UtilGeometry {
	
	public static boolean isPointInsideCone(Vec3 point, Vec3 origin, Vec3 direction, double maxAngle, double maxDistance) {
		Vec3 diff = point.subtract(origin);
		double dist = diff.length();
		if (dist > maxDistance) return false;
		double dot = diff.dot(direction);
		double mag = dist * direction.length();
		double angle = Math.acos(dot / mag);
		angle = Math.toDegrees(angle);
		return angle <= maxAngle;
	}
	
	public static boolean canEntitySeeEntity(Entity e1, Entity e2) {
		Level level = e1.getLevel();
		Vec3 diff = e2.position().subtract(e1.position());
		Vec3 look = diff.normalize();
		int dist = (int)diff.length();
		Vec3 pos = e1.position();
		int k = 0;
		while (k++ < dist) {
			if (!level.getBlockState(new BlockPos(pos)).isAir()) return false;
			pos = pos.add(look);
		}
		return true;
	}
	
}
