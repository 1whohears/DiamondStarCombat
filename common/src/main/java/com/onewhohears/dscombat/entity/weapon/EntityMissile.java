package com.onewhohears.dscombat.entity.weapon;

import java.util.List;
import java.util.Objects;

import com.onewhohears.dscombat.entity.Revivable;
import com.onewhohears.onewholibs.common.core.DistantRayCastManager;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DependencySafety;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.radar.TrackableEntitiesManager;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.dscombat.data.weapon.MissileChunkLoadingManager;
import com.onewhohears.dscombat.data.weapon.stats.MissileStats;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.damagesource.WeaponDamageSource;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.client.util.UtilClientSafeSounds;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.dscombat.client.util.UtilParticles;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import static com.onewhohears.dscombat.data.radar.RadarInstance.RAY_CAST_TIMEOUT;

public abstract class EntityMissile<T extends MissileStats> extends EntityBullet<T> implements Revivable {
	
	public static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Vec3> TARGET_POS = SynchedEntityData.defineId(EntityMissile.class, DataSerializers.VEC3);
	
	public Entity target;
	public Vec3 targetPos;

    @Nullable
    protected Vec3 explodeRelTargetNextTick = null;
	
	private boolean discardedButTicking, didSonicBoom;
	private int prevTickCount, tickCountRepeats, repeatCoolDown;
	private int lerpSteps;
	private double lerpX, lerpY, lerpZ, lerpXRot, lerpYRot;
	
