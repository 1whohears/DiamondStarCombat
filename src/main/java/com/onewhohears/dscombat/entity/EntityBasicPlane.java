package com.onewhohears.dscombat.entity;

import java.util.List;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityBasicPlane extends Entity {
	
	public static final EntityDataAccessor<Integer> MAX_HEALTH = SynchedEntityData.defineId(EntityBasicPlane.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> HEALTH = SynchedEntityData.defineId(EntityBasicPlane.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Float> MAX_SPEED = SynchedEntityData.defineId(EntityBasicPlane.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> THROTTLE = SynchedEntityData.defineId(EntityBasicPlane.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Quaternion> Q = SynchedEntityData.defineId(EntityBasicPlane.class, DataSerializers.QUATERNION);
	public static final EntityDataAccessor<Boolean> FREE_LOOK = SynchedEntityData.defineId(EntityBasicPlane.class, EntityDataSerializers.BOOLEAN);
	public Quaternion clientQ = new Quaternion(Quaternion.ONE);
	public Quaternion prevQ = new Quaternion(Quaternion.ONE);
	
	public boolean inputThrottleUp, inputThrottleDown, inputPitchUp, inputPitchDown;
	public boolean inputRollLeft, inputRollRight, inputYawLeft, inputYawRight;
	public boolean inputMouseMode, inputFlare;
	public float prevXRot, prevYRot, zRot, prevZRot;
	
	private int lerpSteps;
	private double lerpX, lerpY, lerpZ, lerpXRot, lerpYRot, lerpRotRoll;
	private boolean prevInputMouseMode;
	
	public EntityBasicPlane(EntityType<? extends EntityBasicPlane> entity, Level level) {
		super(entity, level);
		this.blocksBuilding = true;
	}
	
	public EntityBasicPlane(EntityType<? extends EntityBasicPlane> entity, Level level, double x, double y, double z) {
		this(entity, level);
		setPos(x, y, z);
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
		double test = getDeltaMovement().length();
		if (Double.isNaN(test)) {
            setDeltaMovement(Vec3.ZERO);
        }
		if (test < 0.0001 && test > -0.0001) {
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
		EulerAngles a = getAngles(q);
		double pitchRad = Math.toRadians(a.pitch);
    	double yawRad = Math.toRadians(a.yaw);
    	double rollRad = Math.toRadians(a.roll);
    	System.out.println("########");
    	System.out.println("is client "+level.isClientSide);
    	System.out.println("Q = "+q);
    	Vec3 motion = getDeltaMovement();
    	System.out.println("prev motion: "+motion);
    	Vec3 newForce = getWeightForce();
    	newForce = newForce.add(getLiftForce(pitchRad, yawRad, rollRad, motion));
    	newForce = newForce.add(getThrustForce(pitchRad, yawRad));
    	newForce = newForce.add(getDragForce(motion));
    	motion = motion.add(newForce);
		if (onGround && motion.y < 0) motion = new Vec3(motion.x, 0, motion.z);
		double speed = motion.length();
		if (speed > getMaxSpeed()) motion = motion.scale(getMaxSpeed() / speed);
		System.out.println("new motion = "+motion);
		setDeltaMovement(motion);
		move(MoverType.SELF, getDeltaMovement());
		/*if (this.isControlledByLocalInstance()) {
			if (level.isClientSide) {
				this.controlDirection(q);
			}
			EulerAngles a = getAngles(q);
			double pitchRad = Math.toRadians(a.pitch);
	    	double yawRad = Math.toRadians(a.yaw);
	    	double rollRad = Math.toRadians(a.roll);
			System.out.println("########");
			Vec3 total = Vec3.ZERO;
			total = total.add(getThrustForce(pitchRad, yawRad));
			total = total.add(getDragForce(getDeltaMovement()));
			total = total.add(getLiftForce(pitchRad, yawRad, rollRad, getDeltaMovement()));
			total = total.add(getWeightForce());
			total = getDeltaMovement().add(total);
			if (onGround && total.y < 0) total = new Vec3(total.x, 0, total.z);
			double speed = total.length();
			if (speed > getMaxSpeed()) total = total.scale(getMaxSpeed() / speed);
			System.out.println("total force = "+total);
			this.setDeltaMovement(total);
			this.move(MoverType.SELF, getDeltaMovement());
			/*messageToPlayer("Throttle: "+this.getCurrentThrottle());
			messageToPlayer("Motion: "+motion);
			messageToPlayer("Pit = "+a.pitch);
			messageToPlayer("Rol = "+a.roll);
			messageToPlayer("Yaw = "+a.yaw);*/
		//} else {
			/*EulerAngles a = getAngles(q);
			double pitchRad = Math.toRadians(a.pitch);
	    	double yawRad = Math.toRadians(a.yaw);
	    	double rollRad = Math.toRadians(a.roll);*/
			/*motion = motion.add(getDragForce(motion));
			motion = motion.add(getLiftForce(pitchRad, yawRad, rollRad));
			motion = motion.add(getWeightForce());
			if (onGround) motion = new Vec3(motion.x, 0, motion.z);*/
			//Vec3 motion = getDeltaMovement();
			//this.setDeltaMovement(motion);
		//}
        q = UtilAngles.normalizeQuaternion(q);
        setPrevQ(getClientQ());
        setQ(q);
        if (level.isClientSide && isControlledByLocalInstance()) {
        	setClientQ(q);
        	// send packet not needed?
        }
		tickLerp();
	}
	
	private EulerAngles getAngles(Quaternion q) {
		EulerAngles angles = UtilAngles.toEulerAngles(q);
		setXRot((float)angles.pitch);
		setYRot((float)angles.yaw);
		zRot = (float)angles.roll;
		return angles;
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
	
	private void messageToPlayer(String message) {
		if (!this.level.isClientSide) return;
		Minecraft.getInstance().player.displayClientMessage(new TextComponent(message), false);
	}
	
	public Vec3 getThrustForce(double pitchRad, double yawRad) {
		Vec3 direction = UtilAngles.getRollAxis(pitchRad, yawRad);
		Vec3 thrustForce = direction.scale(getThrust());
		System.out.println("thrust force = "+thrustForce);
		return thrustForce;
	}
	
	public double getThrust() {
		float throttle = getCurrentThrottle();
		return throttle * 0.1;
	}
	
	public Vec3 getDragForce(Vec3 motion) {
		Vec3 direction = motion.normalize();
		Vec3 dragForce = direction.scale(-getDrag(motion));
		System.out.println("drag force = "+dragForce);
		return dragForce;
	}
	
	public double getDrag(Vec3 motion) {
		double speed = motion.length();
		if (onGround && getCurrentThrottle() <= 0) {
			if (speed > 0.1) return 0.1;
			return speed;
		}
		// Drag = (drag coefficient) * (air pressure) * (speed)^2 * (wing surface area) / 2
		double dc = 0.01;
		double air = 1;
		double speedSqr = speed * speed;
		double wing = 1;
		return dc * air * speedSqr * wing / 2;
	}
	
	public Vec3 getLiftForce(double pitchRad, double yawRad, double rollRad, Vec3 motion) {
		Vec3 direction = UtilAngles.getYawAxis(pitchRad, yawRad, rollRad);
		//System.out.println("lift direction = "+direction);
		Vec3 u = motion;
		Vec3 v = UtilAngles.getRollAxis(pitchRad, yawRad);
		double vl2 = v.lengthSqr();
		double zSpeedSqr = v.scale(u.dot(v) / vl2).lengthSqr();
		Vec3 liftForce = direction.scale(getLift(zSpeedSqr));
		System.out.println("lift force = "+liftForce);
		return liftForce;
	}
	
	public double getLift(double zSpeedSqr) {
		// Lift = (angle of attack coefficient) * (air density) * (speed)^2 * (wing surface area) / 2
		double ac = 0.05;
		double air = 1;
		double speedSqr = zSpeedSqr;
		double wing = 1;
		return ac * air * speedSqr * wing / 2;
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
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		} else if (!this.level.isClientSide) {
			return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
		} else {
			return InteractionResult.SUCCESS;
		}
	}
	
	@Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
	}
	
	@Override
    public void positionRider(Entity passenger) {
		if (!this.hasPassenger(passenger)) return;
		Vec3 pos = new Vec3(getX(), getY(), getZ());
		Vec3 seatPos = new Vec3(0, 0, 0);
		passenger.setPos(pos.add(seatPos));
		/*passenger.setYBodyRot(-getYRot());
		passenger.setYHeadRot(-getYRot());
		passenger.setXRot(-getXRot());*/
	}
	
	@Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
		return super.getDismountLocationForPassenger(livingEntity);
	}
	
	@Override
    protected boolean canAddPassenger(Entity passenger) {
		return getPassengers().size() < 1;
	}
	
	@Nullable
	@Override
    public Entity getControllingPassenger() {
        List<Entity> list = getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }
	
	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}
	
	@Override
    protected boolean canRide(Entity entityIn) {
        return true;
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return false;
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
	protected void readAdditionalSaveData(CompoundTag p_20052_) {
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag p_20139_) {
		
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
