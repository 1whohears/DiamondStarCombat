package com.onewhohears.dscombat.data;

import java.util.Random;

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
	
	public static BulletData basic() {
		return new BulletData("basic", Vec3.ZERO, 600, 10, 1, 1, 1, 0);
	}
	
	private float damage;
	private double speed;
	private boolean explosive;
	private boolean destroyTerrain;
	private boolean causesFire;
	private double explosiveDamage; // TODO doesn't do anything
	private float explosionRadius;
	private float innacuracy;
	
	public BulletData(String id, Vec3 launchPos, int maxAge, int maxAmmo, int fireRate, 
			float damage, double speed, float innacuracy) {
		super(id, launchPos, maxAge, maxAmmo, fireRate);
		this.damage = damage;
		this.speed = speed;
		this.innacuracy = innacuracy;
	}
	
	public BulletData(String id, Vec3 launchPos, int maxAge, int maxAmmo, int fireRate, 
			float damage, double speed, float innacuracy, boolean explosive, boolean destroyTerrain, 
			boolean causesFire, double explosiveDamage, float explosionRadius) {
		this(id, launchPos, maxAge, maxAmmo, fireRate, damage, speed, innacuracy);
		this.explosive = explosive;
		this.destroyTerrain = destroyTerrain;
		this.causesFire = causesFire;
		this.explosiveDamage = explosiveDamage;
		this.explosionRadius = explosionRadius;
	}
	
	public BulletData(CompoundTag tag) {
		super(tag);
		this.damage = tag.getFloat("damage");
		this.speed = tag.getDouble("speed");
		this.explosive = tag.getBoolean("explosive");
		this.destroyTerrain = tag.getBoolean("destroyTerrain");
		this.causesFire = tag.getBoolean("causesFire");
		this.explosiveDamage = tag.getDouble("explosiveDamage");
		this.explosionRadius = tag.getFloat("explosionRadius");
		this.innacuracy = tag.getFloat("innacuracy");
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putFloat("damage", damage);
		tag.putDouble("speed", speed);
		tag.putBoolean("explosive", explosive);
		tag.putBoolean("destroyTerrain", destroyTerrain);
		tag.putBoolean("causesFire", causesFire);
		tag.putDouble("explosiveDamage", explosiveDamage);
		tag.putFloat("explosionRadius", explosionRadius);
		tag.putFloat("innacuracy", innacuracy);
		return tag;
	}
	
	public BulletData(FriendlyByteBuf buffer) {
		super(buffer);
		this.damage = buffer.readFloat();
		this.speed = buffer.readDouble();
		this.explosive = buffer.readBoolean();
		this.destroyTerrain = buffer.readBoolean();
		this.causesFire = buffer.readBoolean();
		this.explosiveDamage = buffer.readDouble();
		this.explosionRadius = buffer.readFloat();
		this.innacuracy = buffer.readFloat();
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeFloat(damage);
		buffer.writeDouble(speed);
		buffer.writeBoolean(explosive);
		buffer.writeBoolean(destroyTerrain);
		buffer.writeBoolean(causesFire);
		buffer.writeDouble(explosiveDamage);
		buffer.writeFloat(explosionRadius);
		buffer.writeFloat(innacuracy);
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.BULLET;
	}

	@Override
	public EntityAbstractWeapon shoot(Level level, Entity vehicle, Entity owner, Vec3 direction, Quaternion vehicleQ) {
		if (!this.checkRecoil()) {
			this.setLaunchFail(null);
			return null;
		}
		if (!this.checkShoot(1)) {
			this.setLaunchFail("not enough ammo");
			return null;
		}
		System.out.println(this.getId()+" ammo "+this.getCurrentAmmo());
		EntityBullet bullet = new EntityBullet(level, owner, this);
		Random r = new Random();
		float pitch = UtilAngles.getPitch(direction);
		float yaw = UtilAngles.getYaw(direction);
		pitch = pitch + (r.nextFloat()-0.5f) * 2f * innacuracy;
		yaw = yaw + (r.nextFloat()-0.5f) * 2f * innacuracy;
		bullet.setXRot(pitch);
		bullet.setYRot(yaw);
		direction = UtilAngles.rotationToVector(yaw, pitch);
		bullet.setPos(vehicle.position()
				.add(UtilAngles.rotateVector(this.getLaunchPos(), vehicleQ)));
		bullet.setDeltaMovement(direction.scale(speed).add(vehicle.getDeltaMovement()));
		level.addFreshEntity(bullet);
		this.setLaunchSuccess(1);
		return bullet;
	}
	
	public float getDamage() {
		return damage;
	}
	
	public double getSpeed() {
		return speed;
	}

	public boolean isExplosive() {
		return explosive;
	}

	public boolean isDestroyTerrain() {
		return destroyTerrain;
	}

	public double getExplosiveDamage() {
		return explosiveDamage;
	}

	public float getExplosionRadius() {
		return explosionRadius;
	}

	public boolean isCausesFire() {
		return causesFire;
	}

	public float getInnacuracy() {
		return innacuracy;
	}

}
