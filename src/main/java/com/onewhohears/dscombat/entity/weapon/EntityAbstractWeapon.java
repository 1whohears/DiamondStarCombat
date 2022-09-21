package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.BulletData;
import com.onewhohears.dscombat.data.WeaponData;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public abstract class EntityAbstractWeapon extends Projectile {
	
	public static final EntityDataAccessor<WeaponData> DATA = SynchedEntityData.defineId(EntityAbstractWeapon.class, DataSerializers.WEAPON_DATA);
	
	public EntityAbstractWeapon(EntityType<? extends EntityAbstractWeapon> type, Level level) {
		super(type, level);
	}
	
	public EntityAbstractWeapon(Level level, Entity pilot, WeaponData data) {
		super(ModEntities.BULLET.get(), level);
		this.setOwner(pilot);
		this.setWeaponData(data);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA, BulletData.basic());
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		//System.out.println("bullet reloaded");
		this.discard();
	}

	/*@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
	}*/
	
	@Override
	public void tick() {
		super.tick();
		/*if (!this.level.isClientSide && touchingUnloadedChunk()) {
			//System.out.println("bullet unloaded");
			discard(); 
			return; 
		}*/
		if (!this.level.isClientSide && this.tickCount > this.getWeaponData().getMaxAge()) { 
			//System.out.println("bullet to old");
			discard(); 
			return; 
		}
	}
	
	public void setWeaponData(WeaponData data) {
		entityData.set(DATA, data);
	}
	
	public WeaponData getWeaponData() {
		return entityData.get(DATA);
	}
	
	@Override
	public boolean ignoreExplosion() {
		return true;
	}
	
	/*@Override
	public boolean canBeCollidedWith() {
		return false;
	}*/
	
	@Override 
	public boolean canCollideWith(Entity entity) {
		if (entity instanceof EntityAbstractWeapon) return false;
		return true;
	}

}
