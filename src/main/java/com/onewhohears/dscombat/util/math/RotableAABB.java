package com.onewhohears.dscombat.util.math;

import java.util.ArrayList;
import java.util.List;

import com.mojang.math.Quaternion;

import net.minecraft.world.entity.EntityDimensions;
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
	
	public List<Vec3> createRelPosList(float precision) {
		double height = extents.y*2/precision;
		double length = extents.z*2/precision;
		double width = extents.x*2/precision;
		int hsteps = (int)Math.ceil(height);
		int lsteps = (int)Math.ceil(length);
		int wsteps = (int)Math.ceil(width);
		List<Vec3> list = new ArrayList<Vec3>();
		for(int h=0;h<hsteps;++h)for(int l=0;l<lsteps;++l)for(int w=0;w<wsteps;++w) {
			double hpos = h*precision-extents.y-(height-hsteps)/2;
			double lpos = l*precision-extents.z-(length-lsteps)/2;
			double wpos = w*precision-extents.x-(width-wsteps)/2;
			list.add(new Vec3(hpos, lpos, wpos));
		}
		return list;
	}
	
	public Vec3 repositionSubCollider(Vec3 rel_pos) {
		return UtilAngles.rotateVector(rel_pos, rot).add(getCenter());
	}
	
	public Vec3 getTangetVel(Vec3 rel_pos, Vec3 ang_vel) {
		
	}
	
	public static Vec3 extentsFromBB(AABB bb) {
		return new Vec3(bb.getXsize()/2, bb.getYsize()/2, bb.getZsize()/2);
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
	
	public EntityDimensions getMaxDimensions() {
		float max = (float)Math.max(extents.x, Math.max(extents.y, extents.z));
		return EntityDimensions.scalable(max, max);
	}
	
}
