package com.onewhohears.dscombat.util.math;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

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
	
	public Vec3 getCollideMovePos(Vec3 entity_pos, Vec3 entity_move, Quaternion rot, Vec3 parent_move, Vec3 rot_rate) {
		Vector3f collide = getCollideMovePos(
				UtilGeometry.convertVector(entity_pos), UtilGeometry.convertVector(entity_move),
				rot, UtilGeometry.convertVector(parent_move), UtilGeometry.convertVector(rot_rate));
		if (collide == null) return null;
		return UtilGeometry.convertVector(collide);
	}
	
	@Nullable
	public Vector3f getCollideMovePos(Vector3f entity_pos, Vector3f entity_move, Quaternion rot, Vector3f parent_move, Vector3f rot_rate) {
		Vector3f rel_rot_collide = getRelRotCollidePos(entity_pos, entity_move, rot);
		if (rel_rot_collide == null) return null;
		Vector3f rel_tan_vel = rot_rate.copy();
		rel_tan_vel.mul(Mth.DEG_TO_RAD);
		rel_tan_vel.mul(-1,-1,1);
		rel_tan_vel.cross(rel_rot_collide);
		Vector3f tan_vel = rel_tan_vel.copy(); tan_vel.transform(rot);
		Vector3f collide = rel_rot_collide.copy(); collide.transform(rot);
		System.out.println("rel_rot_collide = "+rel_rot_collide);
		System.out.println("rel_collide = "+collide);
		collide.add(getCenter());
		collide.add(tan_vel);
		collide.add(parent_move);
		System.out.println("tan_vel = "+tan_vel);
		System.out.println("parent_move = "+parent_move);
		System.out.println("collide = "+collide);
		return collide;
	}
	
	@Nullable
	private Vector3f getRelRotCollidePos(Vector3f entity_pos, Vector3f entity_move, Quaternion rot) {
		Vector3f dc = entity_pos.copy();
		dc.sub(getCenter());
		Quaternion roti = rot.copy(); roti.conj();
		Vector3f rot_dc = dc.copy(); rot_dc.transform(roti);
		Vector3f rot_move = entity_move.copy(); rot_move.transform(roti);
		System.out.println("center = "+getCenter());
		System.out.println("entity_pos = "+entity_pos);
		System.out.println("entity_move = "+entity_move);
		System.out.println("dc = "+dc);
		System.out.println("rot = "+rot);
		System.out.println("rot_dc = "+rot_dc);
		System.out.println("rot_move = "+rot_move);
		return collideRelRotComponents(rot_dc, rot_move);
	}
	
	@Nullable
	private Vector3f collideRelRotComponents(Vector3f rot_dc, Vector3f rot_move) {
		Vector3f ext = getExtents();
		float cx = collideRelRotComponent(rot_dc.x(), rot_move.x(), ext.x(), false);
		if (Float.isNaN(cx)) return null;
		float cy = collideRelRotComponent(rot_dc.y(), rot_move.y(), ext.y(), true);
		if (Float.isNaN(cy)) return null;
		float cz = collideRelRotComponent(rot_dc.z(), rot_move.z(), ext.z(), false);
		if (Float.isNaN(cz)) return null;
		return new Vector3f(cx, cy, cz);
	}
	
	private float collideRelRotComponent(float rel_rot_pos, float rot_move, float ext, boolean push) {
		if (rel_rot_pos >= 0) {
			double de = ext - rel_rot_pos;
			if (de >= 0) {
				if (push) return ext; // inside the collider so push back
				return rel_rot_pos; // inside but don't move (xz axis)
			} 
			if (rot_move <= de) return ext; // outside but would move through the collider
		} else {
			double de = -ext - rel_rot_pos;
			if (de <= 0) {
				if (push) return -ext; // inside the collider so push back
				return rel_rot_pos; // inside but don't move (xz axis)
			}
			if (rot_move >= de) return -ext; // outside but would move through the collider
		}
		return Float.NaN;
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
		float max = (float)Math.max(extents.x(), Math.max(extents.y(), extents.z()));
		return EntityDimensions.scalable(max*2, max*2);
	}
	
}
