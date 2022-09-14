package com.onewhohears.dscombat.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class BulletData extends WeaponData {
	
	private float damage;
	private double speed;
	
	public static BulletData getDefault() {
		return new BulletData(1, 1);
	}
	
	public BulletData(float damage, double speed) {
		this.damage = damage;
		this.speed = speed;
	}
	
	public BulletData(CompoundTag tag) {
		this.damage = tag.getFloat("damage");
		this.speed = tag.getDouble("speed");
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putFloat("damage", damage);
		tag.putDouble("speed", speed);
		return tag;
	}
	
	public BulletData(FriendlyByteBuf buffer) {
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
		return new BulletData(damage, speed);
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.BULLET;
	}

	@Override
	public float getDamage() {
		return damage;
	}

	@Override
	public double getSpeed() {
		return speed;
	}

}
