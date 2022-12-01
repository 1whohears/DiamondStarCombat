package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundMissileMovePacket;
import com.onewhohears.dscombat.data.ChunkManager;
import com.onewhohears.dscombat.data.weapon.MissileData;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilClientSafeSoundInstance;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public abstract class EntityMissile extends EntityBullet {
	
	public static final EntityDataAccessor<Float> ACCELERATION = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAX_ROT = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.FLOAT);
	
	/**
	 * only set on server side
	 */
	protected final double fuseDist;
	/**
	 * only set on server side
	 */
	protected final float fov;
	
	public Entity parent;
	public Entity target;
	public Vec3 targetPos;
	
	public EntityMissile(EntityType<?> type, Level level) {
		super(type, level);
		fuseDist = 0;
		fov = 0;
	}
	
	public EntityMissile(Level level, Entity owner, MissileData data) {
		super(level, owner, data);
		this.setAcceleration((float)data.getAcceleration());
		this.setMaxRot(data.getMaxRot());
		fuseDist = data.getFuseDist();
		fov = data.getFov();
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(ACCELERATION, 0f);
		entityData.define(MAX_ROT, 0f);
	}
	
	/*private EntityMissile(EntityType<? extends EntityMissile> type, Level level, Entity owner, MissileData data, ResourceLocation texture) {
		this(type, level, texture);
		this.setOwner(owner);
		this.setWeaponData(data);
	}*/
	
	@Override
	public void tick() {
		//System.out.println("ROCKET "+this.tickCount+" "+this.level);
		//System.out.println("target = "+target); // target entity is not set on client side
		//MissileData data = (MissileData)getWeaponData();
		if (!this.level.isClientSide) {
			tickGuide();
			if (target != null) {
				if (this.distanceTo(target) <= fuseDist) {
					//System.out.println("WITHIN FUSE DISTANCE");
					//System.out.println("IS REMOVED "+this.isRemoved());
					this.setPos(target.position());
					this.checkExplode();
					this.discard();
					if (target instanceof EntityMissile) {
						target.kill();
					}
					//System.out.println("close enough");
				}
			}
			if (targetPos != null) loadChunks();
			if (tickCount % 10 == 0) PacketHandler.INSTANCE.send(
					PacketDistributor.TRACKING_ENTITY.with(() -> this), 
					new ClientBoundMissileMovePacket(getId(), position(), 
							getDeltaMovement(), getXRot(), getYRot(), targetPos));
		}
		if (this.level.isClientSide /*&& this.tickCount % 2 == 0*/) {
			Vec3 move = getDeltaMovement();
			level.addParticle(ParticleTypes.SMOKE, 
					getX(), getY(), getZ(), 
					-move.x * 0.5D + random.nextGaussian() * 0.05D, 
					-move.y * 0.5D + random.nextGaussian() * 0.05D, 
					-move.z * 0.5D + random.nextGaussian() * 0.05D);
			tickClientGuide();
		}
		engineSound();
		super.tick();
		//System.out.println("pos = "+position());
		//System.out.println("vel = "+getDeltaMovement());
		//System.out.println("pit = "+getXRot()+" yaw = "+getYRot());
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
		this.setXRot(nrx);
		this.setYRot(nry);
	}
	
	public void guideToTarget() {
		if (target == null) {
			//missile.discard();
			this.targetPos = null;
			return;
		}
		if (target.isRemoved()) {
			//missile.discard();
			this.target = null;
			this.targetPos = null;
			return;
		}
		if (this.tickCount % 4 == 0) {
			if (!checkTargetRange(target, 10000)) {
				//missile.discard();
				this.targetPos = null;
				return;
			}
			if (!checkCanSee(target)) {
				//missile.discard();
				//System.out.println("can't see target");
				this.targetPos = null;
				return;
			}
		}
		//System.out.println("==========");
		Vec3 tVel = target.getDeltaMovement();
		if (target.isOnGround()) tVel = tVel.add(0, -tVel.y, 0); // some entities have -0.07 y vel when on ground
		Vec3 pos = UtilGeometry.interceptPos( 
				this.position(), this.getDeltaMovement(), 
				target.position(), tVel);
		//System.out.println("TARGET POS = "+target.position());
		//System.out.println("INTER  POS = "+pos);
		this.targetPos = pos;
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
	
	private void loadChunks() {
		ChunkPos cp = this.chunkPosition();
		ChunkManager.addChunk(this, cp.x, cp.z);
		Vec3 move = this.getDeltaMovement();
		Vec3 next = position().add(move);
		ChunkPos ncp = level.getChunk(new BlockPos(next)).getPos();
		if (ncp.x != cp.x || ncp.z != cp.z)
			ChunkManager.addChunk(this, ncp.x, ncp.z);
	}
	
	@Override
	protected void motion() {
		//MissileData data = (MissileData)getWeaponData();
		Vec3 cm = getDeltaMovement();
		double B = 0.004d * UtilEntity.getAirPressure(getY());
		double bleed = B * (Math.abs(getXRot()-xRotO)+Math.abs(getYRot()-yRotO));
		double vel = cm.length() + this.getAcceleration() - bleed;
		double max = this.getSpeed();
		if (vel > max) vel = max;
		else if (vel < 0) vel = 0;
		Vec3 nm = getLookAngle().scale(vel);
		setDeltaMovement(nm);
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
		this.checkExplode();
		this.discard();
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
	
	public float getMaxRot() {
		return entityData.get(MAX_ROT);
	}
	
	public void setMaxRot(float max_rot) {
		entityData.set(MAX_ROT, max_rot);
	}

}
