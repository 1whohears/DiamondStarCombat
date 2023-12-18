package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityHelicopter extends EntityVehicle {
	
	public static final EntityDataAccessor<Float> ACC_FORWARD = SynchedEntityData.defineId(EntityHelicopter.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ACC_SIDE = SynchedEntityData.defineId(EntityHelicopter.class, EntityDataSerializers.FLOAT);
	
	private float propellerRot, propellerRotOld;
	
	public EntityHelicopter(EntityType<? extends EntityHelicopter> entity, Level level, ImmutableVehicleData vehicleData) {
		super(entity, level, vehicleData);
	}
	
	@Override
	public AircraftType getAircraftType() {
		return AircraftType.HELICOPTER;
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(ACC_FORWARD, 0f);
		entityData.define(ACC_SIDE, 0f);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setAccForward(compound.getFloat("accForward"));
		this.setAccSide(compound.getFloat("accSide"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putFloat("accForward", getAccForward());
		compound.putFloat("accSide", getAccSide());
	}
	
	@Override
	public void clientTick() {
		super.clientTick();
		float th = getCurrentThrottle();
		propellerRotOld = propellerRot;
		propellerRot += th * vehicleData.spinRate;
	}
	
	@Override
	public void tickGround(Quaternion q) {
		addFrictionForce(kineticFric);
	}
	
	@Override
	public double getDriveAcc() {
		return 0;
	}
	
	@Override
	public void tickAir(Quaternion q) {
		if (inputs.special && isOperational()) {
			float max_th = getMaxPushThrust();
			if (max_th != 0) setCurrentThrottle((float)-getWeightForce().y / max_th);
			setDeltaMovement(getDeltaMovement().multiply(1, 0.95, 1));
		}
		super.tickAir(q);
		Vec3 motion = getDeltaMovement();
		if (!isDriverCameraLocked() && isOperational()) {
			motion = motion.multiply(0.95, 1, 0.95);
			EulerAngles a = UtilAngles.toDegrees(q);
			// pitch forward backward
			Vec3 fDir = UtilAngles.rotationToVector(a.yaw, 0);
			motion = motion.add(fDir.scale(inputs.pitch).scale(getAccForward()));
			// roll left right
			Vec3 sDir = UtilAngles.rotationToVector(a.yaw+90, 0);
			motion = motion.add(sDir.scale(inputs.roll).scale(getAccSide()));
		}
		setDeltaMovement(motion);
		// IDEA 4 helicopter hover auto pilot mode with inputSpecial2
	}
	
	@Override
	public void controlDirection(Quaternion q) {
		super.controlDirection(q);
	}
	
	@Override
	public void directionGround(Quaternion q) {
		if (!isOperational()) return;
		flatten(q, 4f, 4f, true);
	}
	
	@Override
	public void directionAir(Quaternion q) {
		super.directionAir(q);
		if (!isOperational()) return;
		addMomentY(inputs.yaw * getYawTorque(), true);
		if (isDriverCameraLocked()) {
			addMomentX(inputs.pitch * getPitchTorque(), true);
			addMomentZ(inputs.roll * getRollTorque(), true);
		} else flatten(q, getMaxDeltaPitch(), getMaxDeltaRoll(), false);
	}

	@Override
	public Vec3 getThrustForce(Quaternion q) {
		Vec3 direction = UtilAngles.getYawAxis(q);
		Vec3 thrustForce = direction.scale(getPushThrustMag());
		return thrustForce;
	}
	
	@Override
	public float getMaxPushThrust() {
		return getMaxSpinThrust() * (float)airPressure * vehicleData.heliLiftFactor;
	}
	
	public float getPropellerRotation(float partialTicks) {
		return Mth.lerp(partialTicks, propellerRotOld, propellerRot);
	}
	
	@Override
	public boolean isLandingGear() {
		if (vehicleData.alwaysLandingGear) return true;
    	return super.isLandingGear();
    }
	
	public float getAccForward() {
		return entityData.get(ACC_FORWARD);
	}
	
	public void setAccForward(float acc) {
		entityData.set(ACC_FORWARD, acc);
	}
	
	public float getAccSide() {
		return entityData.get(ACC_SIDE);
	}
	
	public void setAccSide(float acc) {
		entityData.set(ACC_SIDE, acc);
	}
	
	@Override
	public boolean isCustomBoundingBox() {
    	return true;
    }

	@Override
	public boolean canBrake() {
		return false;
	}

	@Override
	public boolean canToggleLandingGear() {
		return !vehicleData.alwaysLandingGear;
	}
	
	@Override
	public boolean canHover() {
    	return true;
    }

}
