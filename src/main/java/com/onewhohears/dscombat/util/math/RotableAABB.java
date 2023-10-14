package com.onewhohears.dscombat.util.math;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RotableAABB {
	
	private Vector3f center, extents;
	
	public RotableAABB(AABB bb) {
		this(UtilGeometry.convertVector(bb.getCenter()), extentsFromBB(bb));
	}
	
	public RotableAABB() {
		this(Vector3f.ZERO, Vector3f.ZERO);
	}
	
	public RotableAABB(float width, float height, float length) {
		this(Vector3f.ZERO, new Vector3f(width/2, height/2, length/2));
	}
	
	public RotableAABB(Vector3f center, Vector3f extents) {
		this.center = center;
		this.extents = extents;
	}
	
	public RotableAABB copy() {
		return new RotableAABB(getCenter(), getExtents());
	}
	
	public static class CollisionData {
		public Direction dir = Direction.NORTH;
		public Vector3f collide_direction = new Vector3f();
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
		Vector3f diff = collide.copy(); diff.sub(entity_pos);
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
		// need to check which move components are zero then rot that collision direction 
		entity_move.transform(rot);
		data.collide_direction.transform(rot);
		data.dir = Direction.getNearest(data.collide_direction.x(), data.collide_direction.y(), data.collide_direction.z());
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
			/*System.out.println("dx = "+dx);
			System.out.println("dy = "+dy);
			System.out.println("dz = "+dz);*/
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
		data.collide_direction.set(0, 0, Math.signum(cz));
		rot_move.mul(1, 1, 0);
		return new Vector3f(rel_rot_pos.x(), rel_rot_pos.y(), cz);
	}
	
	private Vector3f getXCollide(Vector3f rel_rot_pos, Vector3f rot_move, float cx, CollisionData data) {
		data.collide_direction.set(Math.signum(cx), 0, 0);
		rot_move.mul(0, 1, 1);
		return new Vector3f(cx, rel_rot_pos.y(), rel_rot_pos.z());
	}
	
	private Vector3f getYCollide(Vector3f rel_rot_pos, Vector3f rot_move, float cy, CollisionData data) {
		data.collide_direction.set(0, Math.signum(cy), 0);
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
	}
	
	public static Vector3f extentsFromBB(AABB bb) {
		return new Vector3f((float)bb.getXsize()/2, (float)bb.getYsize()/2, (float)bb.getZsize()/2);
	}

	public Vector3f getCenter() {
		return center;
	}

	public void setCenter(Vector3f center) {
		this.center = center;
	}
	
	public Vector3f getExtents() {
		return extents;
	}
	
	public EntityDimensions getMaxDimensions() {
		float x = extents.x(), y = extents.y(), z = extents.z();
		float max = Mth.sqrt(x*x+y*y+z*z);
		return EntityDimensions.scalable(max*2, max*2);
	}
	
}
