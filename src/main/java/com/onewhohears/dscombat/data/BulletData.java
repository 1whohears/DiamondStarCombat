package com.onewhohears.dscombat.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class BulletData extends WeaponData {
	
	private float damage;
	private double speed;
	
	public static BulletData getDefault() {
		return new BulletData(null, Vec3.ZERO, 1, 1);
	}
	
	public BulletData(String id, Vec3 launchPos, float damage, double speed) {
		super(id, launchPos);
		this.damage = damage;
		this.speed = speed;
	}
	
	public BulletData(CompoundTag tag) {
		super(tag);
		this.damage = tag.getFloat("damage");
		this.speed = tag.getDouble("speed");
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putFloat("damage", damage);
		tag.putDouble("speed", speed);
		return tag;
	}
	
	public BulletData(FriendlyByteBuf buffer) {
		super(buffer);
		this.damage = buffer.readFloat();
		this.speed = buffer.readDouble();
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeFloat(damage);
		buffer.writeDouble(speed);
	}
	
	@Override
	public WeaponData copy() {
		return new BulletData(this.getId(), this.getLaunchPos(), 
				this.getDamage(), this.getSpeed());
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.BULLET;
	}
	
	public float getDamage() {
		return damage;
	}
	
	public double getSpeed() {
		return speed;
	}

}
