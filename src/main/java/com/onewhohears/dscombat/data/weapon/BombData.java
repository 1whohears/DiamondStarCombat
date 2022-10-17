package com.onewhohears.dscombat.data.weapon;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityAbstractWeapon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BombData extends BulletData {
	
	public BombData(String id, Vec3 launchPos, int maxAge, int maxAmmo, int fireRate, 
			float damage, double speed, float innacuracy, boolean explosive, boolean destroyTerrain, 
			boolean causesFire, double explosiveDamage, float explosionRadius) {
		super(id, launchPos, maxAge, maxAmmo, fireRate, damage, speed, innacuracy,
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
	public void read(FriendlyByteBuf buffer) {
		super.read(buffer);
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
	public EntityAbstractWeapon shoot(Level level, EntityAbstractAircraft vehicle, Entity owner, Vec3 direction, Quaternion vehicleQ) {
		if (!this.checkAmmo(1, owner)) return null;
		System.out.println(this.getId()+" ammo "+this.getCurrentAmmo());
		this.setLaunchFail("bombs are not added yet");
		
		updateClientAmmo(vehicle);
		return null;
	}
}
