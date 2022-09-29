package com.onewhohears.dscombat.entity.aircraft;

import java.util.List;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.data.PartData;
import com.onewhohears.dscombat.data.PartsManager;
import com.onewhohears.dscombat.data.RadarData;
import com.onewhohears.dscombat.data.WeaponData;
import com.onewhohears.dscombat.data.WeaponSystem;
import com.onewhohears.dscombat.entity.aircraft.parts.EntitySeat;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;

import net.minecraft.client.model.EntityModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraftforge.network.NetworkHooks;

public abstract class EntityAbstractAircraft extends Entity {
	
	public static final EntityDataAccessor<Integer> MAX_HEALTH = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> HEALTH = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Float> MAX_SPEED = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> THROTTLE = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Quaternion> Q = SynchedEntityData.defineId(EntityAbstractAircraft.class, DataSerializers.QUATERNION);
	public static final EntityDataAccessor<Boolean> FREE_LOOK = SynchedEntityData.defineId(EntityAbstractAircraft.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<PartsManager> PARTS_MANAGER = SynchedEntityData.defineId(EntityAbstractAircraft.class, DataSerializers.PARTS_MANAGER);
	//public Quaternion clientQ = Quaternion.ONE.copy();
	public Quaternion prevQ = Quaternion.ONE.copy();
	
	public boolean inputThrottleUp, inputThrottleDown, inputPitchUp, inputPitchDown;
	public boolean inputRollLeft, inputRollRight, inputYawLeft, inputYawRight;
	public boolean inputMouseMode, inputFlare, inputShoot, inputSelect;
	public float prevXRot, prevYRot, zRot, prevZRot;
	
	private int lerpSteps, lerpStepsQ;
	private double lerpX, lerpY, lerpZ, lerpXRot, lerpYRot, lerpZRot;
	private boolean prevInputMouseMode, prevInputSelect;
	
	public EntityAbstractAircraft(EntityType<? extends EntityAbstractAircraft> entity, Level level) {
		super(entity, level);
		this.blocksBuilding = true;
	}
	
	@Override
	protected void defineSynchedData() {
		entityData.define(MAX_HEALTH, 10);
        entityData.define(HEALTH, 10);
		entityData.define(MAX_SPEED, 1.5f);
		entityData.define(THROTTLE, 0.0f);
		entityData.define(Q, Quaternion.ONE);
		entityData.define(FREE_LOOK, false);
		entityData.define(PARTS_MANAGER, new PartsManager());
	}
	
	@Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (Q.equals(key) && level.isClientSide() && !isControlledByLocalInstance()) {
            if (firstTick) {
                lerpStepsQ = 0;
                //setClientQ(getQ());
                setPrevQ(getQ());
            } else {
                lerpStepsQ = 10;
            }
        }
        // TODO check of update synch data to see how often it updates the part data
    }
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		Quaternion q = new Quaternion(getXRot(), getYRot(), 0, true);
		setQ(q);
		setPrevQ(q);
		//setClientQ(q);
		this.setPartsManager(new PartsManager(compound));
		// TODO read all the other synced data
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		this.getPartsManager().write(compound);
		// TODO add all the other synced data
	}
	
	public void init() {
		setupAircraftParts();
	}
	
	@Override
	public void tick() {
		//System.out.println(this.tickCount+" "+this.level);
		if (this.firstTick) init();
		super.tick();
		if (!level.isClientSide) this.getPartsManager().getWeapons().tick();
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
		Quaternion q = getQ();
		/*Quaternion q;
        if (level.isClientSide) {
            q = getClientQ();
        } else {
            q = getQ();
        }*/
		setPrevQ(q);
		controlDirection(q);
    	tickMovement(q);
    	motionClamp();
    	//System.out.println("MOTION "+getDeltaMovement());
		move(MoverType.SELF, getDeltaMovement());
		q = UtilAngles.normalizeQuaternion(q);
		EulerAngles angles = UtilAngles.toDegrees(q);
		setXRot(-(float)angles.pitch);
		setYRot(-(float)angles.yaw);
		zRot = (float)angles.roll;
		//setPrevQ(getClientQ());
        setQ(q);
        /*if (level.isClientSide && isControlledByLocalInstance()) {
            setClientQ(q);
        }*/
        controlSystem();
		tickLerp();
		/*System.out.println("######### client "+this.level.isClientSide);
		System.out.println("P "+this.getPrevQ());
		System.out.println("C "+this.getClientQ());
		System.out.println("Q "+this.getQ());*/
	}
	
	public void motionClamp() {
		Vec3 motion = getDeltaMovement();
		double vel = motion.length();
		double max = getMaxSpeed();
		if (vel > max) motion = motion.scale(max / vel);
		setDeltaMovement(motion);
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
		motion = motion.add(getWeightForce());
		motion = motion.add(getThrustForce(q));
		motion = motion.add(getFrictionForce());
		setDeltaMovement(motion);
	}
	
	public void tickAir(Quaternion q) {
		Vec3 motion = getDeltaMovement();
		motion = motion.add(getWeightForce());
		motion = motion.add(getThrustForce(q));
		motion = motion.add(getDragForce(q));
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
		double dc = 0.015;
		double air = getAirPressure();
		double speedSqr = getDeltaMovement().lengthSqr();
		double wing = getSurfaceArea();
		return dc * air * speedSqr * wing / 2;
	}
	
	public double getAirPressure() {
		double space = 300;
		double water = 64;
		double scale = 1;
		if (getY() > space) return 0;
		if (getY() < water) return scale;
		return -(scale/(space-water)) * (getY()-water) + scale;
	}
	
	public double getSurfaceArea() {
		return 1;
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
		if (speed < 0.05) return speed;
		return 0.05;
	}
	
	public Vec3 getWeightForce() {
		return new Vec3(0, -getWeight(), 0);
	}
	
	public double getWeight() {
		return 0.05;
	}
	
	public void controlSystem() {
		if (level.isClientSide) return;
		Entity controller = this.getControllingPassenger();
		if (controller == null) return;
		RadarData radar = getRadar();
		if (radar != null) radar.tickUpdateTargets(this);
		if (this.inputShoot) {
			WeaponSystem system = this.getPartsManager().getWeapons();
			WeaponData data = system.getSelected();
			if (data == null) return;
			data.shoot(level, this, controller, UtilAngles.getRollAxis(getQ()), this.getQ());
			if (data.isFailedLaunch() && data.getFailedLaunchReason() != null) {
				System.out.println(data.getFailedLaunchReason());
				if (controller instanceof ServerPlayer player) {
					player.displayClientMessage(new TextComponent(
							data.getFailedLaunchReason()), true);
				}
			}
		}
	}
	
	public RadarData getRadar() {
		PartData rPart = this.getPartsManager().get("main-radar");
		if (rPart == null) return null;
		return (RadarData) rPart;
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
			this.lerpStepsQ = 0;
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
		/*if (lerpStepsQ > 0) {
			this.setPrevQ(this.getClientQ());
			this.setClientQ(UtilAngles.lerpQ(1f / lerpSteps, this.getClientQ(), this.getQ()));
			--lerpStepsQ;
		} else if (lerpStepsQ == 0) {
			this.setPrevQ(this.getClientQ());
			this.setClientQ(this.getQ());
			--lerpStepsQ;
		}*/
	}
	
	public void updateControls(boolean throttleUp, boolean throttleDown, boolean pitchUp, boolean pitchDown,
			boolean rollLeft, boolean rollRight, boolean yawLeft, boolean yawRight,
			boolean mouseMode, boolean flare, boolean shoot, boolean select) {
		this.inputThrottleUp = throttleUp;
		this.inputThrottleDown = throttleDown;
		this.inputPitchUp = pitchUp;
		this.inputPitchDown = pitchDown;
		this.inputRollLeft = rollLeft;
		this.inputRollRight = rollRight;
		this.inputYawLeft = yawLeft;
		this.inputYawRight = yawRight;
		this.inputFlare = flare;
		this.inputMouseMode = mouseMode;
		if (!prevInputMouseMode && inputMouseMode) 
			setFreeLook(!isFreeLook());
		prevInputMouseMode = inputMouseMode;
		this.inputShoot = shoot;
		this.inputSelect = select;
		if (!this.prevInputSelect && this.inputSelect && !this.level.isClientSide) 
			getPartsManager().getWeapons().selectNextWeapon();
		this.prevInputSelect = this.inputSelect;
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
		this.inputFlare = false;
		this.inputMouseMode = false;
		this.inputShoot = false;
		this.inputSelect = false;
		this.setCurrentThrottle(0);
	}
	
	public boolean isFreeLook() {
    	return entityData.get(FREE_LOOK);
    }
    
    public void setFreeLook(boolean freeLook) {
    	entityData.set(FREE_LOOK, freeLook);
    }
	
    /**
     * register parts before calling super
     */
	protected void setupAircraftParts() {
		this.getPartsManager().setupParts(this);
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
    public void positionRider(Entity passenger) {
		//if (!this.hasPassenger(passenger)) return;
		//System.out.println("POSITION RIDER "+passenger);
		if (!(passenger instanceof EntitySeat seat)) return;
 		Vec3 pos = position();
		Vec3 seatPos = UtilAngles.rotateVector(seat.getRelativePos(), getQ());
		passenger.setPos(pos.add(seatPos));
	}
	
	/*@Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
		return super.getDismountLocationForPassenger(livingEntity);
	}*/
	
	@Nullable
	@Override
    public Entity getControllingPassenger() {
        List<Entity> list = getPassengers();
        if (list.isEmpty()) return null;
        if (!(list.get(0) instanceof EntitySeat seat)) return null;
        return seat.getPlayer();
    }
	
	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}
	
	@Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        System.out.println("AIRCRAFT ADDED PASSENGER "+passenger);
	}
	
	@Override
    protected boolean canAddPassenger(Entity passenger) {
		System.out.println("CAN ADD PASSENGER "+passenger);
		return passenger instanceof EntitySeat;
	}
	
	@Override
    protected boolean canRide(Entity entityIn) {
		System.out.println("CAN RIDE "+entityIn);
        return false;
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
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
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
    	return 5.0f;
    }
    
    public float getMaxDeltaYaw() {
    	return 2.0f;
    }
    
    public float getMaxDeltaRoll() {
    	return 10.0f;
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
        return entityData.get(Q).copy();
    }

    public void setQ(Quaternion q) {
        entityData.set(Q, q.copy());
    }

    /*public Quaternion getClientQ() {
        return clientQ.copy();
    }

    public void setClientQ(Quaternion q) {
        clientQ = q.copy();
    }*/

    public Quaternion getPrevQ() {
        return prevQ.copy();
    }

    public void setPrevQ(Quaternion q) {
        prevQ = q.copy();
    }
    
    public PartsManager getPartsManager() {
    	return entityData.get(PARTS_MANAGER);
    }
    
    public void setPartsManager(PartsManager manager) {
    	entityData.set(PARTS_MANAGER, manager);
    }
    
    /**
     * smaller number is better
     * @return value to be multiplied to the range of a radar
     */
    public double getStealth() {
    	return 1;
    }
    
    public abstract ResourceLocation getTexture();
    
    public abstract EntityModel<?> getModel();
}
