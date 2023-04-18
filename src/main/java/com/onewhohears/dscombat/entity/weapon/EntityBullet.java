package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.BulletData;
import com.onewhohears.dscombat.data.weapon.WeaponDamageSource;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityBullet extends EntityWeapon {
	
	public static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(EntityBullet.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Boolean> EXPLOSIVE = SynchedEntityData.defineId(EntityBullet.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> TERRAIN = SynchedEntityData.defineId(EntityBullet.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> FIRE = SynchedEntityData.defineId(EntityBullet.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(EntityBullet.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(EntityBullet.class, EntityDataSerializers.FLOAT);
	
	public EntityBullet(EntityType<? extends EntityBullet> type, Level level) {
		super(type, level);
	}
	
	public EntityBullet(Level level, Entity owner, BulletData data) {
		super(level, owner, data);
		this.setDamage(data.getDamage());
		this.setExplosive(data.isExplosive());
		this.setTerrain(data.isDestroyTerrain());
		this.setFire(data.isCausesFire());
		this.setRadius(data.getExplosionRadius());
		this.setSpeed((float)data.getSpeed());
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DAMAGE, 0f);
		entityData.define(EXPLOSIVE, false);
		entityData.define(TERRAIN, false);
		entityData.define(FIRE, false);
		entityData.define(RADIUS, 0f);
		entityData.define(SPEED, 0f);
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setDamage(compound.getFloat("damage"));
		this.setExplosive(compound.getBoolean("explosive"));
		this.setTerrain(compound.getBoolean("terrain"));
		this.setFire(compound.getBoolean("fire"));
		this.setRadius(compound.getFloat("radius"));
		this.setSpeed(compound.getFloat("speed"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putFloat("damage", this.getDamage());
		compound.putBoolean("explosive", this.getExplosive());
		compound.putBoolean("terrain", this.getTerrain());
		compound.putBoolean("fire", this.getFire());
		compound.putFloat("radius", this.getRadius());
		compound.putFloat("speed", this.getSpeed());
	}
	
	@Override
	public void tick() {
		super.tick();
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
		//System.out.println("BULLET HIT "+result.getEntity());
		DamageSource source = getImpactDamageSource();
		result.getEntity().hurt(source, getDamage());
		kill();
	}
	
	protected void checkExplode() {
		if (!level.hasChunk(chunkPosition().x, chunkPosition().z)) return;
		if (getExplosive()) {
			if (!level.isClientSide) {
				Explosion.BlockInteraction interact = Explosion.BlockInteraction.NONE;
				if (getTerrain()) interact = Explosion.BlockInteraction.BREAK;
				level.explode(this, getExplosionDamageSource(),
					null, getX(), getY(), getZ(), 
					getRadius(), getFire(), 
					interact);
				System.out.println("EXPLODE "+this);
			} else {
				level.addParticle(ParticleTypes.SMOKE, 
						getX(), getY()+0.5D, getZ(), 
						0.0D, 0.0D, 0.0D);
			}
		}
	}
	
	@Override
	protected void tickSetMove() {
		Vec3 dir = UtilAngles.rotationToVector(getYRot(), getXRot());
		setDeltaMovement(dir.scale(getSpeed()));
	}
	
	public float getDamage() {
		return entityData.get(DAMAGE);
	}
	
	public void setDamage(float damage) {
		entityData.set(DAMAGE, damage);
	}
	
	public boolean getExplosive() {
		return entityData.get(EXPLOSIVE);
	}
	
	public void setExplosive(boolean explosive) {
		entityData.set(EXPLOSIVE, explosive);
	}
	
	public boolean getTerrain() {
		return entityData.get(TERRAIN);
	}
	
	public void setTerrain(boolean terrain) {
		entityData.set(TERRAIN, terrain);
	}
	
	public boolean getFire() {
		return entityData.get(FIRE);
	}
	
	public void setFire(boolean fire) {
		entityData.set(FIRE, fire);
	}
	
	public float getRadius() {
		return entityData.get(RADIUS);
	}
	
	public void setRadius(float radius) {
		entityData.set(RADIUS, radius);
	}
	
	public float getSpeed() {
		return entityData.get(SPEED);
	}
	
	public void setSpeed(float speed) {
		entityData.set(SPEED, speed);
	}
	
	@Override
	public void kill() {
		// ORDER MATTERS
		super.kill();
		checkExplode();
	}

	@Override
	protected WeaponDamageSource getImpactDamageSource() {
		return WeaponDamageSource.bullet(getOwner(), this);
	}

	@Override
	protected WeaponDamageSource getExplosionDamageSource() {
		return WeaponDamageSource.bullet_explode(getOwner(), this);
	}

}
