package com.onewhohears.dscombat.util.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.math.Quaternion;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RotableAABB {
	
	public static final double SUBSIZE = 0.5;
	public static final double SUBSIZEHALF = SUBSIZE*0.5;
	private Vec3 center, extents;
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
	}
	
	public RotableAABB copy() {
		return new RotableAABB(getCenter(), getExtents());
	}
	
	public void setCenterAndRot(Vec3 center, Quaternion q) {
		setCenter(center);
		setRot(q);
	}
	
	public void updateColliders(List<VoxelShape> colliders, AABB aabb, Vec3 entityMoveByParent) {
		//System.out.println("ADDING SUB COLLIDERS "+getSubColliders().size());
		subColliders.clear();
		addSubColliders(aabb);
		addSubColliders(aabb.move(entityMoveByParent));
		colliders.addAll(subColliders);
	}
	
	private double shapePosComponent(double clipRelRot, double ext, double radius) {
		double max = ext - radius;
		return Math.signum(clipRelRot) * Math.min(Math.abs(clipRelRot), max);
	}
	
	public void addSubColliders(AABB aabb) {
		Vec3 close = UtilGeometry.getClosestPointOnAABB(center, aabb);
		//System.out.println("close = "+close);
		Optional<Vec3> clipOpt = clip(close, center);
		if (clipOpt.isEmpty()) return;
		Vec3 clip = clipOpt.get();
		//System.out.println("clip = "+clip);
		Vec3 clipRelRot = toRelRotPos(clip);
		for (int i = 1; i <= 4; ++i) {
			double radius = SUBSIZEHALF * i;
			Vec3 shapeRelRot = new Vec3(
					shapePosComponent(clipRelRot.x, extents.x+0.001, radius), 
					shapePosComponent(clipRelRot.y, extents.y+0.001, radius), 
					shapePosComponent(clipRelRot.z, extents.z+0.001, radius));
			Vec3 shape = toWorldPos(shapeRelRot);
			//System.out.println("shape = "+shape);
			addShape(shape, radius);
		}
	}
	
	private void addShape(Vec3 pos, double radius) {
		//System.out.println("shape pos = "+pos);
		VoxelShape shape = Shapes.create(
			pos.x-radius, pos.y-radius, pos.z-radius, 
			pos.x+radius, pos.y+radius, pos.z+radius);
		subColliders.add(shape);
	}
	
	public boolean isColliding(AABB aabb) {
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
	
	public boolean isInside(AABB aabb) {
		return isInside(UtilGeometry.getClosestPointOnAABB(center, aabb));
	}
	
	public boolean isInside(Vec3 pos) {
		return isInsideRelPos(toRelRotPos(pos));
	}
	
	public boolean isInsideRelPos(Vec3 relRotPos) {
		boolean insideX = relRotPos.x() < extents.x() && relRotPos.x() > -extents.x();
		boolean insideY = relRotPos.y() < extents.y() && relRotPos.y() > -extents.y();
		boolean insideZ = relRotPos.z() < extents.z() && relRotPos.z() > -extents.z();
		return insideX && insideY && insideZ;
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
	
	public Optional<Vec3> clip(Vec3 from, Vec3 to, boolean containCheck) {
		Vec3 fromRelRot = toRelRotPos(from);
		if (containCheck && containsRelRot(fromRelRot)) return Optional.of(toWorldPos(fromRelRot));
		Vec3 toRelRot = toRelRotPos(to);
		Vec3 diff = toRelRot.subtract(fromRelRot);
		double tMin = Double.MAX_VALUE;
		Vec3 clipRelRot = Vec3.ZERO;
		/*
		 * HOW 5 it seems this clip math is correct...
		 * but the survival player can't punch the hitbox from top down sometimes when the creative player can???
		 * oh well. why would you punch the boat from top down anyway. it works from the side consistently anyway.
		 */
		//System.out.println("fromRelRot = "+fromRelRot);
		//System.out.println("diff = "+diff);
		//System.out.println("extents = "+extents);
		Double clipY = clipAxis(fromRelRot.y, toRelRot.y, extents.y);
		if (clipY != null) {
			double t = (clipY - fromRelRot.y) / diff.y;
			clipRelRot = fromRelRot.add(diff.scale(t));
			clipRelRot = new Vec3(clipRelRot.x, clipY, clipRelRot.z);
			if (t < tMin && containsRelRot(clipRelRot)) tMin = t;
			//System.out.println("clipRelRot = "+clipRelRot);
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
	
	public Vec3 getPushOutPos(Vec3 pos, AABB aabb) {
		Vec3 close = UtilGeometry.getClosestPointOnAABB(center, aabb);
		Vec3 aabbDiff = pos.subtract(close);
		//System.out.println("aabbDiff = "+aabbDiff);
		Vec3 push = getPushOutPos(close).add(aabbDiff);
		//System.out.println("push + aabbDiff = "+push);
		return push;
	}
	
	public Vec3 getPushOutPos(Vec3 pos) {
		Vec3 posRelRot = toRelRotPos(pos);
		if (!isInsideRelPos(posRelRot)) return pos;
		double distSqrMin = Double.MAX_VALUE;
		Vec3 pushRelRot = Vec3.ZERO;
		Double pushY = pushAxis(posRelRot.y, extents.y, 0.002);
		if (pushY != null) {
			Vec3 test = new Vec3(posRelRot.x, pushY, posRelRot.z);
			double distSqr = posRelRot.distanceToSqr(test);
			//System.out.println("distSqr pushY = "+distSqr);
			if (distSqr < distSqrMin) {
				distSqrMin = distSqr;
				pushRelRot = test;
			}
		}
		Double pushX = pushAxis(posRelRot.x, extents.x, 0.002);
		if (pushX != null) {
			Vec3 test = new Vec3(pushX, posRelRot.y, posRelRot.z);
			double distSqr = posRelRot.distanceToSqr(test);
			//System.out.println("distSqr pushX = "+distSqr);
			if (distSqr < distSqrMin) {
				distSqrMin = distSqr;
				pushRelRot = test;
			}
		}
		Double pushZ = pushAxis(posRelRot.z, extents.z, 0.002);
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
	
	public EntityDimensions getMaxDimensions() {
		float x = (float) extents.x(), y = (float) extents.y(), z = (float) extents.z();
		float max = Mth.sqrt(x*x+y*y+z*z);
		return EntityDimensions.scalable(max*2, max*2);
	}
	
	public DisguisedAABB getDisguisedAABB(Vec3 pos) {
		return new DisguisedAABB(this, pos, 0.5);
	}
	
	public AABB getMaxDimBox() {
		EntityDimensions d = getMaxDimensions();
    	double pX = center.x, pY = center.y, pZ = center.z;
    	double f = d.width / 2.0F;
        double f1 = d.height / 2.0F;
        return new AABB(pX-f, pY-f1, pZ-f, 
        		pX+f, pY+f1, pZ+f);
	}
	
}
