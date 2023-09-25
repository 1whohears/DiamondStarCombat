package com.onewhohears.dscombat.util.math;

import com.mojang.math.Quaternion;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RotableAABB {
	
	private Vec3 center, extents;
	private Quaternion rot;
	
	public RotableAABB(AABB bb) {
		this(bb.getCenter(), extentsFromBB(bb), Quaternion.ONE.copy());
	}
	
	public RotableAABB() {
		this(Vec3.ZERO, Vec3.ZERO, Quaternion.ONE.copy());
	}
	
	public RotableAABB(double width, double height, double length) {
		this(Vec3.ZERO, new Vec3(width/2, height/2, length/2), Quaternion.ONE.copy());
	}
	
	public RotableAABB(Vec3 center, Vec3 extents, Quaternion rot) {
		this.center = center;
		this.extents = extents;
		this.rot = rot;
	}
	
	public RotableAABB copy() {
		return new RotableAABB(center, extents, rot);
	}
	
	public static Vec3 extentsFromBB(AABB bb) {
		return new Vec3(bb.getXsize() / 2, bb.getYsize() / 2, bb.getZsize() / 2);
	}
	
	public Quaternion getRot() {
		return rot;
	}

	public void setRot(Quaternion rot) {
		this.rot = rot;
	}

	public Vec3 getCenter() {
		return center;
	}

	public void setCenter(Vec3 center) {
		this.center = center;
	}

	public void move(Vec3 offset) {
		setCenter(getCenter().add(offset));
	}
	
}
