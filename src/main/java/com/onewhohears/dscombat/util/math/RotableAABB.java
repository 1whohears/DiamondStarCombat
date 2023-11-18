package com.onewhohears.dscombat.util.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RotableAABB {
	
	public static final double SUBSIZE = 0.5;
	public static final double SUBSIZEHALF = SUBSIZE*0.5;
	public static int subUpdateCounter;
	private Vec3 center, extents;
	private Quaternion rot = Quaternion.ONE.copy(), roti = Quaternion.ONE.copy();
	private final List<VoxelShape> subColliders = new ArrayList<>();
	//private boolean updatedSubs = false;
	
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
	
	public void updateCollider(Vec3 center, Quaternion q) {
		//if (!center.equals(this.center) || !q.equals(this.rot)) updatedSubs = false;
		//subColliders.clear();
		setCenter(center);
		setRot(q);
	}
	
	public void addColliders(List<VoxelShape> colliders, AABB aabb) {
		//System.out.println("ADDING SUB COLLIDERS "+getSubColliders().size());
		updateSubColliders(aabb);
		colliders.addAll(getSubColliders());
		//VoxelShape shape = getCollide(aabb, data);
		//if (shape != null) colliders.add(shape);
	}
	
	/*@Nullable
	public VoxelShape getCollide(AABB aabb, CollisionData data) {
		Vec3 close = UtilGeometry.getClosestPointOnAABB(center, aabb);
		Vec3 diff = close.subtract(center);
		Vec3 dir = diff.normalize();
		Quaternion roti = rot.copy();
		roti.conj();
		Vec3 diffRot = UtilAngles.rotateVector(diff, roti);
		System.out.println("diff = "+diff);
		System.out.println("diffRot = "+diffRot);
		boolean insideX = diffRot.x() < extents.x() && diffRot.x() > -extents.x();
		boolean insideY = diffRot.y() < extents.y() && diffRot.y() > -extents.y();
		boolean insideZ = diffRot.z() < extents.z() && diffRot.z() > -extents.z();
		double dex = diffRot.x, dey = diffRot.y, dez = diffRot.z;
		if (insideX && insideY) {
			double sign = Math.signum(diffRot.z);
			dez = extents.z*sign;
			data.normal = Vec3.ZERO.add(0, 0, sign);
		} else if (insideZ && insideY) {
			double sign = Math.signum(diffRot.x);
			dex = extents.x*sign;
			data.normal = Vec3.ZERO.add(sign, 0, 0);
		} else if (insideX && insideZ) {
			double sign = Math.signum(diffRot.y);
			dey = extents.y*sign;
			data.normal = Vec3.ZERO.add(0, sign, 0);
		
		}
		Vec3 diffEdgeRot = new Vec3(dex,dey,dez);
		Vec3 diffEdge = UtilAngles.rotateVector(diffEdgeRot, rot);
		System.out.println("diffEdge = "+diffEdge);
		Vec3 edge = diffEdge.add(center);
		//Vec3 dirRot = UtilAngles.rotateVector(dir, roti);
		//Vec3 edgeRot = dirRot.multiply(extents);
		//Vec3 edge = UtilAngles.rotateVector(edgeRot, rot).scale(1.01).add(center);
		EulerAngles a = UtilAngles.toRadians(rot);
		Vec3 xStep = UtilAngles.getPitchAxis(a.pitch, a.yaw, a.roll);
		Vec3 yStep = UtilAngles.getYawAxis(a.pitch, a.yaw, a.roll);
		Vec3 zStep = UtilAngles.getRollAxis(a.pitch, a.yaw);
		Optional<Vec3> clip = aabb.clip(center, edge);
		System.out.println("CLIP edge "+edge+" "+clip.isPresent());
		Vec3 pos;
		if (clip.isEmpty()) pos = edge.subtract(dir.scale(SUBSIZEHALF));
		else pos = clip.get().subtract(dir.scale(SUBSIZEHALF));
		data.normal = UtilAngles.rotateVector(data.normal, rot);
		data.dir = Direction.getNearest(data.normal.x(), data.normal.y(), data.normal.z());
		Vec3 pos = edge.subtract(dir.scale(SUBSIZEHALF));
		return Shapes.create(
			pos.x-SUBSIZEHALF, pos.y-SUBSIZEHALF, pos.z-SUBSIZEHALF, 
			pos.x+SUBSIZEHALF, pos.y+SUBSIZEHALF, pos.z+SUBSIZEHALF);
	}*/
	
	public List<VoxelShape> getSubColliders() {
		//if (!updatedSubs) updateSubColliders();
		return subColliders;
	}
	
	public void updateSubColliders(AABB aabb) {
		subColliders.clear();
		Vec3 startNoRot = extents.subtract(SUBSIZEHALF, SUBSIZEHALF, SUBSIZEHALF);
		Vec3 start = toWorldPos(startNoRot.scale(-1));
		Vec3 steps =  startNoRot.scale(2/SUBSIZE);
		int xSteps = (int)Math.ceil(steps.x), ySteps = (int)Math.ceil(steps.y), zSteps = (int)Math.ceil(steps.z);
		double xScale = steps.x / (double)xSteps * SUBSIZE;
		double yScale = steps.y / (double)ySteps * SUBSIZE;
		double zScale = steps.z / (double)zSteps * SUBSIZE;
		EulerAngles a = UtilAngles.toRadians(rot);
		Vec3 xStep = UtilAngles.getPitchAxis(a.pitch, a.yaw, a.roll).scale(xScale);
		Vec3 yStep = UtilAngles.getYawAxis(a.pitch, a.yaw, a.roll).scale(yScale);
		Vec3 zStep = UtilAngles.getRollAxis(a.pitch, a.yaw).scale(zScale);
		Vec3 close = UtilGeometry.getClosestPointOnAABB(center, aabb);
		double max = 0.81;
		// FIXME 4.1 another optimization is reducing the number of loop iterations. 
		// big hit boxes have a lot of steps. so more smaller hit boxes might be the way.
		for(int i = 0; i <= xSteps; ++i) for(int j = 0; j <= ySteps; ++j) {
			Vec3 pos = start.add(xStep.scale(i)).add(yStep.scale(j));
			if (pos.distanceToSqr(close) <= max) addShape(pos);
			pos = start.add(xStep.scale(i)).add(yStep.scale(j)).add(zStep.scale(zSteps));
			if (pos.distanceToSqr(close) <= max) addShape(pos);
		}
		for(int j = 0; j <= ySteps; ++j) for(int k = 0; k <= zSteps; ++k) {
			Vec3 pos = start.add(zStep.scale(k)).add(yStep.scale(j));
			if (pos.distanceToSqr(close) <= max) addShape(pos);
			pos = start.add(zStep.scale(k)).add(yStep.scale(j)).add(xStep.scale(xSteps));
			if (pos.distanceToSqr(close) <= max) addShape(pos);
		}
		for(int i = 0; i <= xSteps; ++i) for(int k = 0; k <= zSteps; ++k) {
			Vec3 pos = start.add(xStep.scale(i)).add(zStep.scale(k));
			if (pos.distanceToSqr(close) <= max) addShape(pos);
			pos = start.add(xStep.scale(i)).add(zStep.scale(k)).add(yStep.scale(ySteps));
			if (pos.distanceToSqr(close) <= max) addShape(pos);
		}
		// FIXME 4.2 players are frozen stuck when inside a vehicle
		/*for(int i = 0; i <= xSteps; ++i) for(int j = 0; j <= ySteps; ++j) for(int k = 0; k <= zSteps; ++k) {
			if (i!=0&&i!=xSteps && j!=0&&j!=ySteps && k!=0&&k!=zSteps) continue;
			Vec3 pos = start.add(xStep.scale(i)).add(yStep.scale(j)).add(zStep.scale(k));
			if (pos.distanceToSqr(close) > 0.25) continue;
			VoxelShape shape = Shapes.create(
				pos.x-SUBSIZEHALF, pos.y-SUBSIZEHALF, pos.z-SUBSIZEHALF, 
				pos.x+SUBSIZEHALF, pos.y+SUBSIZEHALF, pos.z+SUBSIZEHALF);
			subColliders.add(shape);
		}*/
		//updatedSubs = true;
		if (subColliders.size() > 0) ++subUpdateCounter; 
		//System.out.println("UPDATED SUB COLLIDERS "+(++subUpdateCounter)+" "+subColliders.size());
	}
	
	private void addShape(Vec3 pos) {
		VoxelShape shape = Shapes.create(
			pos.x-SUBSIZEHALF, pos.y-SUBSIZEHALF, pos.z-SUBSIZEHALF, 
			pos.x+SUBSIZEHALF, pos.y+SUBSIZEHALF, pos.z+SUBSIZEHALF);
		subColliders.add(shape);
	}
	
	/*public static class CollisionData {
		public Direction dir = Direction.NORTH;
		public Vec3 normal = Vec3.ZERO;
	}*/
	
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
		Vec3 fromRelRot = toRelRotPos(from);
		if (containsRelRot(fromRelRot)) return Optional.of(toWorldPos(fromRelRot));
		Vec3 toRelRot = toRelRotPos(to);
		Vec3 diff = toRelRot.subtract(fromRelRot);
		double tMin = Double.MAX_VALUE;
		Vec3 clipRelRot = Vec3.ZERO;
		// FIXME 4.5 clip logic is better but something is still wrong
		System.out.println("fromRelRot = "+fromRelRot);
		System.out.println("diff = "+diff);
		System.out.println("extents = "+extents);
		Double clipY = clipAxis(fromRelRot.y, toRelRot.y, extents.y);
		if (clipY != null) {
			double t = (clipY - fromRelRot.y) / diff.y;
			clipRelRot = fromRelRot.add(diff.scale(t));
			clipRelRot = new Vec3(clipRelRot.x, clipY, clipRelRot.z);
			if (t < tMin && containsRelRot(clipRelRot)) tMin = t;
			System.out.println("clipRelRot = "+clipRelRot);
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
	
	/*@Nullable
	public Vec3 getCollidePos(Vec3 entity_pos, Vector3f entity_move, Quaternion rot, CollisionData data) {
		Vector3f collide = getCollidePos(UtilGeometry.convertVector(entity_pos), entity_move, rot, data);
		if (collide == null) return null;
		return UtilGeometry.convertVector(collide);
	}
	
	@Nullable
	public Vector3f getCollidePos(Vector3f entity_pos, Vector3f entity_move, Quaternion rot, CollisionData data) {
		Vector3f rel_rot_collide = getRelRotCollidePos(entity_pos, entity_move, rot, data);
		if (rel_rot_collide == null) return null;
		Vector3f collide = UtilAngles.rotateVector(rel_rot_collide, rot);
		collide.add(getCenter());
		return collide;
	}
	
	@Nullable
	public Vec3 getCollideMovePos(Vec3 entity_pos, Vector3f entity_move, Quaternion rot, Vec3 parent_move, Vec3 rot_rate, CollisionData data) {
		Vector3f collide = getCollideMovePos(UtilGeometry.convertVector(entity_pos), entity_move,
				rot, UtilGeometry.convertVector(parent_move), UtilGeometry.convertVector(rot_rate), data);
		if (collide == null) return null;
		return UtilGeometry.convertVector(collide);
	}
	
	@Nullable
	public Vector3f getCollideMovePos(Vector3f entity_pos, Vector3f entity_move, Quaternion rot, Vector3f parent_move, Vector3f rot_rate, CollisionData data) {
		Vector3f rel_rot_collide = getRelRotCollidePos(entity_pos, entity_move, rot, data);
		if (rel_rot_collide == null) return null;
		Vector3f rel_tan_vel = rot_rate.copy();
		rel_tan_vel.mul(Mth.DEG_TO_RAD);
		rel_tan_vel.mul(-1,-1,1);
		rel_tan_vel.cross(rel_rot_collide);
		Vector3f tan_vel = UtilAngles.rotateVector(rel_tan_vel, rot);
		Vector3f collide = UtilAngles.rotateVector(rel_rot_collide, rot);
		//System.out.println("rel_rot_collide = "+rel_rot_collide);
		//System.out.println("rel_collide     = "+collide);
		collide.add(getCenter());
		collide.add(tan_vel);
		collide.add(parent_move);
		//System.out.println("tan_vel     = "+tan_vel);
		//System.out.println("parent_move = "+parent_move);
		//System.out.println("collide = "+collide);
		//Vector3f diff = collide.copy(); diff.sub(entity_pos);
		//System.out.println("diff = "+diff);
		return collide;
	}
	
	@Nullable
	private Vector3f getRelRotCollidePos(Vector3f entity_pos, Vector3f entity_move, Quaternion rot, CollisionData data) {
		Vector3f dc = entity_pos.copy();
		dc.sub(getCenter());
		Quaternion roti = rot.copy(); roti.conj();
		Vector3f rel_rot_pos = UtilAngles.rotateVector(dc, roti);
		//Vector3f rot_move = UtilAngles.rotateVector(entity_move, roti);
		//System.out.println("center = "+getCenter());
		//System.out.println("rot = "+rot);
		//System.out.println("entity_pos = "+entity_pos);
		//System.out.println("dc     = "+dc);
		//System.out.println("rot_dc = "+rot_dc);
		//System.out.println("entity_move = "+entity_move);
		//System.out.println("rot_move = "+rot_move);
		entity_move.transform(roti);
		Vector3f crrc = collideRelRotComponents(rel_rot_pos, entity_move, data);
		if (crrc == null) return null;
		entity_move.transform(rot);
		data.normal.transform(rot);
		data.dir = Direction.getNearest(data.normal.x(), data.normal.y(), data.normal.z());
		return crrc;
	}
	
	@Nullable
	private Vector3f collideRelRotComponents(Vector3f rel_rot_pos, Vector3f rot_move, CollisionData data) {
		Vector3f ext = getExtents();
		boolean insideX = rel_rot_pos.x() < ext.x() && rel_rot_pos.x() > -ext.x();
		boolean insideY = rel_rot_pos.y() < ext.y() && rel_rot_pos.y() > -ext.y();
		boolean insideZ = rel_rot_pos.z() < ext.z() && rel_rot_pos.z() > -ext.z();
		if (insideX && insideY && insideZ) {
			float dx = Math.min(Mth.abs(ext.x()-rel_rot_pos.x()), Mth.abs(-ext.x()-rel_rot_pos.x()));
			float dy = Math.min(Mth.abs(ext.y()-rel_rot_pos.y()), Mth.abs(-ext.y()-rel_rot_pos.y()))+0.1f;
			float dz = Math.min(Mth.abs(ext.z()-rel_rot_pos.z()), Mth.abs(-ext.z()-rel_rot_pos.z()));
			if (isAbsSmallest(dz, dy, dx)) return getZCollide(rel_rot_pos, rot_move, ext.z()*Math.signum(rel_rot_pos.z()), data);
			else if (isAbsSmallest(dx, dy, dz)) return getXCollide(rel_rot_pos, rot_move, ext.x()*Math.signum(rel_rot_pos.x()), data);
			else if (isAbsSmallest(dy, dz, dx)) return getYCollide(rel_rot_pos, rot_move, ext.y()*Math.signum(rel_rot_pos.y()), data);
			else return getYCollide(rel_rot_pos, rot_move, dy*Math.signum(rel_rot_pos.y()), data);
		}
		Float cz = collideComponent(rel_rot_pos.z(), rot_move.z(), ext.z());
		if (cz != null && insideX && insideY) return getZCollide(rel_rot_pos, rot_move, cz, data);
		Float cx = collideComponent(rel_rot_pos.x(), rot_move.x(), ext.x());
		if (cx != null && insideY && insideZ) return getXCollide(rel_rot_pos, rot_move, cx, data);
		Float cy = collideComponent(rel_rot_pos.y(), rot_move.y(), ext.y());
		if (cy != null && insideX && insideZ) return getYCollide(rel_rot_pos, rot_move, cy, data);
		return null;
	}
	
	private Vector3f getZCollide(Vector3f rel_rot_pos, Vector3f rot_move, float cz, CollisionData data) {
		data.normal.set(0, 0, Math.signum(cz));
		rot_move.mul(1, 1, 0);
		return new Vector3f(rel_rot_pos.x(), rel_rot_pos.y(), cz);
	}
	
	private Vector3f getXCollide(Vector3f rel_rot_pos, Vector3f rot_move, float cx, CollisionData data) {
		data.normal.set(Math.signum(cx), 0, 0);
		rot_move.mul(0, 1, 1);
		return new Vector3f(cx, rel_rot_pos.y(), rel_rot_pos.z());
	}
	
	private Vector3f getYCollide(Vector3f rel_rot_pos, Vector3f rot_move, float cy, CollisionData data) {
		data.normal.set(0, Math.signum(cy), 0);
		rot_move.mul(1, 0, 1);
		return new Vector3f(rel_rot_pos.x(), cy, rel_rot_pos.z());
	}
	
	private boolean isAbsSmallest(Float a, Float b, Float c) {
		if (a == null || b == null || c == null) return false;
		return Mth.abs(a) < Mth.abs(b) && Mth.abs(a) < Mth.abs(c);
	}
	
	@Nullable
	private Float collideComponent(float rel_rot_pos, float rot_move, float ext) {
		if (rel_rot_pos >= ext) {
			float de = ext-rel_rot_pos;
			if (rot_move > de) return null;
			return ext;
		} else if (rel_rot_pos <= -ext) {
			float de = -ext-rel_rot_pos;
			if (rot_move < de) return null;
			return -ext;
		} else return null;
	}*/
	
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
	
}
