package com.onewhohears.dscombat.util.math;

import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
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
		double mag = Math.sqrt(dir.lengthSqr() * base.lengthSqr());
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
	
	public static Vec3 vecCompByAxis(Vec3 u, Vec3 v) {
		if (isZero(v)) return Vec3.ZERO;
		return v.scale(u.dot(v) / v.lengthSqr());
	}
	
	public static Vec3 vecCompByNormAxis(Vec3 u, Vec3 n) {
		return n.scale(u.dot(n));
	}
	
	public static double vecCompMagSqrDirByAxis(Vec3 u, Vec3 v) {
		if (isZero(v)) return 0;
		double dot = u.dot(v);
		double vl2 = v.lengthSqr();
		Vec3 vec = v.scale(dot / vl2);
		return vec.lengthSqr() * Math.signum(dot);
	}
	
	public static double vecCompMagSqrDirByNormAxis(Vec3 u, Vec3 n) {
		double dot = u.dot(n);
		Vec3 vec = n.scale(dot);
		return vec.lengthSqr() * Math.signum(dot);
	}
	
	public static double vecCompMagDirByAxis(Vec3 u, Vec3 v) {
		if (isZero(v)) return 0;
		double dot = u.dot(v);
		double vl2 = v.lengthSqr();
		Vec3 vec = v.scale(dot / vl2);
		return vec.length() * Math.signum(dot);
	}
	
	public static double vecCompMagDirByNormAxis(Vec3 u, Vec3 n) {
		double dot = u.dot(n);
		Vec3 vec = n.scale(dot);
		return vec.length() * Math.signum(dot);
	}
	
	public static boolean isZero(Vec3 v) {
		return v.x == 0 && v.y == 0 && v.z == 0;
	}
	
	public static Vec3 getClosestPointOnAABB(Vec3 pos, AABB aabb) {
		if (pos == null) return aabb.getCenter();
		double rx = pos.x, ry = pos.y, rz = pos.z;
		if (rx >= aabb.maxX) rx = aabb.maxX;
		else if (rx <= aabb.minX) rx = aabb.minX;
		if (ry >= aabb.maxY) ry = aabb.maxY;
		else if (ry <= aabb.minY) ry = aabb.minY;
		if (rz >= aabb.maxZ) rz = aabb.maxZ;
		else if (rz <= aabb.minZ) rz = aabb.minZ;
		return new Vec3(rx, ry, rz);
	}
	
	public static boolean vec3NAN(Vec3 v) {
		return Double.isNaN(v.x) || Double.isNaN(v.y) || Double.isNaN(v.z);
	}
	
	public static int[] worldToScreenPosInt(Vec3 world_pos, Matrix4f view_mat, Matrix4f proj_mat, int width, int height) {
		float[] sp = worldToScreenPos(world_pos, view_mat, proj_mat, width, height);
		return new int[] {(int)sp[0], (int)sp[1]};
	}
	
	public static float[] worldToScreenPos(Vec3 world_pos, Matrix4f view_mat, Matrix4f proj_mat, int width, int height) {
		Vector4f clipSpace = new Vector4f((float)world_pos.x, (float)world_pos.y, (float)world_pos.z, 1f);
		clipSpace.transform(view_mat);
		clipSpace.transform(proj_mat);
		if (clipSpace.w() <= 0) return new float[] {-1,-1};
		Vector3f ndcSpace = new Vector3f(clipSpace);
		ndcSpace.mul(1/clipSpace.w());
		float win_x = (ndcSpace.x()+1f)/2f*width;
		float win_y = (ndcSpace.y()+1f)/2f*height;
		return new float[] {win_x, win_y};
	}
	
	public static Vector3f convertVector(Vec3 v) {
		return new Vector3f((float)v.x, (float)v.y, (float)v.z);
	}
	
	public static Vec3 convertVector(Vector3f v) {
		return new Vec3(v.x(), v.y(), v.z());
	}
	
	public static Vec3 getBBFeet(AABB bb) {
		return new Vec3(bb.getCenter().x, bb.minY, bb.getCenter().z);
	}
	
	public static Vec3 toFloats(Vec3 v) {
		return new Vec3((float)v.x, (float)v.y, (float)v.z);
	}
	
	public static Vec3 toVec3(BlockPos pos) {
		return new Vec3(pos.getX(), pos.getY(), pos.getZ());
	}
	
}
