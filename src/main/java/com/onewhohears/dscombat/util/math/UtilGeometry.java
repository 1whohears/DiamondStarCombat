package com.onewhohears.dscombat.util.math;

import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
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
	
	public static boolean isZero(Vector3f v) {
		return v.x() == 0 && v.y() == 0 && v.z() == 0;
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
		return new float[] {win_x, height - win_y};
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
	
	public static final Random RANDOM = new Random();
	
	public static Vec3 inaccurateTargetPos(Vec3 origin, Vec3 targetPos, float inaccuracy) {
		double dist = origin.distanceTo(targetPos);
		double i = dist*Math.tan(Mth.DEG_TO_RAD*inaccuracy);
		return targetPos.add((RANDOM.nextDouble()-0.5)*i, (RANDOM.nextDouble()-0.5)*i, (RANDOM.nextDouble()-0.5)*i);
	}
	
	public static boolean isEqual(Vec3 v1, Vec3 v2) {
		return v1.x == v2.x && v1.y == v2.y && v1.z == v2.z;
	}
	
	/**
	 * @return double array size 4 of roots. root 1 real, root 1 imaginary, root2 real, root2 imaginary
	 */
	public static double[] roots(double a, double b, double c) {
		double[] roots = new double[4];
		double d = b*b - 4*a*c;
		if (d > 0) {
			double sqrtD = Math.sqrt(d);
			roots[0] = (-b + sqrtD) / (2*a);
			roots[1] = 0;
			roots[2] = (-b - sqrtD) / (2*a);
			roots[3] = 0;
		} else if (d == 0) {
			double root = -b / (2*a);
			roots[0] = root;
			roots[1] = 0;
			roots[2] = root;
			roots[3] = 0;
		} else {
			double real = -b / (2*a);
			double imaginary = Math.sqrt(-d) / (2 * a);
			roots[0] = real;
			roots[1] = imaginary;
			roots[2] = real;
			roots[3] = -imaginary;
		}
		return roots;
	}
	
	/**
	 * @return double array size 2 of roots. null if imaginary.
	 */
	@Nullable
	public static double[] rootsNoI(double a, double b, double c) {
		double d = b*b - 4*a*c;
		if (d < 0) return null;
		double[] roots = new double[2];
		if (d == 0) {
			double root = -b / (2*a);
			roots[0] = root;
			roots[1] = root;
		} else {
			double sqrtD = Math.sqrt(d);
			roots[0] = (-b + sqrtD) / (2*a);
			roots[1] = (-b - sqrtD) / (2*a);
		}
		return roots;
	}
	
	public static int numTrue(boolean[] bools) {
		int num = 0;
		for (int i = 0; i < bools.length; ++i) 
			if (bools[i]) ++num;
		return num;
	}
	
	public static int getMaxYIndex(Vec3[] array) {
		if (array.length == 0) return -1;
		int maxIndex = 0;
		double max = array[0].y;
		for (int i = 1; i < array.length; ++i) {
			if (array[i].y > max) {
				maxIndex = i;
				max = array[i].y;
			}
		}
		return maxIndex;
	}
	
	public static int getMinYIndex(Vec3[] array) {
		if (array.length == 0) return -1;
		int minIndex = 0;
		double min = array[0].y;
		for (int i = 1; i < array.length; ++i) {
			if (array[i].y < min) {
				minIndex = i;
				min = array[i].y;
			}
		}
		return minIndex;
	}
	
	public static int getMaxXIndex(Vec3[] array) {
		if (array.length == 0) return -1;
		int maxIndex = 0;
		double max = array[0].x;
		for (int i = 1; i < array.length; ++i) {
			if (array[i].x > max) {
				maxIndex = i;
				max = array[i].x;
			}
		}
		return maxIndex;
	}
	
	public static int getMinXIndex(Vec3[] array) {
		if (array.length == 0) return -1;
		int minIndex = 0;
		double min = array[0].x;
		for (int i = 1; i < array.length; ++i) {
			if (array[i].x < min) {
				minIndex = i;
				min = array[i].x;
			}
		}
		return minIndex;
	}
	
	public static int getMaxZIndex(Vec3[] array) {
		if (array.length == 0) return -1;
		int maxIndex = 0;
		double max = array[0].z;
		for (int i = 1; i < array.length; ++i) {
			if (array[i].z > max) {
				maxIndex = i;
				max = array[i].z;
			}
		}
		return maxIndex;
	}
	
	public static int getMinZIndex(Vec3[] array) {
		if (array.length == 0) return -1;
		int minIndex = 0;
		double min = array[0].z;
		for (int i = 1; i < array.length; ++i) {
			if (array[i].z < min) {
				minIndex = i;
				min = array[i].z;
			}
		}
		return minIndex;
	}
	
	public static int getMinIndex(double[] array) {
		if (array.length == 0) return -1;
		int minIndex = 0;
		double min = array[0];
		for (int i = 1; i < array.length; ++i) {
			if (array[i] < min) {
				minIndex = i;
				min = array[i];
			}
		}
		return minIndex;
	}
	
	public static float crossProduct(Vec2 v1, Vec2 v2) {
		return v1.x * v2.y - v1.y * v2.x;
	}
	
	public static boolean isIn2DTriangle(Vec2 p, Vec2[] v) {
		if (v.length < 3) return false;
		float area = 0.5f * (-v[1].y*v[2].x + v[0].y*(-v[1].x+v[2].x) + v[0].x*(v[1].y-v[2].y) + v[1].x*v[2].y);
		float a = 1/(2*area);
		float s0 = a * (v[0].y*v[2].x - v[0].x*v[2].y + (v[2].y-v[0].y)*p.x + (v[0].x-v[2].x)*p.y);
		float t0 = a * (v[0].x*v[1].y - v[0].y*v[1].x + (v[0].y-v[1].y)*p.x + (v[1].x-v[0].x)*p.y);
		return s0 >= 0 && t0 >= 0 && 1-s0-t0 >= 0;
	}
	
	public static boolean isIn2DQuad(Vec2 p, Vec2[] v) {
		if (v.length < 4) return false;
		Vec2[] t1 = new Vec2[] {v[0], v[1], v[2]};
		Vec2[] t2 = new Vec2[] {v[0], v[2], v[3]};
		return isIn2DTriangle(p, t1) || isIn2DTriangle(p, t2); 
	}
	
	public static int add(int n, int a, int max) {
		n += a;
		double r = (double)n / (double)max;
		n -= (int)r * max;
		return n;
	}
	
	@Nullable
	public static Vec2 intersect(Vec2 as, Vec2 ae, Vec2 bs, Vec2 be) {
		Vec2 ad = ae.add(as.negated());
		Vec2 bd = be.add(bs.negated());
		float det = crossProduct(bd, ad);
		if (det == 0) return null;
		Vec2 D = bs.add(as.negated());
		float u = (D.y * bd.x - D.x * bd.y) / det;
		return as.add(ad.scale(u));
	}
	
}
