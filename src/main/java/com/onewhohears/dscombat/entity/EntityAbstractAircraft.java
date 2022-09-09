package com.onewhohears.dscombat.entity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.common.PacketHandler;
import com.onewhohears.dscombat.common.network.ServerBoundQPacket;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class EntityAbstractAircraft extends Entity {
	
	public static final EntityDataAccessor<Integer> MAX_HEALTH = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> HEALTH = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Float> MAX_SPEED = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> THROTTLE = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Quaternion> Q = SynchedEntityData.defineId(EntityAbstractAircraft.class, DataSerializers.QUATERNION);
	public static final EntityDataAccessor<Boolean> FREE_LOOK = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.BOOLEAN);
	public Quaternion clientQ = new Quaternion(Quaternion.ONE);
	public Quaternion prevQ = new Quaternion(Quaternion.ONE);
	
	public boolean inputThrottleUp, inputThrottleDown, inputPitchUp, inputPitchDown;
	public boolean inputRollLeft, inputRollRight, inputYawLeft, inputYawRight;
	public boolean inputMouseMode, inputFlare;
	public float prevXRot, prevYRot, zRot, prevZRot;
	
	private int lerpSteps;
	private double lerpX, lerpY, lerpZ, lerpXRot, lerpYRot, lerpRotRoll;
	private boolean prevInputMouseMode;
	
	private List<EntitySeat> seats;
	
	public EntityAbstractAircraft(EntityType<? extends EntityAbstractAircraft> entity, Level level) {
		super(entity, level);
		this.blocksBuilding = true;
		seats = new ArrayList<EntitySeat>();
	}
	
	@Override
	protected void defineSynchedData() {
		entityData.define(MAX_HEALTH, 10);
        entityData.define(HEALTH, 10);
		entityData.define(MAX_SPEED, 2.0f);
		entityData.define(THROTTLE, 0.0f);
		entityData.define(Q, Quaternion.ONE);
		entityData.define(FREE_LOOK, false);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (Double.isNaN(getDeltaMovement().length())) {
            setDeltaMovement(Vec3.ZERO);
        }
		if (this.getPassengers().size() == 0) {
			this.setCurrentThrottle(0);
		}
		prevXRot = getXRot();
		prevYRot = getYRot();
		prevZRot = zRot;
		if (level.isClientSide && !isControlledByLocalInstance()) {
			tickLerp();
		}
		Quaternion q;
		if (level.isClientSide) {
            q = getClientQ();
        } else {
            q = getQ();
        }
		controlDirection(q);
    	tickMovement(q);
		move(MoverType.SELF, getDeltaMovement());
		q = UtilAngles.normalizeQuaternion(q);
		EulerAngles angles = UtilAngles.toDegrees(q);
		setXRot((float)angles.pitch);
		setYRot((float)angles.yaw);
		zRot = (float)angles.roll;
        setPrevQ(getClientQ());
        setQ(q);
        if (level.isClientSide && isControlledByLocalInstance()) {
        	setClientQ(q);
        	PacketHandler.INSTANCE.sendToServer(new ServerBoundQPacket(q));
        }
		tickLerp();
	}
	
	public void controlDirection(Quaternion q) {
		if (this.getControllingPassenger() == null) this.resetControls();
		if (inputThrottleUp) this.increaseThrottle();
		if (inputThrottleDown) this.decreaseThrottle();
		float pitchInput = 0;
		if (inputPitchUp) pitchInput += 1;
		if (inputPitchDown) pitchInput -= 1;
		float rollInput = 0;
		if (inputRollLeft) rollInput -= 1;
		if (inputRollRight) rollInput += 1;
		float yawInput = 0;
		if (inputYawLeft) yawInput -= 1;
		if (inputYawRight) yawInput += 1;
		
		q.mul(new Quaternion(Vector3f.ZP, rollInput*getMaxDeltaRoll(), true));
		q.mul(new Quaternion(Vector3f.XN, pitchInput*getMaxDeltaPitch(), true));
		q.mul(new Quaternion(Vector3f.YN, yawInput*getMaxDeltaYaw(), true));
	}
	
	public void tickMovement(Quaternion q) {
		if (onGround) tickGround(q);
		else tickAir(q);
	}
	
	public void tickGround(Quaternion q) {
		Vec3 motion = getDeltaMovement();
		if (motion.y < 0) motion = new Vec3(motion.x, 0, motion.z);
		motion.add(getWeightForce());
		motion.add(getThrustForce(q));
		motion.add(getFrictionForce());
		setDeltaMovement(motion);
	}
	
	public void tickAir(Quaternion q) {
		Vec3 motion = getDeltaMovement();
		motion.add(getWeightForce());
		motion.add(getThrustForce(q));
		motion.add(getDragForce(q));
		setDeltaMovement(motion);
	}
	
	public Vec3 getThrustForce(Quaternion q) {
		Vec3 direction = UtilAngles.getRollAxis(q);
		Vec3 thrustForce = direction.scale(getThrust());
		//System.out.println("thrust force = "+thrustForce);
		return thrustForce;
	}
	
	public double getThrust() {
		float throttle = getCurrentThrottle();
		return throttle * 0.1;
	}
	
	public Vec3 getDragForce(Quaternion q) {
		Vec3 direction = getDeltaMovement().normalize();
		Vec3 dragForce = direction.scale(-getDrag(q));
		//System.out.println("drag force = "+dragForce);
		return dragForce;
	}
	
	public double getDrag(Quaternion q) {
		// Drag = (drag coefficient) * (air pressure) * (speed)^2 * (wing surface area) / 2
		double dc = 0.01;
		double air = 1;
		double speedSqr = getDeltaMovement().lengthSqr();
		double wing = 1;
		return dc * air * speedSqr * wing / 2;
	}
	
	public Vec3 getFrictionForce() {
		double x = getDeltaMovement().x;
		double z = getDeltaMovement().z;
		Vec3 direction = new Vec3(-x, 0, -z).normalize();
		return direction.scale(getFriction());
	}
	
	public double getFriction() {
		double x = getDeltaMovement().x;
		double z = getDeltaMovement().z;
		double speed = Math.sqrt(x*x + z*z);
		if (speed < 0.1) return speed;
		return 0.1;
	}
	
	public Vec3 getWeightForce() {
		return new Vec3(0, -getWeight(), 0);
	}
	
	public double getWeight() {
		return 0.05;
	}
	
	@Override
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        if (x == getX() && y == getY() && z == getZ()) return;
        lerpX = x; lerpY = y; lerpZ = z;
        lerpYRot = yaw; lerpXRot = pitch;
        lerpSteps = 10;
    }
	
	private void tickLerp() {
		if (this.isControlledByLocalInstance()) {
			this.lerpSteps = 0;
	        this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
		}
		if (this.lerpSteps > 0) {
			double d0 = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
	        double d1 = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
	        double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
	        double d3 = Mth.wrapDegrees(this.lerpYRot - (double)this.getYRot());
	        this.setYRot(this.getYRot() + (float)d3 / (float)this.lerpSteps);
	        this.setXRot(this.getXRot() + (float)(this.lerpXRot - (double)this.getXRot()) / (float)this.lerpSteps);
	        --this.lerpSteps;
	        this.setPos(d0, d1, d2);
	        this.setRot(this.getYRot(), this.getXRot());
		}
	}
	
	public void updateControls(boolean throttleUp, boolean throttleDown, boolean pitchUp, boolean pitchDown,
			boolean rollLeft, boolean rollRight, boolean yawLeft, boolean yawRight,
			boolean mouseMode, boolean flare) {
		this.inputThrottleUp = throttleUp;
		this.inputThrottleDown = throttleDown;
		this.inputPitchUp = pitchUp;
		this.inputPitchDown = pitchDown;
		this.inputRollLeft = rollLeft;
		this.inputRollRight = rollRight;
		this.inputYawLeft = yawLeft;
		this.inputYawRight = yawRight;
		this.inputMouseMode = mouseMode;
		this.inputFlare = flare;
		if (!prevInputMouseMode && inputMouseMode) setFreeLook(!isFreeLook());
		prevInputMouseMode = inputMouseMode;
	}
	
	public void resetControls() {
		this.inputThrottleUp = false;
		this.inputThrottleDown = false;
		this.inputPitchUp = false;
		this.inputPitchDown = false;
		this.inputRollLeft = false;
		this.inputRollRight = false;
		this.inputYawLeft = false;
		this.inputYawRight = false;
		this.inputMouseMode = false;
		this.inputFlare = false;
	}
	
	protected void addSeat(Vec3 pos) {
		List<Entity> passengers = getPassengers();
		for (Entity p : passengers) if (p instanceof EntitySeat seat) 
			if (seat.getRelativeSeatPos().equals(pos)) {
				System.out.println("seat already at "+pos);
				return;
			}
		EntitySeat seat = new EntitySeat(ModEntities.SEAT.get(), level);
		level.addFreshEntity(seat);
		this.addPassenger(seat);
		System.out.println("added seat at "+pos);
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		} else if (!this.level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.SUCCESS;
		}
	}
	
	@Override
    protected void addPassenger(Entity passenger) {
        passenger.startRiding(this);
	}
	
	@Override
    public void positionRider(Entity passenger) {
		//if (!this.hasPassenger(passenger)) return;
		if (!(passenger instanceof EntitySeat seat)) return;
 		Vec3 pos = new Vec3(getX(), getY(), getZ());
		Vec3 seatPos = UtilAngles.rotateVector(seat.getRelativeSeatPos(), getQ());
		passenger.setPos(pos.add(seatPos));
	}
	
	/*@Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
		return super.getDismountLocationForPassenger(livingEntity);
	}*/
	
	@Override
    protected boolean canAddPassenger(Entity passenger) {
		return passenger instanceof EntitySeat;
	}
	
	@Nullable
	@Override
    public Entity getControllingPassenger() {
        List<Entity> list = getPassengers();
        if (list.isEmpty()) return null;
        if (!(list.get(0) instanceof EntitySeat seat)) return null;
        List<Entity> list2 = seat.getPassengers();
        return list2.isEmpty() ? null : list2.get(0);
    }
	
	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}
	
	@Override
    protected boolean canRide(Entity entityIn) {
        return entityIn instanceof EntitySeat;
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return true;
    }
    
    @Override
    public double getPassengersRidingOffset() {
        return 0;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
	
    public float getMaxSpeed() {
    	return entityData.get(MAX_SPEED);
    }
    
    public void setMaxSpeed(float maxSpeed) {
    	entityData.set(MAX_SPEED, maxSpeed);
    }
    
    public float getCurrentThrottle() {
    	return entityData.get(THROTTLE);
    }
    
    public void setCurrentThrottle(float throttle) {
    	if (throttle > 1f) throttle = 1f;
    	if (throttle < 0) throttle = 0;
    	entityData.set(THROTTLE, throttle);
    }
    
    public float getThrottleIncreaseRate() {
    	return 0.02f;
    }
    
    public float getThrottleDecreaseRate() {
    	return 0.05f;
    }
    
    public float getMaxDeltaPitch() {
    	return 4.0f;
    }
    
    public float getMaxDeltaYaw() {
    	return 4.0f;
    }
    
    public float getMaxDeltaRoll() {
    	return 4.0f;
    }
    
    public void increaseThrottle() {
    	float current = this.getCurrentThrottle();
    	if (current < 1f) {
    		current += this.getThrottleIncreaseRate();
    		this.setCurrentThrottle(current);
    	}
    }
    
    public void decreaseThrottle() {
    	float current = this.getCurrentThrottle();
    	if (current > 0) {
    		current -= this.getThrottleDecreaseRate();
    		this.setCurrentThrottle(current);
    	}
    }
    
    public Quaternion getQ() {
        return new Quaternion(entityData.get(Q));
    }

    public void setQ(Quaternion q) {
        entityData.set(Q, q);
    }

    public Quaternion getClientQ() {
        return new Quaternion(clientQ);
    }

    public void setClientQ(Quaternion q) {
        clientQ = q;
    }

    public Quaternion getPrevQ() {
        return prevQ.copy();
    }

    public void setPrevQ(Quaternion q) {
        prevQ = q;
    }
    
    public boolean isFreeLook() {
    	return entityData.get(FREE_LOOK);
    }
    
    public void setFreeLook(boolean freeLook) {
    	entityData.set(FREE_LOOK, freeLook);
    }
}
