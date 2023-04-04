package com.onewhohears.dscombat.util.math;

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
	
	public static double angleBetween(Vec3 dir, Vec3 base) {
		double dot = dir.dot(base);
		double mag = dir.length() * base.length();
		return Math.acos(dot / mag);
	}
	
	public static double angleBetweenDegrees(Vec3 dir, Vec3 base) {
		return Math.toDegrees(angleBetween(dir, base));
	}
	
	public static double angleBetweenVecPlane(Vec3 dir, Vec3 planeNormal) {
		double a = angleBetween(dir, planeNormal);
		return Math.PI/2 - a;
	}
	
	public static double angleBetweenVecPlaneDegrees(Vec3 dir, Vec3 planeNormal) {
		return Math.toDegrees(angleBetweenVecPlane(dir, planeNormal));
	}
	
	public static Vec3 interceptPos(Vec3 mPos, Vec3 mVel, Vec3 tPos, Vec3 tVel) {
		double x = interceptComponent(mPos.x, tPos.x, mVel.x, tVel.x);
		double y = interceptComponent(mPos.y, tPos.y, mVel.y, tVel.y);
		double z = interceptComponent(mPos.z, tPos.z, mVel.z, tVel.z);
		return new Vec3(x, y, z);
	}
	
	private static double interceptComponent(double mPos, double tPos, double mVel, double tVel) {
		double dp = tPos - mPos;
		double dv = tVel - mVel;
		if ((dp > 0 && dv < 0) || (dp < 0 && dv > 0))
			tPos += tVel * dp / -dv;
		return tPos;
	}
	
	public static Vec3 componentOfVecByAxis(Vec3 u, Vec3 v) {
		return v.scale(u.dot(v) / v.lengthSqr());
	}
	
	public static double componentMagSqrDirByAxis(Vec3 u, Vec3 v) {
		double dot = u.dot(v);
		double vl2 = v.lengthSqr();
		Vec3 vec = v.scale(dot / vl2);
		return vec.lengthSqr() * Math.signum(dot);
	}
	
	public static boolean isZero(Vec3 v) {
		return v.x == 0 && v.y == 0 && v.z == 0;
	}
	
}
