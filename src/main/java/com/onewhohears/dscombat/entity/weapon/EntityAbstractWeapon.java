package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.init.DataSerializers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public abstract class EntityAbstractWeapon extends Projectile {
	
	public static final EntityDataAccessor<WeaponData> DATA = SynchedEntityData.defineId(EntityAbstractWeapon.class, DataSerializers.WEAPON_DATA);
	
	private ResourceLocation TEXTURE;
	private SoundEvent shootSound;
	
	public EntityAbstractWeapon(EntityType<? extends EntityAbstractWeapon> type, Level level) {
		super(type, level);
	}
	
	@SuppressWarnings("unchecked")
	public EntityAbstractWeapon(Level level, Entity owner, WeaponData data) {
		super((EntityType<? extends Projectile>) data.getEntityType(), level);
		this.setOwner(owner);
		this.setWeaponData(data);
		TEXTURE = data.getTexture();
		shootSound = data.getShootSound();
	}

	@Override
	protected void defineSynchedData() {
		//this.entityData.define(DATA, getDefaultData());
		this.entityData.define(DATA, getWeaponData());
	}
	
	/*@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		//System.out.println("bullet reloaded");
		this.discard();
	}*/

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		//super.addAdditionalSaveData(compound);
		this.discard();
	}
	
	@Override
	public void tick() {
		//System.out.println("weapon "+this.tickCount+" "+this.level);
		super.tick();
		this.xRotO = this.getXRot();
		this.yRotO = this.getYRot();
		if (!this.level.isClientSide && touchingUnloadedChunk()) {
			//System.out.println("bullet unloaded");
			discard(); 
		}
		if (!this.level.isClientSide && this.tickCount > this.getWeaponData().getMaxAge()) { 
			//System.out.println("bullet to old");
			discard();
		}
		motion();
		move(MoverType.SELF, getDeltaMovement());
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
	
	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	public ResourceLocation getTexture() {
		return TEXTURE;
	}
	
	public SoundEvent getShootSound() {
		return shootSound;
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 25000;
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
		return false;
	}
	
	protected void motion() {
		
	}

}
