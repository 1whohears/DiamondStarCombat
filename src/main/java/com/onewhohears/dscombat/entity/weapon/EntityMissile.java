package com.onewhohears.dscombat.entity.weapon;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.damagesource.WeaponDamageSource;
import com.onewhohears.dscombat.data.weapon.MissileData;
import com.onewhohears.dscombat.data.weapon.NonTickingMissileManager;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilClientSafeSoundInstance;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public abstract class EntityMissile extends EntityBullet {
	
	public static final EntityDataAccessor<Float> ACCELERATION = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> TURN_RADIUS = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> BLEED = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Integer> FUEL_TICKS = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Vec3> TARGET_POS = SynchedEntityData.defineId(EntityMissile.class, DataSerializers.VEC3);
	
	/**
	 * only set on server side
	 */
	protected float fuseDist, fov;
	protected int throughWaterDepth, throughBlocksDepth;
	
	public Entity target;
	public Vec3 targetPos;
	
	private boolean discardedButTicking;
	private int prevTickCount, tickCountRepeats, repeatCoolDown, lerpSteps;
	private double lerpX, lerpY, lerpZ, lerpXRot, lerpYRot;
	
	public EntityMissile(EntityType<? extends EntityMissile> type, Level level) {
		super(type, level);
		if (!level.isClientSide) NonTickingMissileManager.addMissile(this);
		if (level.isClientSide) engineSound();
		throughWaterDepth = 0;
		throughBlocksDepth = 0;
	}
	
	public EntityMissile(Level level, Entity owner, MissileData data) {
		super(level, owner, data);
		if (!level.isClientSide) NonTickingMissileManager.addMissile(this);
		setAcceleration((float)data.getAcceleration());
		setTurnRadius(data.getTurnRadius());
		setBleed((float)data.getBleed());
		fuseDist = (float) data.getFuseDist();
		fov = data.getFov();
		setFuelTicks(data.getFuelTicks());
		throughWaterDepth = 0;
		throughBlocksDepth = 0;
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(ACCELERATION, 0f);
		entityData.define(TURN_RADIUS, 0f);
		entityData.define(BLEED, 0f);
		entityData.define(FUEL_TICKS, 0);
		entityData.define(TARGET_ID, -1);
		entityData.define(TARGET_POS, Vec3.ZERO.add(0, -1000, 0));
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		setAcceleration(compound.getFloat("acc"));
		setTurnRadius(compound.getFloat("turnRadius"));
		setBleed(compound.getFloat("bleed"));
		fuseDist = compound.getFloat("fuseDist");
		fov = compound.getFloat("fov");
		setFuelTicks(compound.getInt("fuelTicks"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putFloat("acc", getAcceleration());
		compound.putFloat("turnRadius", getTurnRadius());
		compound.putFloat("bleed", getBleed());
		compound.putFloat("fuseDist", fuseDist);
		compound.putFloat("fov", fov);
		compound.putInt("fuelTicks", getFuelTicks());
	}
	
	@Override
	public void tick() {
		if (isTestMode()) {
			if (level.isClientSide) {
				Vec3 look = getLookAngle();
				level.addParticle(ParticleTypes.SMOKE, 
					getX(), getY(), getZ(), 
					-look.x * 0.5D + random.nextGaussian() * 0.05D, 
					-look.y * 0.5D + random.nextGaussian() * 0.05D, 
					-look.z * 0.5D + random.nextGaussian() * 0.05D);
			}
			return;
		}
		xRotO = getXRot(); 
		yRotO = getYRot();
		if (!level.isClientSide && !isRemoved()) {
			tickGuide();
			if (targetPos != null) setTargetPos(targetPos);
			else setTargetPos(Vec3.ZERO.add(0, -1000, 0));
			if (target != null) setTargetId(target.getId());
			else setTargetId(-1);
			if (target != null && distanceTo(target) <= fuseDist) kill();
		}
		if (level.isClientSide && !isRemoved()) {
			tickClientGuide();
			Vec3 move = getDeltaMovement();
			level.addParticle(ParticleTypes.SMOKE, 
					getX(), getY(), getZ(), 
					-move.x * 0.5D + random.nextGaussian() * 0.05D, 
					-move.y * 0.5D + random.nextGaussian() * 0.05D, 
					-move.z * 0.5D + random.nextGaussian() * 0.05D);
		}
		super.tick();
		tickLerp();
	}
	
	public abstract void tickGuide();
	
	public void tickClientGuide() {
		Vec3 tpos = getTargetPos();
		if (tpos.y == -1000) tpos = null;
		int tid = getTargetId();
		if (tid != -1) {
			Entity t = level.getEntity(tid);
			if (t != null) targetPos = t.position();
			else targetPos = tpos;
		} else targetPos = tpos;
		guideToPosition();
	}
	
	public void guideToPosition() {
		if (targetPos == null) return;
		Vec3 gm = targetPos.subtract(position());
		float grx = UtilAngles.getPitch(gm), gry = UtilAngles.getYaw(gm);
		float orx = getXRot(), ory = getYRot();
		float nrx = orx, nry = ory;
		float rot = getTurnDegrees();
		if (Math.abs(grx-orx) < rot) nrx = grx;
		else if (grx > orx) nrx += rot;
		else if (grx < orx) nrx -= rot;
		if (Math.abs(gry-ory) < rot) nry = gry;
		else {
			if (gry > 90 && ory < -90) {
				nry -= rot;
				if (nry < -180) nry += 360;
			} else if (ory > 90 && gry < -90) {
				nry += rot;
				if (nry > 180) nry -= 360;
			} else {
				if (gry > ory) nry += rot;
				else if (gry < ory) nry -= rot;
			}
		}
		setXRot(nrx);
		setYRot(nry);
	}
	
	public float getTurnDegrees() {
		return (float)getDeltaMovement().length() / getTurnRadius() * Mth.RAD_TO_DEG;
	}
	
	public void guideToTarget() {
		//System.out.println("target null check");
		if (target == null) {
			targetPos = null;
			return;
		}
		//System.out.println("target removed check");
		if (target.isRemoved()) {
			target = null;
			targetPos = null;
			return;
		}
		if (tickCount % 10 == 0) {
			//System.out.println("check target range");
			if (!checkTargetRange(target, 10000)) {
				target = null;
				targetPos = null;
				return;
			}
			//System.out.println("check can see");
			if (!checkCanSee(target)) {
				//System.out.println("can't see target");
				target = null;
				targetPos = null;
				return;
			}
		}
		//System.out.println("intercept math");
		Vec3 tVel = target.getDeltaMovement();
		if (UtilEntity.isOnGroundOrWater(target)) 
			tVel = tVel.multiply(1, 0, 1);
		Vec3 pos = UtilGeometry.interceptPos( 
				position(), getDeltaMovement(), 
				target.position(), tVel);
		targetPos = pos;
		//System.out.println("guide to position");
		guideToPosition();
	}
	
	protected static boolean checkTargetRange(Entity weapon, Entity target, float fov, double range) {
		if (fov == -1) return weapon.distanceTo(target) <= range;
		return UtilGeometry.isPointInsideCone(
				target.position(), 
				weapon.position(),
				weapon.getLookAngle(), 
				fov, range);
	}
	
	protected boolean checkTargetRange(Entity target, double range) {
		return checkTargetRange(this, target, fov, range);
	}
	
	protected boolean checkCanSee(Entity target) {
		return UtilEntity.canEntitySeeEntity(this, target, Config.COMMON.maxBlockCheckDepth.get(), 
				throughWaterDepth, throughBlocksDepth);
	}
	
	private void engineSound() {
		UtilClientSafeSoundInstance.dopplerSound(Minecraft.getInstance(), this, 
				ModSounds.MISSILE_ENGINE_1.get(), 
				0.8F, 1.0F, 10F);
	}
	
	@Override
	public void checkDespawn() {
		
	}
	
	public void tickOutRange() {
		xRotO = getXRot(); 
		yRotO = getYRot();
		// uses special kill override function. don't change to discard.
		if (tickCount > maxAge) { 
			//System.out.println("old");
			kill();
			return;
		}
		if (dieIfNoTargetOutsideTickRange() && targetPos == null) {
			//System.out.println("no target pos");
			kill();
			return;
		}
		//System.out.println("starting tick guide");
		tickGuide();
		//System.out.println("starting motion");
		tickSetMove();
		//System.out.println("starting set pos");
		setPos(position().add(getDeltaMovement()));
	}
	
	public boolean dieIfNoTargetOutsideTickRange() {
		return true;
	}
	
	@Override
	protected void tickSetMove() {
		Vec3 cm = getDeltaMovement();
		double B = getBleed() * UtilEntity.getAirPressure(this);
		double bleed = B * (Math.abs(getXRot()-xRotO)+Math.abs(getYRot()-yRotO));
		double vel = cm.length() - bleed;
		if (tickCount <= getFuelTicks()) vel += getAcceleration();
		double max = getSpeed();
		if (vel > max) vel = max;
		else if (vel <= 0) vel = 0;
		Vec3 nm = getLookAngle().scale(vel);
		setDeltaMovement(nm);
		/*if (!level.isClientSide) {
			System.out.println("speed = "+vel);
			System.out.println("bleed = "+bleed);
			System.out.println("fuelAge = "+getFuelTicks());
			System.out.println("x  = "+getXRot()+" y  = "+getYRot());
			System.out.println("xO = "+xRotO    +" yO = "+yRotO);
		}*/
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
		if (isRemoved()) return false;
		if (equals(source.getDirectEntity())) return false;
		kill();
		return true;
	}
	
	@Nullable
	protected EntityHitResult findHitEntity(Vec3 start, Vec3 end) {
		return ProjectileUtil.getEntityHitResult(this, 
				start, end, 
				getBoundingBox(), 
				this::canHitEntity, 
				start.distanceToSqr(end));
	}
	
	@Override
	public boolean ignoreExplosion() {
		return false;
	}
	
	public float getAcceleration() {
		return entityData.get(ACCELERATION);
	}
	
	public void setAcceleration(float acceleration) {
		entityData.set(ACCELERATION, acceleration);
	}
	
	public float getBleed() {
		return entityData.get(BLEED);
	}
	
	public void setBleed(float bleed) {
		entityData.set(BLEED, bleed);
	}
	
	public int getFuelTicks() {
		return entityData.get(FUEL_TICKS);
	}
	
	public void setFuelTicks(int fuelTicks) {
		entityData.set(FUEL_TICKS, fuelTicks);
	}
	
	public float getTurnRadius() {
		return entityData.get(TURN_RADIUS);
	}
	
	public void setTurnRadius(float max_rot) {
		entityData.set(TURN_RADIUS, max_rot);
	}
	
	public int getTargetId() {
		return entityData.get(TARGET_ID);
	}
	
	public void setTargetId(int id) {
		entityData.set(TARGET_ID, id);
	}
	
	public Vec3 getTargetPos() {
		return entityData.get(TARGET_POS);
	}
	
	public void setTargetPos(Vec3 pos) {
		entityData.set(TARGET_POS, pos);
	}
	
	public void discardButTick() {
		//System.out.println("discard but tick");
		discard();
		discardedButTicking = true;
		repeatCoolDown = 5;
	}
	
	@Override
	public void kill() {
		super.kill();
		discardedButTicking = false;
	}
	
	@Override
	public void revive() {
		super.revive();
		discardedButTicking = false;
	}
	
	public boolean isDiscardedButTicking() {
		return discardedButTicking;
	}
	
	public int getTickCountRepeats() {
		if (tickCount == prevTickCount) ++tickCountRepeats;
		else if (tickCountRepeats > 0) tickCountRepeats = 0;
		prevTickCount = tickCount;
		if (repeatCoolDown > 0) {
			--repeatCoolDown;
			return 10;
		}
		return tickCountRepeats;
	}
	
	public Fluid getFluidClipContext() {
		return ClipContext.Fluid.SOURCE_ONLY;
	}
	
	@Override
	protected WeaponDamageSource getImpactDamageSource() {
		return WeaponDamageSource.missile_contact(getOwner(), this);
	}

	@Override
	protected WeaponDamageSource getExplosionDamageSource() {
		return WeaponDamageSource.missile(getOwner(), this);
	}
	
	@Override
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        if (x == getX() && y == getY() && z == getZ()) return;
        lerpX = x; lerpY = y; lerpZ = z;
        lerpYRot = yaw; lerpXRot = pitch;
        lerpSteps = 10;
    }
	
	private void tickLerp() {
		if (!level.isClientSide) {
			syncPacketPositionCodec(getX(), getY(), getZ());
			lerpSteps = 0;
			return;
		}
		if (lerpSteps > 0) {
			double d0 = getX() + (lerpX - getX()) / (double)lerpSteps;
	        double d1 = getY() + (lerpY - getY()) / (double)lerpSteps;
	        double d2 = getZ() + (lerpZ - getZ()) / (double)lerpSteps;
	        double d3 = Mth.wrapDegrees(lerpYRot - (double)getYRot());
	        setYRot(getYRot() + (float)d3 / (float)lerpSteps);
	        setXRot(getXRot() + (float)(lerpXRot - (double)getXRot()) / (float)lerpSteps);
	        --lerpSteps;
	        setPos(d0, d1, d2);
	        setRot(getYRot(), getXRot());
		}
	}

}
