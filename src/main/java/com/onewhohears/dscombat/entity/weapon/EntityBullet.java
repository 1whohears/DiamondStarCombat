package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.aircraft.DSCPhysicsConstants;
import com.onewhohears.dscombat.data.weapon.BulletData;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.damagesource.WeaponDamageSource;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
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
	public void init() {
		Vec3 dir = UtilAngles.rotationToVector(getYRot(), getXRot());
		setDeltaMovement(dir.scale(getSpeed()));
	}
	
	protected void checkExplode() {
		if (getAge() < minExplodeAge()) return;
		if (!level.hasChunk(chunkPosition().x, chunkPosition().z)) return;
		if (getExplosive()) {
			if (!level.isClientSide) {
				Explosion.BlockInteraction interact = Explosion.BlockInteraction.NONE;
				if (getTerrain()) interact = Explosion.BlockInteraction.BREAK;
				level.explode(this, getExplosionDamageSource(),
					null, getX(), getY(), getZ(), 
					getRadius(), getFire(), 
					interact);
				//System.out.println("EXPLODE "+this+" "+tickCount);
			}
		}
	}
	
	public int minExplodeAge() {
		return 1;
	}
	
	@Override
	protected void tickSetMove() {
		setDeltaMovement(getDeltaMovement().add(0, -DSCPhysicsConstants.GRAVITY, 0));
	}
	
	@Override
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
		return WeaponDamageSource.WeaponDamageType.BULLET.getSource(getOwner(), this);
	}

	@Override
	protected WeaponDamageSource getExplosionDamageSource() {
		return WeaponDamageSource.WeaponDamageType.BULLET_EXPLODE.getSource(getOwner(), this);
	}

	@Override
	public WeaponData.WeaponType getWeaponType() {
		return WeaponData.WeaponType.BULLET;
	}

	@Override
	public WeaponData.WeaponClientImpactType getClientImpactType() {
		if (getExplosive()) return WeaponData.WeaponClientImpactType.SMALL_BULLET_EXPLODE;
		return WeaponData.WeaponClientImpactType.SMALL_BULLET_IMPACT;
	}

}
