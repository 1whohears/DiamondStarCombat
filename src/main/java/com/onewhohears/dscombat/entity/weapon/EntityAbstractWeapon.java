package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.WeaponData;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public abstract class EntityAbstractWeapon extends Projectile {
	
	public static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(EntityAbstractWeapon.class, EntityDataSerializers.INT);
	
	/**
	 * only set on server side
	 */
	protected final int maxAge;
	
	@SuppressWarnings("unchecked")
	public EntityAbstractWeapon(EntityType<?> type, Level level) {
		super((EntityType<? extends Projectile>) type, level);
		maxAge = 0;
	}
	
	@SuppressWarnings("unchecked")
	public EntityAbstractWeapon(Level level, Entity owner, WeaponData data) {
		super((EntityType<? extends Projectile>) data.getEntityType(), level);
		this.setOwner(owner);
		maxAge = data.getMaxAge();
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(OWNER_ID, -1);
	}
	
	/*@Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (level.isClientSide) {
			if (key.equals(DATA)) {
				TEXTURE = this.getWeaponData().getTexture();
			}
		}
	}*/
	
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
		if (!this.level.isClientSide && touchingUnloadedChunk()) {
			//System.out.println("bullet unloaded");
			discard(); 
		}
		if (!level.isClientSide && tickCount > maxAge) { 
			//System.out.println("bullet to old");
			discard();
		}
		motion();
		move(MoverType.SELF, getDeltaMovement());
	}
	
	protected int getOwnerId() {
		return entityData.get(OWNER_ID);
	}
	
	protected void setOwnerId(int id) {
		entityData.set(OWNER_ID, id);
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
	
	@Override
	public Entity getOwner() {
		Entity o = super.getOwner();
		if (o == null && level.isClientSide) {
			Minecraft m = Minecraft.getInstance();
			o = m.level.getEntity(getOwnerId());
		}
		return o;
	}
	
	@Override
	public void setOwner(Entity owner) {
		super.setOwner(owner);
		if (owner != null) this.setOwnerId(owner.getId());
		else this.setOwnerId(-1);
	}

}
