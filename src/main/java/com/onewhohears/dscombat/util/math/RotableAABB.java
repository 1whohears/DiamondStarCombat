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
	public Vec3 getFeetCollidePos(double skin, AABB bb, Vec3 entity_move, Vec3 entity_pos, Vec3 parent_move, Vec3 rot_rate, Quaternion rot) {
		Vec3 bb_bottom = UtilGeometry.getBBFeet(bb);
		Vec3 dc = bb_bottom.subtract(getCenter());
		Vec3 rot_dc = UtilAngles.rotateVectorInverse(dc, rot);
		Vec3 ext = getExtents();
		boolean isXZCollide = rot_dc.x<ext.x && rot_dc.x>-ext.x
						   && rot_dc.z<ext.z && rot_dc.z>-ext.z;
					     //&& rot_dc.y<ext.y+skin && rot_dc.y>-ext.y;
		if (!isXZCollide) return null;
		Vec3 rel_rot_collide = new Vec3(rot_dc.x, ext.y, rot_dc.z);
		Vec3 rel_collide = UtilAngles.rotateVector(rel_rot_collide, rot);
		double collideY = getCenter().y+rel_collide.y;
		double dcy = collideY-bb_bottom.y;
		System.out.println("dcy = "+dcy+" y_move = "+entity_move.y);
		if (Math.abs(dcy) > skin) {
			if (entity_move.y == 0) return null;
			if (Math.signum(dcy) != Math.signum(entity_move.y)) return null;
			if (entity_move.y < 0 && dcy < entity_move.y) return null;
			if (entity_move.y > 0 && dcy > entity_move.y) return null;
		}
		Vec3 collide = new Vec3(bb_bottom.x, collideY, bb_bottom.z);
		double dy = entity_pos.y-bb_bottom.y;
		collide = collide.add(0, dy, 0);
		Vec3 rel_tan_vel = rot_rate.scale(Mth.DEG_TO_RAD).multiply(-1,-1,1).cross(rot_dc);
		Vec3 tan_vel = UtilAngles.rotateVector(rel_tan_vel, rot);
		return collide.add(tan_vel).add(parent_move);
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
