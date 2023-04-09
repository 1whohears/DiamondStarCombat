package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.AircraftTextures;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntityHelicopter extends EntityAircraft {
	
	public static final float CO_LIFT = 2.75f;
	
	public static final EntityDataAccessor<Float> ACC_FORWARD = SynchedEntityData.defineId(EntityHelicopter.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ACC_SIDE = SynchedEntityData.defineId(EntityHelicopter.class, EntityDataSerializers.FLOAT);
	
	private final float propellerRate = 3.141f;
	private float propellerRot, propellerRotOld;
	private final boolean alwaysLandingGear;
	
	public EntityHelicopter(EntityType<? extends EntityHelicopter> entity, Level level, AircraftTextures textures,
			RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item, boolean alwaysLandingGear) {
		super(entity, level, textures, engineSound, item, false);
		this.alwaysLandingGear = alwaysLandingGear;
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
		propellerRot += th * propellerRate;
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
		if (!level.isClientSide && inputSpecial && isOperational()) {
			float max_th = getMaxThrust();
			if (max_th != 0) setCurrentThrottle((float)-getWeightForce().y / max_th);
			setDeltaMovement(getDeltaMovement().multiply(1, 0.90, 1));
		}
		super.tickAir(q);
		Vec3 motion = getDeltaMovement();
		if (isFreeLook() && isOperational()) {
			motion = motion.multiply(0.95, 1, 0.95);
			EulerAngles a = UtilAngles.toDegrees(q);
			// pitch forward backward
			Vec3 fDir = UtilAngles.rotationToVector(a.yaw, 0);
			motion = motion.add(fDir.scale(inputPitch).scale(getAccForward()));
			// roll left right
			Vec3 sDir = UtilAngles.rotationToVector(a.yaw+90, 0);
			motion = motion.add(sDir.scale(inputRoll).scale(getAccSide()));
		}
		setDeltaMovement(motion);
	}
	
	@Override
	public void controlDirection(Quaternion q) {
		super.controlDirection(q);
	}
	
	@Override
	public void directionGround(Quaternion q) {
		flatten(q, 5f, 5f);
		torqueY = 0;
	}
	
	@Override
	public void directionAir(Quaternion q) {
		super.directionAir(q);
		if (!isOperational()) return;
		addTorqueY(inputYaw * getAccelerationYaw(), true);
		if (!isFreeLook()) {
			addTorqueX(inputPitch * getAccelerationPitch(), true);
			addTorqueZ(inputRoll * getAccelerationRoll(), true);
		} else flatten(q, getMaxDeltaPitch(), getMaxDeltaRoll());
	}

	@Override
	public Vec3 getThrustForce(Quaternion q) {
		Vec3 direction = UtilAngles.getYawAxis(q);
		Vec3 thrustForce = direction.scale(getThrustMag());
		return thrustForce;
	}
	
	@Override
	public double getThrustMag() {
		return super.getThrustMag();
	}
	
	@Override
	public float getMaxThrust() {
		return super.getMaxThrust() * (float)UtilEntity.getAirPressure(getY()) * CO_LIFT;
	}
	
	public float getPropellerRotation(float partialTicks) {
		return Mth.lerp(partialTicks, propellerRotOld, propellerRot);
	}
	
	@Override
	public boolean isLandingGear() {
		if (alwaysLandingGear) return true;
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

}
