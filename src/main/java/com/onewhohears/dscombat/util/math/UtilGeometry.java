package com.onewhohears.dscombat.util.math;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class UtilGeometry {
	
	public static boolean isPointInsideCone(Vec3 point, Vec3 origin, Vec3 direction, double maxAngle, double maxDistance) {
		Vec3 diff = point.subtract(origin);
		double dist = diff.length();
		if (dist > maxDistance) {
			//System.out.println(dist+" > max dist "+maxDistance);
			return false;
		}
		double dot = diff.dot(direction);
		double mag = dist * direction.length();
		double angle = Math.acos(dot / mag);
		angle = Math.toDegrees(angle);
		if (angle > maxAngle) {
			//System.out.println(angle+" > max angle "+maxAngle);
			return false;
		}
		return true;
	}
	
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
	
	public static Vec3 interceptPos(Vec3 mPos, Vec3 mVel, Vec3 tPos, Vec3 tVel) {
		Vec3 dp = tPos.subtract(mPos);
		//System.out.println("D = "+dp);
		//System.out.println("X");
		double x = interceptComponent(dp.x, mVel.x, tVel.x);
		//System.out.println("x = "+x);
		//System.out.println("Y");
		double y = interceptComponent(dp.y, mVel.y, tVel.y);
		//System.out.println("y = "+y);
		//System.out.println("Z");
		double z = interceptComponent(dp.z, mVel.z, tVel.z);
		//System.out.println("z = "+z);
		return tPos.add(x, y, z);
	}
	
	private static double interceptComponent(double dp, double mVel, double tVel) {
		//System.out.println("mVel = "+mVel);
		//System.out.println("tVel = "+tVel);
		double dv = tVel - mVel;
		//System.out.println("dv = "+dv);
		if ((dp > 0 && dv < 0) || (dp < 0 && dv > 0))
			return tVel * dp / -dv;
		return 0;
	}
	
}