	public EntityMissile(EntityType<? extends EntityMissile<?>> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
		if (!isClientSide()) MissileChunkLoadingManager.registerMissile(this);
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(TARGET_ID, -1);
		entityData.define(TARGET_POS, Vec3.ZERO.add(0, -1000, 0));
	}
	
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		super.writeSpawnData(buffer);
		DataSerializers.VEC3.write(buffer, getDeltaMovement());
		// Sync rotation to prevent missiles appearing horizontal on spawn
		buffer.writeFloat(getXRot());
		buffer.writeFloat(getYRot());
	}

	@Override
	public void readSpawnData(FriendlyByteBuf buffer) {
		super.readSpawnData(buffer);
		setDeltaMovement(DataSerializers.VEC3.read(buffer));
		// Read rotation to prevent missiles appearing horizontal on spawn
		setXRot(buffer.readFloat());
		setYRot(buffer.readFloat());
	}
	
	@Override
	public void init() {
	}
	
	@Override
	public void tick() {
		if (isClientSide()) clientTickParticles();
		if (isTestMode()) return;
		xRotO = getXRot(); 
		yRotO = getYRot();
		
		// CRITICAL: Kill missiles that go underground to prevent getting stuck
		if (!isClientSide() && getY() < -60) {
			kill();
			return;
		}
		
		if (!isRemoved()) {
			if (!isClientSide()) {
                handleInterceptTarget();
				tickGuide();
				if (targetPos != null) setTargetPos(targetPos);
				else setTargetPos(Vec3.ZERO.add(0, -1000, 0));
				if (target != null) setTargetId(target.getId());
				else setTargetId(-1);
				checkInterceptTarget();
				// Only update tracking/RDP every 5 ticks — these are not needed every single tick
				if (tickCount % 5 == 0) {
					TrackableEntitiesManager.addTrackableEntity(this);
					DependencySafety.addExtraEntityToRDP(Objects.requireNonNull(getServer()), this);
				}
			} else {
				tickClientGuide();
				if (firstTick) engineSound();
				if (canSonicBoom()) sonicBoomSound();
			}
		}
		super.tick();
		tickLerp();
		if (!isClientSide() && !isRemoved() && tickCount > 100 && getDeltaMovement().length() < 0.1) {
			kill();
			return;
		}
	}

    protected void handleInterceptTarget() {
        if (target != null && explodeRelTargetNextTick != null) {
            //System.out.println("EXPLODING CAUSE NEXT TICK");
            moveTo(target.position().add(explodeRelTargetNextTick));
            kill();
            return;
        }
    }

    protected void checkInterceptTarget() {
        if (target == null || explodeRelTargetNextTick != null) return;
        double fuseDistSqr = getWeaponStats().getFuseDist() * getWeaponStats().getFuseDist();
        Vec3 pr = target.position().subtract(position());
        Vec3 vr = target.getDeltaMovement().subtract(getDeltaMovement());
        double vr2 = vr.lengthSqr();
        if (vr2 < 1e-9) {
            if (pr.lengthSqr() <= fuseDistSqr) kill();
            return;
        }
        double t = -pr.dot(vr) / vr2;
        t = Math.max(0.0, Math.min(1.0, t));
        Vec3 closest = pr.add(vr.scale(t));
        double closestDist = closest.length();
        //if (closestDist < 100) System.out.println("t = "+t+" closest = "+closestDist+" "+closest);
        if (closest.lengthSqr() <= fuseDistSqr) {
            //explodeRelTargetNextTick(target.position().add(target.getDeltaMovement()).subtract(closest));
            explodeRelTargetNextTick(closest.scale(-1));
        }
    }

    protected void explodeRelTargetNextTick(Vec3 pos) {
        //System.out.println("EXPLODE AT NEXT TICK "+pos+" target "+target.position()+" "+target.getDeltaMovement());
        explodeRelTargetNextTick = pos;
    }
	
	public void clientTickParticles() {
		if (getWeaponStats().isShowAfterBurner() && getAge() <= getFuelTicks()) {
			UtilParticles.missileAfterBurner(getWorld(), position(), getLookAngle().scale(-1));
		}
		if (getWeaponStats().isShowTrail()) {
			UtilParticles.missileTrail(getWorld(), position(), getLookAngle(), getRadius(), isInWater());
		}
	}
	
	public abstract void tickGuide();
	
	public void tickClientGuide() {
		Vec3 tpos = getTargetPos();
		if (tpos.y == -1000) tpos = null;
		int tid = getTargetId();
		if (tid != -1) {
			Entity t = getWorld().getEntity(tid);
			if (t != null) targetPos = t.position();
			else targetPos = tpos;
		} else targetPos = tpos;
		guideToPosition();
	}
	
	public void guideToTarget() {
		if (target == null) {
            //System.out.println("target is null");
            resetTarget();
			return;
		}
		if (target.isRemoved()) {
            //System.out.println("target is removed");
            resetTarget();
			return;
		}
		if (tickCount % 10 == 0) {
			if (!checkTargetRange(target, 1000000)) {
                //System.out.println("target not in range");
                resetTarget();
				return;
			}
			//System.out.println("check can see");
            DistantRayCastManager.distantRayCast((ServerLevel) getWorld(), this, target,
                    (level, missile, targetEntity, pass) -> {
                        if (!pass) {
                            //System.out.println("target FAILED ray cast");
                            resetTarget();
                        }
                    }, RAY_CAST_TIMEOUT, 550,
                    getWeaponStats().getSeeThroWater()+1, getWeaponStats().getSeeThroBlock());
		}
        if (target == null) {
            //System.out.println("target is null 2");
            return;
        }
		//System.out.println("intercept math");
		Vec3 tVel = target.getDeltaMovement();
		if (UtilVehicleEntity.isOnGroundOrWater(target))
			tVel = tVel.multiply(1, 0, 1);
        targetPos = UtilGeometry.interceptPos(
            position(), getDeltaMovement(),
            target.getBoundingBox().getCenter(), tVel);
		//System.out.println("guide to position");
		guideToPosition();
	}

    public void resetTarget() {
        target = null;
        targetPos = null;
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
		return checkTargetRange(this, target, getWeaponStats().getFov(), range);
	}
	
	protected boolean checkCanSee(Entity target) {
		// throWaterRange+1 is needed for ground radar to see boats in water
		return UtilEntity.canEntitySeeEntity(this, target, Config.COMMON.maxBlockCheckDepth.get(),
				getWeaponStats().getSeeThroWater()+1, getWeaponStats().getSeeThroBlock());
	}
	
	private void engineSound() {
		UtilClientSafeSounds.dopplerSound(this, 
				getWeaponStats().getEngineSound(level().registryAccess()), 0.8F, 1.0F, 
				DSCPhyCons.getVelSound(), false, 200.0);
	}

    private boolean canSonicBoom() {
        if (didSonicBoom) return false;
        if (!isClientSide()) return false;
        Entity owner = getOwner();
        return owner == null || !owner.equals(Minecraft.getInstance().player);
    }

	private void sonicBoomSound() {
		didSonicBoom = UtilClientSafeSounds.missileSonicBoom(this);
	}
	
	@Override
	public void checkDespawn() {
		
	}
	
	public void tickOutRange() {
		if (isRemoved() && !isDiscardedButTicking()) return; // already killed, don't re-tick
		xRotO = getXRot(); 
		yRotO = getYRot();
		// uses special kill override function. don't change to discard.
		if (tickCount > getMaxAge()) { 
			//System.out.println("old");
			kill();
			return;
		}
		if (dieIfNoTargetOutsideTickRange() && targetPos == null) {
			//System.out.println("no target pos");
			kill();
			return;
		}
		if (tickCount > 100 && getDeltaMovement().length() < 0.1) {
			if (!isRemoved()) kill();
			return;
		}
		
		// CRITICAL: Kill missiles that fly underground to prevent them getting stuck
		if (getY() < -60) {
			kill();
			return;
		}
		
        checkInterceptTarget();
		if (isRemoved() && !isDiscardedButTicking()) return;
		tickGuide();
		tickSetMove();
		// tickCheckCollide() skips removed entities, so we do a manual block+fuse check
		tickOutRangeCollide();
		if (isRemoved() && !isDiscardedButTicking()) return; // hit something
		setPos(position().add(getDeltaMovement()));
		++tickCount;
		
		// Sync position and velocity to clients every 5 ticks to prevent missiles from appearing stuck
		if (tickCount % 5 == 0) {
			syncPacketPositionCodec(getX(), getY(), getZ());
		}
	}
	
	public boolean dieIfNoTargetOutsideTickRange() {
		return true;
	}

	/**
	 * Collision check for missiles ticking outside entity-ticking range (discardedButTicking).
	 * tickCheckCollide() bails early on isRemoved(), so we bypass that here.
	 */
	protected void tickOutRangeCollide() {
		Vec3 move = getDeltaMovement();
		Vec3 pos = position();
		
		// CRITICAL: Only check block collision if chunk is already loaded
		// This prevents forcing chunk generation when missiles fly through unloaded areas
		ChunkPos chunkPos = new ChunkPos(
			new net.minecraft.core.BlockPos((int)pos.x, (int)pos.y, (int)pos.z)
		);
		if (getWorld().hasChunk(chunkPos.x, chunkPos.z)) {
			// Block collision
			net.minecraft.world.phys.BlockHitResult blockHit = getWorld().clip(
					new net.minecraft.world.level.ClipContext(pos, pos.add(move),
							net.minecraft.world.level.ClipContext.Block.COLLIDER,
							getFluidClipContext(), this));
			if (blockHit.getType() != net.minecraft.world.phys.HitResult.Type.MISS) {
				setPos(blockHit.getLocation());
				kill();
				return;
			}
		}
		// If chunk not loaded, skip block collision — missile flies through
		
		// Fuse distance check against target entity
		if (target != null && !target.isRemoved()) {
			double fuseSqr = getWeaponStats().getFuseDist() * getWeaponStats().getFuseDist();
			if (distanceToSqr(target) <= fuseSqr) {
				kill();
			}
		} else if (targetPos != null) {
			double fuseSqr = getWeaponStats().getFuseDist() * getWeaponStats().getFuseDist();
			if (distanceToSqr(targetPos.x, targetPos.y, targetPos.z) <= fuseSqr) {
				kill();
			}
		}
	}
	
	@Override
	protected void tickSetMove() {
		Vec3 cm = getDeltaMovement();
		double cv = cm.length();
		double max = getSpeed();
		double B = getBleed() * UtilVehicleEntity.getAirDensity(this);
        B *= DSCPhyCons.MISSILE_BLEED_SCALE / adjustedSpeedScale();
		double turnBleed = B * (Math.abs(getXRot()-xRotO)+Math.abs(getYRot()-yRotO));
		double airRes = B * cv * DSCPhyCons.MISSILE_AIR_RES_SCALE / adjustedSpeedScale();
		double vel = cv - turnBleed - airRes;
		if (getAge() <= getFuelTicks()) vel += getAcceleration();
		double ga = Math.sin(Mth.DEG_TO_RAD*UtilAngles.getPitch(cm))*getGravityAcc()*DSCPhyCons.MISSILE_GRAV_ACC_SCALE;
		double gravityAcc = Math.max(0, ga);
		vel += gravityAcc;
		if (vel > max) vel = max;
		else if (vel < 0.1) vel = 0.1;
		Vec3 nm = getLookAngle().scale(vel);
		setDeltaMovement(nm);
	}

    private double adjustedSpeedScale() {
        if (getStats().isUseSpeedScale()) return 8 * DSCPhyCons.getIRLScale();
        return 1;
    }

	@Override
	protected void tickSetAngle() {

	}
	
	public void guideToPosition() {
		if (targetPos == null) return;
		Vec3 goal_dir = targetPos.subtract(position());
		Vec3 cur_dir = getLookAngle();
		float deg_diff = (float)UtilGeometry.angleBetweenDegrees(goal_dir, cur_dir);
		float rot = getTurnDegrees();
		if (deg_diff <= rot) {
			setXRot(UtilAngles.getPitch(goal_dir));
			setYRot(UtilAngles.getYaw(goal_dir));
		} else {
			Vec3 P = cur_dir.cross(goal_dir).normalize();
			Vec3 new_dir = UtilAngles.rotateVector(cur_dir, new QuaternionF(
					UtilGeometry.convertVector(P), 
					rot, true));
			setXRot(UtilAngles.getPitch(new_dir));
			setYRot(UtilAngles.getYaw(new_dir));
		}
	}
	
	public float getTurnDegrees() {
		return (float)getDeltaMovement().length() / getTurnRadius() * Mth.RAD_TO_DEG;
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
		if (isRemoved()) return false;
		if (equals(source.getDirectEntity())) return false;
		if (isAlliedTo(source.getEntity())) return false;
		kill();
		return true;
	}
	
	@Override
	public boolean ignoreExplosion() {
		return false;
	}
	
	public double getAcceleration() {
		return getWeaponStats().getAcceleration();
	}
	
	public double getBleed() {
		return getWeaponStats().getBleed();
	}
	
	public int getFuelTicks() {
		return getWeaponStats().getFuelTicks();
	}
	
	public float getTurnRadius() {
		return getWeaponStats().getTurnRadius();
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
		// Notify manager that missile is dead
		if (!isClientSide()) MissileChunkLoadingManager.markMissileDead(this);
	}
	
	@Override
	public void invokeRevive() {
        UtilVehicleEntity.revive(this);
		discardedButTicking = false;
		// Force sync of velocity, position AND rotation when missile re-enters loaded chunks
		// This prevents the "horizontal missile" and "teleport back" effects
		syncPacketPositionCodec(getX(), getY(), getZ());
		// Ensure rotation is set correctly (important for rendering)
		// Set both current and previous rotation to prevent interpolation glitches
		xRotO = getXRot();
		yRotO = getYRot();
		setRot(getYRot(), getXRot());
	}

	@Override
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
	
	@Override
	public Fluid getFluidClipContext() {
		return ClipContext.Fluid.SOURCE_ONLY;
	}
	
	@Override
	public int minExplodeAge() {
		return 20;
	}
	
	@Override
	protected WeaponDamageSource getImpactDamageSource() {
		return WeaponDamageSource.WeaponDamageType.MISSILE_CONTACT.getSource(getOwner(), this);
	}

	@Override
	protected WeaponDamageSource getExplosionDamageSource() {
		return WeaponDamageSource.WeaponDamageType.MISSILE.getSource(getOwner(), this);
	}
	
	@Override
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        if (x == getX() && y == getY() && z == getZ()) return;
        lerpX = x; lerpY = y; lerpZ = z;
        lerpYRot = yaw; lerpXRot = pitch;
        // Use smaller lerp steps for missiles to reduce "rubber banding" effect
        // when missile re-enters loaded chunks after being out of range
        lerpSteps = Math.max(1, Math.min(5, posRotationIncrements / 2));
    }
	
	private void tickLerp() {
		if (!isClientSide()) {
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
	
	@Override
	public boolean isPickable() {
		return true;
	}
	
	@Override
	public void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (isClientSide()) return;
		if (getWorld().getGameRules().getBoolean(DSCGameRules.BROADCAST_MISSILE_HIT)) {
			Entity entity = result.getEntity();
			if (entity == null) return;
			ServerPlayer targetPlayer = null;
			if (entity instanceof ServerPlayer tsp) targetPlayer = tsp;
			else if (entity.getControllingPassenger() instanceof ServerPlayer tsp) targetPlayer = tsp;
			if (targetPlayer == null) return;
			Entity owner = getOwner();
			if (!(owner instanceof ServerPlayer ownerPlayer)) return;
			MutableComponent message = UtilMCText.translatable("info.dscombat.missile_impacted_player",
					ownerPlayer.getDisplayName(), targetPlayer.getDisplayName());
			boolean teamOnly = getWorld().getGameRules().getBoolean(DSCGameRules.BROADCAST_MISSILE_HIT_TEAM_ONLY);
			List<ServerPlayer> players = getWorld().getServer().getPlayerList().getPlayers();
			for (ServerPlayer player : players) {
				if (teamOnly && (ownerPlayer.getTeam() == null || player.getTeam() == null || 
						!ownerPlayer.getTeam().getName().equals(player.getTeam().getName()))) continue;
				player.displayClientMessage(message, false);
			}
		}
	}
	
	@Override
	public WeaponStats.WeaponClientImpactType getClientImpactType() {
		return WeaponStats.WeaponClientImpactType.MED_MISSILE_EXPLODE;
	}

}
