package com.onewhohears.dscombat.entity.weapon;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.weapon.WeaponDamageSource;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilParse;

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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public abstract class EntityWeapon extends Projectile {
	
	public static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(EntityWeapon.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> AGE = SynchedEntityData.defineId(EntityWeapon.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> TEST_MODE = SynchedEntityData.defineId(EntityWeapon.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Vec3> SHOOT_POS = SynchedEntityData.defineId(EntityWeapon.class, DataSerializers.VEC3);
	
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
		entityData.define(SHOOT_POS, Vec3.ZERO);
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
		setTestMode(compound.getBoolean("test_mode"));
		setShootPos(UtilParse.readVec3(compound, "shoot_pos"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		//super.addAdditionalSaveData(compound);
		//System.out.println("weapon got additional save data");
		//this.discard();
		super.addAdditionalSaveData(compound);
		compound.putInt("tickCount", tickCount);
		compound.putInt("maxAge", maxAge);
		compound.putBoolean("test_mode", isTestMode());
		UtilParse.writeVec3(compound, getShootPos(), "shoot_pos");
	}
	
	@Override
	public void tick() {
		//System.out.println(this+" "+tickCount);
		if (isTestMode()) return;
		if (!level.isClientSide && firstTick) setShootPos(position());
		super.tick();
		tickCheckCollide();
		tickSetMove();
		setPos(position().add(getDeltaMovement()));
		checkInsideBlocks();
		tickAge();
	}
	
	protected void tickAge() {
		if (!level.isClientSide) {
			setAge(tickCount);
			if (tickCount > maxAge) kill();
		}
	}
	
	public Fluid getFluidClipContext() {
		return ClipContext.Fluid.NONE;
	}
	
	protected void tickCheckCollide() {
		Vec3 move = getDeltaMovement();
		Vec3 pos = position();
		Vec3 next_pos = pos.add(move);
		HitResult hitresult = level.clip(new ClipContext(pos, next_pos, 
				ClipContext.Block.COLLIDER, getFluidClipContext(), this));
		if (hitresult.getType() != HitResult.Type.MISS) next_pos = hitresult.getLocation();
		Entity owner = getOwner();
		while(!isRemoved()) {
			EntityHitResult entityhitresult = findHitEntity(pos, next_pos);
			if (entityhitresult != null) hitresult = entityhitresult;
			if (owner != null && hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
				Entity hit = ((EntityHitResult)hitresult).getEntity();
				System.out.println("BULLET "+this);
				System.out.println("HIT "+hit);
				System.out.println("OWNER "+owner);
				if (hit.equals(owner)) {
					hitresult = null;
					entityhitresult = null;
				} else if (owner instanceof Player player) {
					if (hit instanceof Player p && !player.canHarmPlayer(p)) {
						hitresult = null;
						entityhitresult = null;
					} else if (hit instanceof EntityAircraft plane) {
						Entity c = plane.getControllingPassenger();
						if (player.equals(c) || (c instanceof Player p && !player.canHarmPlayer(p))) {
							hitresult = null;
							entityhitresult = null;
						}
					}
				} 
			}
			if (hitresult != null && hitresult.getType() != HitResult.Type.MISS && !noPhysics 
					&& !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
				onHit(hitresult);
				hasImpulse = true;
				break;
			}
			if (entityhitresult == null) break;
			hitresult = null;
		}
	}
	
	@Nullable
	protected EntityHitResult findHitEntity(Vec3 start, Vec3 end) {
		return ProjectileUtil.getEntityHitResult(level, this, 
				start, end, 
				getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0D), 
				this::canHitEntity);
	}
	
	protected int getOwnerId() {
		return entityData.get(OWNER_ID);
	}
	
	protected void setOwnerId(int id) {
		entityData.set(OWNER_ID, id);
	}
	
	protected void setAge(int age) {
		entityData.set(AGE, age);
	}
	
	@Override
	public boolean ignoreExplosion() {
		return true;
	}
	
	@Override 
	public boolean canBeCollidedWith() {
		return false;
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
		return false;
	}
	
	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 25000;
	}
	
	protected void tickSetMove() {
		
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
    
    public Vec3 getShootPos() {
    	return entityData.get(SHOOT_POS);
    }
    
    private void setShootPos(Vec3 pos) {
    	entityData.set(SHOOT_POS, pos);
    }
    
    protected abstract WeaponDamageSource getImpactDamageSource();
    
    protected abstract WeaponDamageSource getExplosionDamageSource();

}
