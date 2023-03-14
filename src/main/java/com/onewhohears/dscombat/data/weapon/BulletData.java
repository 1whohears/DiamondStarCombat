package com.onewhohears.dscombat.data.weapon;

import java.util.List;
import java.util.Random;

import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.EntityBullet;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BulletData extends WeaponData {
	
	private float damage;
	private double speed;
	private boolean explosive;
	private boolean destroyTerrain;
	private boolean causesFire;
	private float explosionRadius;
	private float innacuracy;
	
	public BulletData(CompoundTag tag) {
		super(tag);
		this.damage = tag.getFloat("damage");
		this.speed = tag.getDouble("speed");
		this.explosive = tag.getBoolean("explosive");
		this.destroyTerrain = tag.getBoolean("destroyTerrain");
		this.causesFire = tag.getBoolean("causesFire");
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
		tag.putFloat("explosionRadius", explosionRadius);
		tag.putFloat("innacuracy", innacuracy);
		return tag;
	}
	
	public BulletData(FriendlyByteBuf buffer) {
		super(buffer);
		//System.out.println("BULLET BUFFER");
		this.damage = buffer.readFloat();
		this.speed = buffer.readDouble();
		this.explosive = buffer.readBoolean();
		this.destroyTerrain = buffer.readBoolean();
		this.causesFire = buffer.readBoolean();
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
		buffer.writeFloat(explosionRadius);
		buffer.writeFloat(innacuracy);
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.BULLET;
	}
	
	public EntityWeapon getEntity(Level level, Entity owner) {
		return new EntityBullet(level, owner, this);
	}
	
	@Override
	public EntityWeapon getShootEntity(Level level, Entity owner, Vec3 pos, Vec3 direction) {
		EntityBullet bullet = (EntityBullet) super.getShootEntity(level, owner, pos, direction);
		if (bullet == null) return null;
		bullet.setDeltaMovement(direction.scale(speed));
		return bullet;
	}
	
	@Override
	public void setDirection(EntityWeapon weapon, Vec3 direction) {
		float pitch = UtilAngles.getPitch(direction);
		float yaw = UtilAngles.getYaw(direction);
		Random r = new Random();
		pitch = pitch + (r.nextFloat()-0.5f) * 2f * innacuracy;
		yaw = yaw + (r.nextFloat()-0.5f) * 2f * innacuracy;
		weapon.setXRot(pitch);
		weapon.setYRot(yaw);
		direction.subtract(direction).add(UtilAngles.rotationToVector(yaw, pitch));
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

	public float getExplosionRadius() {
		return explosionRadius;
	}

	public boolean isCausesFire() {
		return causesFire;
	}

	public float getInnacuracy() {
		return innacuracy;
	}

	@Override
	public WeaponData copy() {
		return new BulletData(this.write());
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		list.add(new ComponentColor(Component.literal("Damage: ").append(getDamage()+""), 0x040404));
		list.add(new ComponentColor(Component.literal("Max Speed: ").append(getSpeed()+""), 0x040404));
		list.add(new ComponentColor(Component.literal("Innacuracy: ").append(getInnacuracy()+""), 0x040404));
		if (isExplosive()) {
			list.add(new ComponentColor(Component.literal("EXPLOSIVE"), 0xaa0000));
			list.add(new ComponentColor(Component.literal("Radius: ").append(getExplosionRadius()+""), 0x040404));
		}
		if (isCausesFire()) list.add(new ComponentColor(Component.literal("INCENDIARY"), 0xaa0000));
		return list;
	}

}
