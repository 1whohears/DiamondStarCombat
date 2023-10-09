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
	public Vec3 getCollidePos(AABB bb, double skin, Quaternion rot) {
		Vec3 bbbottom = UtilGeometry.getBBFeet(bb);
		Vec3 dc = bbbottom.subtract(getCenter());
		Vec3 rot_dc = UtilAngles.rotateVectorInverse(dc, rot);
		Vec3 ext = getExtents();
		boolean collide = rot_dc.x<ext.x && rot_dc.x>-ext.x
					   && rot_dc.y<ext.y+skin && rot_dc.y>-ext.y
					   && rot_dc.z<ext.z && rot_dc.z>-ext.z;
		if (!collide) return null;
		return new Vec3 (bbbottom.x, getCenter().y+ext.y, bbbottom.z);
	}
	
	public Vec3 getTangetVel(Vec3 pos, Vec3 rot_rate, Quaternion rot) {
		Vec3 dc = pos.subtract(getCenter());
		Vec3 rot_dc = UtilAngles.rotateVectorInverse(dc, rot);
		Vec3 rel_tan_vel = rot_rate.scale(Mth.DEG_TO_RAD).multiply(-1,-1,1).cross(rot_dc);
		return UtilAngles.rotateVector(rel_tan_vel, rot);
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
