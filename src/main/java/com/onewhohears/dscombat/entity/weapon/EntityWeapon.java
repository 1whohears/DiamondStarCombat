package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.WeaponData;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public abstract class EntityWeapon extends Projectile {
	
	public static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(EntityWeapon.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> AGE = SynchedEntityData.defineId(EntityWeapon.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> TEST_MODE = SynchedEntityData.defineId(EntityWeapon.class, EntityDataSerializers.BOOLEAN);
	
	/**
	 * only set on server side
	 */
	protected int maxAge;
	
	public EntityWeapon(EntityType<? extends EntityWeapon> type, Level level) {
		super(type, level);
	}
	
	@SuppressWarnings("unchecked")
	public EntityWeapon(Level level, Entity owner, WeaponData data) {
		this((EntityType<? extends EntityWeapon>) data.getEntityType(), level);
		this.setOwner(owner);
		maxAge = data.getMaxAge();
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(OWNER_ID, -1);
		entityData.define(AGE, 0);
		entityData.define(TEST_MODE, false);
	}
	
	@Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (level.isClientSide) {
			if (key.equals(AGE)) {
				tickCount = entityData.get(AGE);
			}
		}
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		//System.out.println("bullet reloaded");
		//this.discard();
		super.readAdditionalSaveData(compound);
		tickCount = compound.getInt("tickCount");
		maxAge = compound.getInt("maxAge");
		if (getOwner() != null) setOwnerId(getOwner().getId());
		else setOwnerId(-1);
		this.setTestMode(compound.getBoolean("test_mode"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		//super.addAdditionalSaveData(compound);
		//System.out.println("weapon got additional save data");
		//this.discard();
		super.addAdditionalSaveData(compound);
		compound.putInt("tickCount", tickCount);
		compound.putInt("maxAge", maxAge);
		compound.putBoolean("test_mode", this.isTestMode());
	}
	
	@Override
	public void tick() {
		//System.out.println(this+" "+tickCount);
		if (isTestMode()) return;
 		motion();
		super.tick();
		if (!level.isClientSide) {
			setAge(tickCount);
			if (tickCount > maxAge) kill();
		}
		move(MoverType.SELF, getDeltaMovement());
	}
	
	protected int getOwnerId() {
		return entityData.get(OWNER_ID);
	}
	
	protected void setOwnerId(int id) {
		entityData.set(OWNER_ID, id);
	}
	
	/*public int getAge() {
		return entityData.get(AGE);
	}*/
	
	protected void setAge(int age) {
		entityData.set(AGE, age);
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
		if (entity instanceof EntityWeapon) return false;
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
	
	@Override
	public void remove(Entity.RemovalReason reason) {
		super.remove(reason);
		//System.out.println("REMOVED "+reason.toString()+" "+this);
	}
	
	@Override
	public void checkDespawn() {
		if (this.isTestMode()) return;
		//System.out.println("CHECK DESPAWN");
		if (!level.isClientSide) {
			if (!inEntityTickingRange()) {
				//System.out.println("REMOVED OUT OF TICK RANGE");
				discard();
				return;
			}
		}
	}
	
	public boolean inEntityTickingRange() {
		if (level.isClientSide) return true;
		ServerLevel sl = (ServerLevel) level;
		ServerChunkCache scc = sl.getChunkSource();
		return scc.chunkMap.getDistanceManager().inEntityTickingRange(chunkPosition().toLong());
	}
	
	public int getMaxAge() {
		return maxAge;
	}
	
	public boolean isTestMode() {
    	return entityData.get(TEST_MODE);
    }
    
    public void setTestMode(boolean testMode) {
    	entityData.set(TEST_MODE, testMode);
    }

}
