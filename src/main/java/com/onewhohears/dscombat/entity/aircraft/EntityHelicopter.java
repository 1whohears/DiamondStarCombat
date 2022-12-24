package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntityHelicopter extends EntityAircraft {
	
	public static final EntityDataAccessor<Float> ACC_FORWARD = SynchedEntityData.defineId(EntityHelicopter.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ACC_SIDE = SynchedEntityData.defineId(EntityHelicopter.class, EntityDataSerializers.FLOAT);
	
	private final float propellerRate = 3.141f;
	private float propellerRot, propellerRotOld;
	private final boolean alwaysLandingGear;
	
	public EntityHelicopter(EntityType<? extends EntityHelicopter> entity, Level level, ResourceLocation texture,
			RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item, boolean alwaysLandingGear) {
		super(entity, level, texture, engineSound, item);
		this.alwaysLandingGear = alwaysLandingGear;
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
		super.tickGround(q);
		Vec3 motion = getDeltaMovement();
		setDeltaMovement(motion);
	}
	
	@Override
	public void tickAir(Quaternion q) {
		super.tickAir(q);
		Vec3 motion = getDeltaMovement();
		if (isFreeLook()) {
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
		if (isOnGround() || isFreeLook()) {
			torqueX = torqueZ = 0;
			EulerAngles angles = UtilAngles.toDegrees(q);
			float dRoll = getMaxDeltaRoll();
			float dPitch = getMaxDeltaPitch();
			float roll, pitch;
			if (Math.abs(angles.roll) < dRoll) roll = (float) -angles.roll;
			else roll = -(float)Math.signum(angles.roll) * dRoll;
			if (Math.abs(angles.pitch) < dPitch) pitch = (float) -angles.pitch;
			else pitch = -(float)Math.signum(angles.pitch) * dPitch;
			q.mul(new Quaternion(Vector3f.XP, pitch, true));
			q.mul(new Quaternion(Vector3f.ZP, roll, true));
			if (!isOnGround()) torqueY += inputYaw * 0.5f;
		} else if (!isOnGround()) {
			torqueX += inputPitch * 0.5f;
			torqueY += inputYaw * 0.5f;
			torqueZ += inputRoll * 0.5f;
		}
		super.controlDirection(q);
	}

	@Override
	public Vec3 getThrustForce(Quaternion q) {
		Vec3 direction = UtilAngles.getYawAxis(q);
		Vec3 thrustForce = direction.scale(getThrustMag());
		return thrustForce;
	}
	
	@Override
	public double getThrustMag() {
		return super.getThrustMag() * 2.0 * UtilEntity.getAirPressure(getY());
	}
	
	@Override
	public double getFrictionMag() {
		double x = getDeltaMovement().x;
		double z = getDeltaMovement().z;
		double speed = Math.sqrt(x*x + z*z);
		double f = getTotalWeight() * 0.2;
		if (speed < f) return speed;
		return f;
	}
	
	public float getPropellerRotation(float partialTicks) {
		return Mth.lerp(partialTicks, propellerRotOld, propellerRot);
	}
	
	@Override
	public boolean isLandingGear() {
		if (alwaysLandingGear) return true;
    	return entityData.get(LANDING_GEAR);
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

}
