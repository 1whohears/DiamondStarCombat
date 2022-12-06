package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundMissileMovePacket;
import com.onewhohears.dscombat.data.weapon.MissileData;
import com.onewhohears.dscombat.data.weapon.NonTickingMissileManager;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public abstract class EntityMissile extends EntityBullet {
	
	public static final EntityDataAccessor<Float> ACCELERATION = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAX_ROT = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> BLEED = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.FLOAT);
	
	/**
	 * only set on server side
	 */
	protected float fuseDist, fov;
	
	public Entity target;
	public Vec3 targetPos;
	
	private boolean discardedButTicking;
	
	public EntityMissile(EntityType<? extends EntityMissile> type, Level level) {
		super(type, level);
		if (!level.isClientSide) NonTickingMissileManager.addMissile(this);
	}
	
	public EntityMissile(Level level, Entity owner, MissileData data) {
		super(level, owner, data);
		if (!level.isClientSide) NonTickingMissileManager.addMissile(this);
		this.setAcceleration((float)data.getAcceleration());
		this.setMaxRot(data.getMaxRot());
		this.setBleed((float)data.getBleed());
		fuseDist = (float) data.getFuseDist();
		fov = data.getFov();
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(ACCELERATION, 0f);
		entityData.define(MAX_ROT, 0f);
		entityData.define(BLEED, 0f);
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setAcceleration(compound.getFloat("acc"));
		this.setMaxRot(compound.getFloat("max_rot"));
		this.setBleed(compound.getFloat("bleed"));
		fuseDist = compound.getFloat("fuseDist");
		fov = compound.getFloat("fov");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putFloat("acc", this.getAcceleration());
		compound.putFloat("max_rot", this.getMaxRot());
		compound.putFloat("bleed", this.getBleed());
		compound.putFloat("fuseDist", fuseDist);
		compound.putFloat("fov", fov);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!level.isClientSide && !isRemoved()) {
			tickGuide();
			if (target != null) {
				if (this.distanceTo(target) <= fuseDist) {
					this.setPos(target.position());
					this.kill();
					if (target instanceof EntityMissile) {
						target.kill();
					}
				}
			}
			if (tickCount % 10 == 0) PacketHandler.INSTANCE.send(
					PacketDistributor.TRACKING_ENTITY.with(() -> this), 
					new ClientBoundMissileMovePacket(getId(), position(), 
							getDeltaMovement(), getXRot(), getYRot(), targetPos));
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
		engineSound();
		//System.out.println("pos = "+position());
		//System.out.println("vel = "+getDeltaMovement());
	}
	
	public abstract void tickGuide();
	
	public void tickClientGuide() {
		if (tickCount < 20) return;
		this.guideToPosition();
	}
	
	public void guideToPosition() {
		if (targetPos == null) {
			//missile.discard();
			return;
		}
		Vec3 gm = targetPos.subtract(this.position());
		float grx = UtilAngles.getPitch(gm);
		float gry = UtilAngles.getYaw(gm);
		float orx = this.getXRot();
		float ory = this.getYRot();
		float nrx = orx, nry = ory;
		if (Math.abs(grx-orx) < this.getMaxRot()) {
			nrx = grx;
		} else if (grx > orx) {
			nrx += this.getMaxRot();
		} else if (grx < orx) {
			nrx -= this.getMaxRot();
		}
		if (Math.abs(gry-ory) < this.getMaxRot()) {
			nry = gry;
		} else {
			if (gry > 90 && ory < -90) {
				nry -= this.getMaxRot();
				if (nry < -180) nry += 360;
			} else if (ory > 90 && gry < -90) {
				nry += this.getMaxRot();
				if (nry > 180) nry -= 360;
			} else {
				if (gry > ory) {
					nry += this.getMaxRot();
				} else if (gry < ory) {
					nry -= this.getMaxRot();
				}
			}
		}
		xRotO = getXRot(); 
		yRotO = getYRot();
		setXRot(nrx);
		setYRot(nry);
	}
	
	public void guideToTarget() {
		//System.out.println("target null check");
		if (target == null) {
			this.targetPos = null;
			return;
		}
		//System.out.println("target removed check");
		if (target.isRemoved()) {
			this.target = null;
			this.targetPos = null;
			return;
		}
		if (this.tickCount % 10 == 0) {
			//System.out.println("check target range");
			if (!checkTargetRange(target, 10000)) {
				this.target = null;
				this.targetPos = null;
				return;
			}
			//System.out.println("check can see");
			if (!checkCanSee(target)) {
				//System.out.println("can't see target");
				this.target = null;
				this.targetPos = null;
				return;
			}
		}
		//System.out.println("intercept math");
		Vec3 tVel = target.getDeltaMovement();
		if (target.isOnGround()) tVel = tVel.add(0, -tVel.y, 0); // some entities have -0.07 y vel when on ground
		Vec3 pos = UtilGeometry.interceptPos( 
				this.position(), this.getDeltaMovement(), 
				target.position(), tVel);
		this.targetPos = pos;
		//System.out.println("guide to position");
		this.guideToPosition();
	}
	
	protected boolean checkTargetRange(Entity target, double range) {
		if (fov == -1) return this.distanceTo(target) <= range;
		return UtilGeometry.isPointInsideCone(
				target.position(), 
				this.position(),
				this.getLookAngle(), 
				fov, range);
	}
	
	protected boolean checkCanSee(Entity target) {
		return UtilEntity.canEntitySeeEntity(this, target);
	}
	
	private void engineSound() {
		if (!this.level.isClientSide) return;
		if (this.tickCount == 8) {
			UtilClientSafeSoundInstance.dopplerSound(Minecraft.getInstance(), this, ModSounds.MISSILE_ENGINE_1.get(), 
					0.8F, 1.0F, 10F);
		}
	}
	
	@Override
	public void checkDespawn() {
		
	}
	
	public void tickOutRange() {
		if (tickCount > maxAge) { 
			//System.out.println("REMOVED OLD");
			kill();
			return;
		}
		if (targetPos == null) {
			//System.out.println("REMOVED NO TARGET POS");
			kill();
			return;
		}
		//System.out.println("starting tick guide");
		tickGuide();
		//System.out.println("starting motion");
		motion();
		//System.out.println("starting set pos");
		setPos(position().add(getDeltaMovement()));
	}
	
	@Override
	protected void motion() {
		Vec3 cm = getDeltaMovement();
		double B = getBleed() * UtilEntity.getAirPressure(getY());
		double bleed = B * (Math.abs(getXRot()-xRotO)+Math.abs(getYRot()-yRotO));
		double vel = cm.length() + getAcceleration() - bleed;
		double max = getSpeed();
		if (vel > max) vel = max;
		else if (vel < 0) vel = 0;
		Vec3 nm = getLookAngle().scale(vel);
		setDeltaMovement(nm);
		/*System.out.println("x  = "+getXRot()+" y  = "+getYRot());
		System.out.println("xO = "+xRotO    +" yO = "+yRotO);
		System.out.println("vel = "+vel+" acc = "+getAcceleration()+" bleed = "+bleed);*/
	}
	
	public float getHeat() {
		return 2f;
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
		/*System.out.println("MISSILE GOT HURT!");
		System.out.println("ENTITY "+source.getEntity());
		System.out.println("DIRECT ENTITY "+source.getDirectEntity());*/
		if (this.isRemoved()) return false;
		if (this.equals(source.getDirectEntity())) return false;
		this.kill();
		return true;
	}
	
	/*@Override
	public boolean ignoreExplosion() {
		return false;
	}*/
	
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
	
	public float getMaxRot() {
		float velSqr = (float) getDeltaMovement().lengthSqr();
		float K = 1;
		if (velSqr < 1) K = velSqr;
		return entityData.get(MAX_ROT) * K;
	}
	
	public void setMaxRot(float max_rot) {
		entityData.set(MAX_ROT, max_rot);
	}
	
	public void discardButTick() {
		//System.out.println("discard but tick");
		discard();
		discardedButTicking = true;
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

}
