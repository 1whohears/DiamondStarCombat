package com.onewhohears.dscombat.data;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.entity.weapon.EntityAbstractWeapon;
import com.onewhohears.dscombat.entity.weapon.EntityBullet;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BulletData extends WeaponData {
	
	private float damage;
	private double speed;
	
	public BulletData(String id, Vec3 launchPos, int maxAge, float damage, double speed) {
		super(id, launchPos, maxAge);
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
	public WeaponType getType() {
		return WeaponType.BULLET;
	}
	
	public float getDamage() {
		return damage;
	}
	
	public double getSpeed() {
		return speed;
	}

	@Override
	public EntityAbstractWeapon shoot(Level level, Entity vehicle, Entity owner, Vec3 direction, Quaternion vehicleQ) {
		EntityBullet bullet = new EntityBullet(level, owner, this);
		bullet.setPos(vehicle.position().add(UtilAngles.rotateVector(direction, vehicleQ)));
		bullet.setDeltaMovement(direction.scale(speed).add(vehicle.getDeltaMovement()));
		level.addFreshEntity(bullet);
		return bullet;
	}

}
