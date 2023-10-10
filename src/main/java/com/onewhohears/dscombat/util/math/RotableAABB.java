package com.onewhohears.dscombat.util.math;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RotableAABB {
	
	private Vec3 center, extents;
	
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
	
	@Nullable
	public Vec3 getCollideMovePos(Vec3 entity_pos, Vec3 entity_move, Quaternion rot, Vec3 parent_move, Vec3 rot_rate) {
		Vec3 rel_rot_collide = getRelRotCollidePos(entity_pos, entity_move, rot);
		if (rel_rot_collide == null) return null;
		Vec3 rel_collide = UtilAngles.rotateVector(rel_rot_collide, rot);
		Vec3 rel_tan_vel = rot_rate.scale(Mth.DEG_TO_RAD).multiply(-1,-1,1).cross(rel_rot_collide);
		Vec3 tan_vel = UtilAngles.rotateVector(rel_tan_vel, rot);
		return rel_collide.add(getCenter()).add(tan_vel).add(parent_move);
	}
	
	@Nullable
	private Vec3 getRelRotCollidePos(Vec3 entity_pos, Vec3 entity_move, Quaternion rot) {
		Vec3 dc = entity_pos.subtract(getCenter());
		Vec3 rot_dc = UtilAngles.rotateVectorInverse(dc, rot);
		Vec3 rot_move = UtilAngles.rotateVectorInverse(entity_move, rot);
		return collideRelRotComponents(rot_dc, rot_move);
	}
	
	@Nullable
	private Vec3 collideRelRotComponents(Vec3 rot_dc, Vec3 rot_move) {
		Vec3 ext = getExtents();
		double cx = collideRelRotComponent(rot_dc.x, rot_move.x, ext.x, false);
		if (Double.isNaN(cx)) return null;
		double cy = collideRelRotComponent(rot_dc.y, rot_move.y, ext.y, true);
		if (Double.isNaN(cy)) return null;
		double cz = collideRelRotComponent(rot_dc.z, rot_move.z, ext.z, false);
		if (Double.isNaN(cz)) return null;
		return new Vec3(cx, cy, cz);
	}
	
	private double collideRelRotComponent(double rel_rot_pos, double rot_move, double ext, boolean push) {
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
		return Double.NaN;
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

	public void move(Vec3 offset) {
		setCenter(getCenter().add(offset));
	}
	
	public EntityDimensions getMaxDimensions() {
		float max = (float)Math.max(extents.x, Math.max(extents.y, extents.z));
		return EntityDimensions.scalable(max*2, max*2);
	}
	
}
