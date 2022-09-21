package com.onewhohears.dscombat.data;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.entity.weapon.EntityAbstractWeapon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BombData extends BulletData {
	
	public BombData(String id, Vec3 launchPos, int maxAge, int maxAmmo, int fireRate, 
			float damage, double speed, boolean explosive, boolean destroyTerrain, 
			boolean causesFire, double explosiveDamage, float explosionRadius) {
		super(id, launchPos, maxAge, maxAmmo, fireRate, damage, speed, 
				explosive, destroyTerrain, causesFire, explosiveDamage, explosionRadius);
	}
	
	public BombData(CompoundTag tag) {
		super(tag);
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = super.write();
		return tag;
	}
	
	public BombData(FriendlyByteBuf buffer) {
		super(buffer);
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.BOMB;
	}
	
	@Override
	public EntityAbstractWeapon shoot(Level level, Entity vehicle, Entity owner, Vec3 direction, Quaternion vehicleQ) {
		if (!this.checkShoot(1)) return null;
		System.out.println(this.getId()+" ammo "+this.getCurrentAmmo());
		return null;
	}
}
