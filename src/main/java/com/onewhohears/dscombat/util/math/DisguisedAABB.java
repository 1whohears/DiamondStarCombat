package com.onewhohears.dscombat.util.math;

import java.util.Optional;

import com.onewhohears.minigames.util.UtilParse;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class DisguisedAABB extends AABB {
	private final RotableAABB hitbox;
	public DisguisedAABB(RotableAABB hitbox, Vec3 pos, double radius) {
		super(pos.x-radius, pos.y-radius, pos.z-radius, 
				pos.x+radius, pos.y+radius, pos.z+radius);
		this.hitbox = hitbox;
	}
	@Override
	public Optional<Vec3> clip(Vec3 from, Vec3 to) {
		Optional<Vec3> clip = hitbox.clip(from, to);
		System.out.println("CLIP DisguisedAABB:"+clip.toString()+"FROM:"+UtilParse.prettyVec3(from)+"TO:"+UtilParse.prettyVec3(to));
		return clip;
	}
	@Override
	public boolean contains(Vec3 vec) {
		return hitbox.contains(vec);
	}
	@Override
	public boolean contains(double x, double y, double z) {
		return contains(new Vec3(x, y, z));
	}
	@Override
	public boolean intersects(AABB pOther) {
		return hitbox.isColliding(pOther);
	}
	@Override
	public boolean intersects(double pX1, double pY1, double pZ1, double pX2, double pY2, double pZ2) {
		return intersects(new AABB(pX1, pY1, pZ1, pX2, pY2, pZ2));
	}
	@Override
	public boolean intersects(Vec3 pMin, Vec3 pMax) {
		return intersects(new AABB(pMin, pMax));
	}
	@Override
	public String toString() {
		return "Disguised"+super.toString();
	}
	@Override
	public AABB setMinX(double pMinX) {
		return this;
	}
	@Override
	public AABB setMinY(double pMinY) {
		return this;
	}
	@Override
	public AABB setMinZ(double pMinZ) {
		return this;
	}
	@Override
	public AABB setMaxX(double pMaxX) {
		return this;
	}
	@Override
	public AABB setMaxY(double pMaxY) {
		return this;
	}
	@Override
	public AABB setMaxZ(double pMaxZ) {
		return this;
	}
	@Override
	public AABB contract(double pX, double pY, double pZ) {
		return this;
	}
	@Override
	public AABB expandTowards(Vec3 pVector) {
		return this;
	}
	@Override
	public AABB expandTowards(double pX, double pY, double pZ) {
		return this;
	}
	@Override
	public AABB inflate(double pX, double pY, double pZ) {
		return this;
	}
	@Override
	public AABB inflate(double pValue) {
		return this;
	}
	@Override
	public AABB intersect(AABB pOther) {
		return this;
	}
	@Override
	public AABB minmax(AABB pOther) {
		return this;
	}
	@Override
	public AABB move(double pX, double pY, double pZ) {
		return this;
	}
	@Override
	public AABB move(BlockPos pPos) {
		return this;
	}
	@Override
	public AABB move(Vec3 pVec) {
		return this;
	}
	@Override
	public AABB deflate(double pX, double pY, double pZ) {
		return this;
	}
	@Override
	public AABB deflate(double pValue) {
		return this;
	}
	@Override
	public Vec3 getCenter() {
		return hitbox.getCenter();
	}
}
