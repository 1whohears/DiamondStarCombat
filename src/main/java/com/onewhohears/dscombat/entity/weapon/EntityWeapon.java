package com.onewhohears.dscombat.entity.weapon;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientWeaponImpact;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.damagesource.WeaponDamageSource;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilEntity;
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
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;

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
	
	public void init() {
		
	}
	
	@Override
	public void tick() {
		//System.out.println(this+" "+tickCount);
		if (isTestMode()) return;
		if (firstTick) init();
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
		HitResult hitresult = checkBlockCollide();
		if (hitresult.getType() != HitResult.Type.MISS) next_pos = hitresult.getLocation();
		Entity owner = getOwner();
		while(!isRemoved()) {
			EntityHitResult entityhitresult = findHitEntity(pos, next_pos);
			if (entityhitresult != null) hitresult = entityhitresult;
			if (owner != null && hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
				Entity hit = ((EntityHitResult)hitresult).getEntity();
				/*System.out.println("BULLET "+this);
				System.out.println("HIT "+hit);
				System.out.println("OWNER "+owner);*/
				if (hit.isAlliedTo(owner)) {
					hitresult = null;
					entityhitresult = null;
				}
			}
			if (hitresult != null && hitresult.getType() != HitResult.Type.MISS && !noPhysics 
					&& !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
				onHit(hitresult);
				hasImpulse = true;
				break;
			}
			if (entityhitresult == null) break;
			hitresult = null;
		}
	}
	
	protected BlockHitResult checkBlockCollide() {
		return level.clip(new ClipContext(position(), position().add(getDeltaMovement()), 
				ClipContext.Block.COLLIDER, getFluidClipContext(), this));
	}
	
	@Nullable
	protected EntityHitResult findHitEntity(Vec3 start, Vec3 end) {
		return UtilEntity.getEntityHitResultAtClip(level, this, start, end, 
				getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0D), 
				this::canHitEntity, 0.3f);
	}
	
	@Override
	public boolean canHitEntity(Entity entity) {
		return super.canHitEntity(entity) && !isAlliedTo(entity);
	}
	
	@Override
	public void onHit(HitResult result) {
		if (isRemoved()) return;
		setPos(result.getLocation());
		super.onHit(result);
	}
	
	@Override
	public void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		//System.out.println("BULLET HIT "+result.getBlockPos());
		kill();
	}
	
	@Override
	public void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		System.out.println("BULLET HIT "+result.getEntity());
		kill();
		result.getEntity().hurt(getImpactDamageSource(), getDamage());
	}
	
	@Override
	public void kill() {
		if (!level.isClientSide) PacketHandler.INSTANCE.send(
				PacketDistributor.TRACKING_ENTITY.with(() -> this), 
				new ToClientWeaponImpact(this, position()));
		super.kill();
	}
	
	public float getDamage() {
		return 0;
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
	
	public int getAge() {
		return entityData.get(AGE);
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
		return dist < 65536;
	}
	
	protected void tickSetMove() {
		
	}
	
	@Override
	public void lerpMotion(double x, double y, double z) {
		
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
    public boolean isAlliedTo(Entity entity) {
		if (entity == null) return false;
    	Entity o = getOwner();
    	if (entity.equals(o)) return true;
    	if (entity instanceof Projectile p) {
    		Entity po = p.getOwner();
    		if (po != null && po.equals(o)) return true;
    	}
    	Entity c = entity.getControllingPassenger();
    	if (c != null) {
    		if (c.equals(o)) return true;
    		return isAlliedTo(c.getTeam());
    	}
    	return super.isAlliedTo(entity);
    }
    
    @Override
    public boolean isAlliedTo(Team team) {
    	if (team == null) return false;
    	Entity o = getOwner();
		if (o != null) return team.isAlliedTo(o.getTeam());
    	return super.isAlliedTo(team);
    }
	
	@Override
	public void remove(Entity.RemovalReason reason) {
		super.remove(reason);
		//System.out.println("REMOVED "+reason.toString()+" "+this);
	}
	
	@Override
	public void checkDespawn() {
		if (isTestMode()) return;
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
    public abstract WeaponData.WeaponType getWeaponType();
    public abstract WeaponData.WeaponClientImpactType getClientImpactType();

}
