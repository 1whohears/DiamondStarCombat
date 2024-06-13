package com.onewhohears.dscombat.util.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.math.Quaternion;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RotableAABB {
	
	public static final double SUBSIZE = 0.5;
	public static final double SUBSIZEHALF = SUBSIZE*0.5;
	//public static final double SUB_COL_SKIN = 1E-7;
	public static final double SUB_COL_SKIN = 0;
	
	private Vec3 center, extents;
	private double maxRadius;
	private Quaternion rot = Quaternion.ONE.copy(), roti = Quaternion.ONE.copy();
	private final List<VoxelShape> subColliders = new ArrayList<>();
	
	public RotableAABB(AABB bb) {
		this(bb.getCenter(), extentsFromBB(bb));
	}
	
	public RotableAABB() {
		this(Vec3.ZERO, Vec3.ZERO);
	}
	
	public RotableAABB(double width, double height, double length) {
		this(Vec3.ZERO, new Vec3(width/2, height/2, length/2));
	}
	
	public RotableAABB(Vec3 center, Vec3 extents) {
		this.center = center;
		this.extents = extents;
		this.maxRadius = extents.length();
	}
	
	public RotableAABB copy() {
		return new RotableAABB(getCenter(), getExtents());
	}
	
	public void setCenterAndRot(Vec3 center, Quaternion q) {
		setCenter(center);
		setRot(q);
	}
	
	public boolean updateColliders(List<VoxelShape> colliders, Vec3 pos, AABB aabb, Vec3 entityMoveByParent) {
		//System.out.println("ADDING SUB COLLIDERS "+getSubColliders().size());
		subColliders.clear();
		boolean stuck = addSubColliders(pos, aabb);
		//if (!UtilGeometry.isZero(entityMoveByParent)) 
		//	stuck = addSubColliders(pos.add(entityMoveByParent), aabb.move(entityMoveByParent));
		colliders.addAll(subColliders);
		return stuck;
	}
	
	public boolean addSubColliders(Vec3 pos, AABB aabb) {
		boolean stuck = false;
		Vec3 clip = getPushOutPos(pos, aabb, SUB_COL_SKIN);
		Vec3 clipPosDiff = clip.subtract(pos);
		//System.out.println("clip pos diff = "+clipPosDiff);
		if (clipPosDiff.y < 1E-6 && clipPosDiff.y > 0) {
			clip = clip.subtract(0, clipPosDiff.y, 0);
			stuck = true;
		}
		//System.out.println("push clip = "+clip);
		Vec3 clipRelRot = toRelRotPos(clip);
		for (int i = 1; i <= 4; ++i) {
			double radius = SUBSIZEHALF * i;
			Vec3 shapeRelRot = new Vec3(
					shapePosComponent(clipRelRot.x, extents.x+SUB_COL_SKIN, radius), 
					shapePosComponent(clipRelRot.y, extents.y+SUB_COL_SKIN, radius), 
					shapePosComponent(clipRelRot.z, extents.z+SUB_COL_SKIN, radius));
			Vec3 shape = toWorldPos(shapeRelRot);
			//System.out.println("shape = "+shape);
			addShape(shape, radius);
		}
		return stuck;
	}
	
	private double shapePosComponent(double clipRelRot, double ext, double radius) {
		double max = ext - radius;
		return Math.signum(clipRelRot) * Math.min(Math.abs(clipRelRot), max);
	}
	
	private void addShape(Vec3 pos, double radius) {
		//System.out.println("shape pos = "+pos);
		VoxelShape shape = Shapes.create(
			pos.x-radius, pos.y-radius, pos.z-radius, 
			pos.x+radius, pos.y+radius, pos.z+radius);
		subColliders.add(shape);
	}
	
	public boolean contains(AABB aabb) {
		return contains(UtilGeometry.getClosestPointOnAABB(center, aabb));
	}
	
	public boolean contains(Vec3 pos) {
		return containsRelRot(toRelRotPos(pos));
	}
	
	public boolean containsRelRot(Vec3 relRotPos) {
		boolean insideX = relRotPos.x() <= extents.x() && relRotPos.x() >= -extents.x();
		boolean insideY = relRotPos.y() <= extents.y() && relRotPos.y() >= -extents.y();
		boolean insideZ = relRotPos.z() <= extents.z() && relRotPos.z() >= -extents.z();
		return insideX && insideY && insideZ;
	}
	
	public boolean isInside(AABB aabb, double skin) {
		return isInside(UtilGeometry.getClosestPointOnAABB(center, aabb), skin);
	}
	
	public boolean isInside(AABB aabb) {
		boolean inside = isInside(aabb, 0);
		//System.out.println("INTERSECT CHECK "+inside+" "+aabb+" "+this);
		return inside;
	}
	
	public boolean isInside(Vec3 pos, double skin) {
		return isInsideRelPos(toRelRotPos(pos), skin);
	}
	
	public boolean isInside(Vec3 pos) {
		return isInside(pos, 0);
	}
	
	public boolean isInsideRelPos(Vec3 relRotPos, double skin) {
		boolean insideX = relRotPos.x() < extents.x()+skin && relRotPos.x() > -extents.x()-skin;
		boolean insideY = relRotPos.y() < extents.y()+skin && relRotPos.y() > -extents.y()-skin;
		boolean insideZ = relRotPos.z() < extents.z()+skin && relRotPos.z() > -extents.z()-skin;
		return insideX && insideY && insideZ;
	}
	
	public boolean isInsideRelPos(Vec3 relRotPos) {
		return isInsideRelPos(relRotPos, 0);
	}
	
	public Vec3 toRelRotPos(Vec3 pos) {
		return UtilAngles.rotateVector(pos.subtract(center), roti);
	}
	
	public Vec3 toWorldPos(Vec3 relRotPos) {
		return UtilAngles.rotateVector(relRotPos, rot).add(center);
	}
	
	public Vec3 toWorldVel(Vec3 relRotVel) {
		return UtilAngles.rotateVector(relRotVel, rot);
	}
	
	public Optional<Vec3> clip(Vec3 from, Vec3 to) {
		return clip(from, to, true);
	}
	
	public Optional<Vec3> clip(Vec3 from, Vec3 to, boolean push) {
		Vec3 fromRelRot = toRelRotPos(from);
		if (isInsideRelPos(fromRelRot)) {
			if (push) return Optional.of(getPushOutPos(from, SUB_COL_SKIN));
			else return Optional.of(from);
		}
		Vec3 toRelRot = toRelRotPos(to);
		Vec3 diff = toRelRot.subtract(fromRelRot);
		double tMin = Double.MAX_VALUE;
		Vec3 clipRelRot = Vec3.ZERO;
		/*
		 * HOW 5 it seems this clip math is correct...
		 * but the survival player can't punch the hitbox from top down sometimes when the creative player can???
		 * oh well. why would you punch the boat from top down anyway. it works from the side consistently anyway.
		 */
		Double clipY = clipAxis(fromRelRot.y, toRelRot.y, extents.y);
		if (clipY != null) {
			double t = (clipY - fromRelRot.y) / diff.y;
			clipRelRot = fromRelRot.add(diff.scale(t));
			clipRelRot = new Vec3(clipRelRot.x, clipY, clipRelRot.z);
			if (t < tMin && containsRelRot(clipRelRot)) tMin = t;
		}
		Double clipX = clipAxis(fromRelRot.x, toRelRot.x, extents.x);
		if (clipX != null) {
			double t = (clipX - fromRelRot.x) / diff.x;
			clipRelRot = fromRelRot.add(diff.scale(t));
			clipRelRot = new Vec3(clipX, clipRelRot.y, clipRelRot.z);
			if (t < tMin && containsRelRot(clipRelRot)) tMin = t;
		}
		Double clipZ = clipAxis(fromRelRot.z, toRelRot.z, extents.z);
		if (clipZ != null) {
			double t = (clipZ - fromRelRot.z) / diff.z;
			clipRelRot = fromRelRot.add(diff.scale(t));
			clipRelRot = new Vec3(clipRelRot.x, clipRelRot.y, clipZ);
			if (t < tMin && containsRelRot(clipRelRot)) tMin = t;
		}
		if (tMin == Double.MAX_VALUE) return Optional.empty();
		Vec3 clip = toWorldPos(clipRelRot);
		return Optional.of(clip);
	}
	
	private Double clipAxis(double from, double to, double ext) {
		double diff = to - from;
		if (diff > 0 && from <= -ext && (from+diff) >= -ext) return -ext;
		else if (diff < 0 && from >= ext && (from+diff) <= ext) return ext;
		else return null;
	}
	
	public Vec3 getPushOutPos(Vec3 pos, AABB aabb, double skin) {
		Vec3[] relRotCorners = new Vec3[8];
		relRotCorners[0] = toRelRotPos(new Vec3(aabb.minX, aabb.minY, aabb.minZ));
		Vec3 zaxis = UtilAngles.getRollAxis(roti);
		Vec3 yaxis = UtilAngles.getYawAxis(roti);
		Vec3 xaxis = UtilAngles.getPitchAxis(roti);
		relRotCorners[1] = relRotCorners[0].add(zaxis.scale(aabb.getZsize()));
		relRotCorners[2] = relRotCorners[0].add(yaxis.scale(aabb.getYsize()));
		relRotCorners[3] = relRotCorners[0].add(zaxis.scale(aabb.getZsize())).add(yaxis.scale(aabb.getYsize()));
		relRotCorners[4] = relRotCorners[0].add(xaxis.scale(aabb.getXsize()));
		relRotCorners[5] = relRotCorners[0].add(zaxis.scale(aabb.getZsize())).add(xaxis.scale(aabb.getXsize()));
		relRotCorners[6] = relRotCorners[0].add(yaxis.scale(aabb.getYsize())).add(xaxis.scale(aabb.getXsize()));
		relRotCorners[7] = relRotCorners[0].add(zaxis.scale(aabb.getZsize())).add(yaxis.scale(aabb.getYsize())).add(xaxis.scale(aabb.getXsize()));
		int[] absIndex = new int[6];
		absIndex[0] = UtilGeometry.getMaxYIndex(relRotCorners);
		absIndex[1] = UtilGeometry.getMinYIndex(relRotCorners);
		absIndex[2] = UtilGeometry.getMaxXIndex(relRotCorners);
		absIndex[3] = UtilGeometry.getMinXIndex(relRotCorners);
		absIndex[4] = UtilGeometry.getMaxZIndex(relRotCorners);
		absIndex[5] = UtilGeometry.getMinZIndex(relRotCorners);
		double[] dists = new double[12];
		dists[0] = Math.abs(extents.y - relRotCorners[absIndex[0]].y);
		dists[1] = Math.abs(-extents.y - relRotCorners[absIndex[0]].y);
		dists[2] = Math.abs(extents.y - relRotCorners[absIndex[1]].y);
		dists[3] = Math.abs(-extents.y - relRotCorners[absIndex[1]].y);
		dists[4] = Math.abs(extents.x - relRotCorners[absIndex[2]].x);
		dists[5] = Math.abs(-extents.x - relRotCorners[absIndex[2]].x);
		dists[6] = Math.abs(extents.x - relRotCorners[absIndex[3]].x);
		dists[7] = Math.abs(-extents.x - relRotCorners[absIndex[3]].x);
		dists[8] = Math.abs(extents.z - relRotCorners[absIndex[4]].z);
		dists[9] = Math.abs(-extents.z - relRotCorners[absIndex[4]].z);
		dists[10] = Math.abs(extents.z - relRotCorners[absIndex[5]].z);
		dists[11] = Math.abs(-extents.z - relRotCorners[absIndex[5]].z);
		int minIndex = UtilGeometry.getMinIndex(dists);
		//System.out.println("pushOutType: "+minIndex);
		Vec3 relRotPos = toRelRotPos(pos);
		Vec3 relRotPush = Vec3.ZERO;
		int extDir = minIndex % 2 == 0 ? 1 : -1;
		int absIndexIndex = minIndex / 2;
		if (minIndex % 4 == 0) absIndexIndex += 1;
		else if (minIndex % 4 == 3) absIndexIndex -= 1;
		Vec3 relRotCorner = relRotCorners[absIndex[absIndexIndex]];
		if (minIndex >= 0 && minIndex <= 3) {
			double ext = (extents.y + skin) * extDir;
			ext += relRotPos.y - relRotCorner.y;
			relRotPush = new Vec3(relRotPos.x, ext, relRotPos.z);
		} else if (minIndex >= 4 && minIndex <= 7) {
			double ext = (extents.x + skin) * extDir;
			ext += relRotPos.x - relRotCorner.x;
			relRotPush = new Vec3(ext, relRotPos.y, relRotPos.z);
		} else if (minIndex >= 8 && minIndex <= 11) {
			double ext = (extents.z + skin) * extDir;
			ext += relRotPos.z - relRotCorner.z;
			relRotPush = new Vec3(relRotPos.x, relRotPos.y, ext);
		}
		Vec3 push = toWorldPos(relRotPush);
		return push;
	}
	
	public Vec3 getPushOutPosOld(Vec3 pos, AABB aabb, double skin) {
		Vec3 close = UtilGeometry.getClosestPointOnAABB(center, aabb);
		Vec3 aabbDiff = pos.subtract(close);
		Vec3 push = getPushOutPos(close, skin).add(aabbDiff);
		return push;
	}
	
	public Vec3 getPushOutPos(Vec3 pos, double skin) {
		Vec3 posRelRot = toRelRotPos(pos);
		if (!isInsideRelPos(posRelRot)) return pos;
		double distSqrMin = Double.MAX_VALUE;
		Vec3 pushRelRot = Vec3.ZERO;
		Double pushY = pushAxis(posRelRot.y, extents.y, skin);
		if (pushY != null) {
			Vec3 test = new Vec3(posRelRot.x, pushY, posRelRot.z);
			double distSqr = posRelRot.distanceToSqr(test);
			//System.out.println("distSqr pushY = "+distSqr);
			if (distSqr < distSqrMin) {
				distSqrMin = distSqr;
				pushRelRot = test;
			}
		}
		Double pushX = pushAxis(posRelRot.x, extents.x, skin);
		if (pushX != null) {
			Vec3 test = new Vec3(pushX, posRelRot.y, posRelRot.z);
			double distSqr = posRelRot.distanceToSqr(test);
			//System.out.println("distSqr pushX = "+distSqr);
			if (distSqr < distSqrMin) {
				distSqrMin = distSqr;
				pushRelRot = test;
			}
		}
		Double pushZ = pushAxis(posRelRot.z, extents.z, skin);
		if (pushZ != null) {
			Vec3 test = new Vec3(posRelRot.x, posRelRot.y, pushZ);
			double distSqr = posRelRot.distanceToSqr(test);
			//System.out.println("distSqr pushZ = "+distSqr);
			if (distSqr < distSqrMin) {
				distSqrMin = distSqr;
				pushRelRot = test;
			}
		}
		if (distSqrMin == Double.MAX_VALUE) return pos;
		Vec3 push = toWorldPos(pushRelRot);
		//System.out.println("push = "+push);
		return push;
	}
	
	private Double pushAxis(double pos, double ext, double skin) {
		if (pos < 0) return -ext - skin;
		else return ext + skin;
	}
	
	public static Vec3 extentsFromBB(AABB bb) {
		return new Vec3(bb.getXsize()/2, bb.getYsize()/2, bb.getZsize()/2);
	}

	public Vec3 getCenter() {
		return center;
	}

	public void setCenter(Vec3 center) {
		this.center = center;
	}
	
	public Vec3 getExtents() {
		return extents;
	}
	
	public Quaternion getRot() {
		return rot.copy();
	}
	
	public Quaternion getIRot() {
		return roti.copy();
	}
	
	public void setRot(Quaternion rot) {
		this.rot = rot;
		this.roti = rot.copy();
		roti.conj();
	}
	
	public double getMaxRadius() {
		return maxRadius;
	}
	
	public EntityDimensions getMaxDimensions() {
		float max = (float) getMaxRadius();
		return EntityDimensions.scalable(max*2, max*2);
	}
	
	public DisguisedAABB getDisguisedAABB(Vec3 pos) {
		return new DisguisedAABB(this, pos, getMaxRadius());
	}
	
	public AABB makeMaxDimBox() {
		EntityDimensions d = getMaxDimensions();
    	double pX = center.x, pY = center.y, pZ = center.z;
    	double f = d.width / 2.0F;
        double f1 = d.height / 2.0F;
        return new AABB(pX-f, pY-f1, pZ-f, 
        		pX+f, pY+f1, pZ+f);
	}
	
	@Override
	public String toString() {
		return "RotableAABB:"+getCenter()+":"+getExtents();
	}
	
}
