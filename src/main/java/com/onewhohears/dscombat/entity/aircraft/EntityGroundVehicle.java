package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.AircraftTextures;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntityGroundVehicle extends EntityAircraft {
	
	public final boolean isTank;
	
	protected final float wheelRate = 1.5f;
	private float wheelLRot, wheelLRotOld, wheelRRot, wheelRRotOld;
	
	public EntityGroundVehicle(EntityType<? extends EntityGroundVehicle> entity, Level level, AircraftTextures textures,
			RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item, boolean isTank) {
		super(entity, level, textures, engineSound, item);
		this.isTank = isTank;
		// TODO make a functional car/tank/air defense
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		
	}
	
	@Override
	public void controlDirection(Quaternion q) {
		float torque = inputYaw * getAccelerationYaw();
		if (isTank) addTorqueY(torque, true);
		else {
			// TODO how does turn rate change based on speed for a car?
		}
		super.controlDirection(q);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public void tickGround(Quaternion q) {
		super.tickGround(q);
		double speed = xzSpeed;
		float th = getCurrentThrottle();
		float max = getMaxSpeed() * th;
		if (inputSpecial) {
			speed -= 0.03;
			if (speed < 0) speed = 0;
		} else if (th > 0 && speed < max) {
			speed += getThrustMag();
			if (speed > max) speed = max;
		} else {
			speed -= 0.01;
			if (speed < 0) speed = 0;
		}
		Vec3 dir = UtilAngles.rotationToVector(getYRot(), 0);
		Vec3 motion = dir.scale(speed);
		setDeltaMovement(motion);
		// TODO ground vehicle gets over one block or less walls
	}
	
	@Override
	public double getThrustMag() {
		return super.getThrustMag();
	}
	
	@Override
	public Vec3 getThrustForce(Quaternion q) {
		return Vec3.ZERO;
	}
	
	@Override
	public Vec3 getFrictionForce() {
		return Vec3.ZERO;
	}
	
	@Override
	public double getFrictionMag() {
		return 0;
	}
	
	@Override
	public void clientTick() {
		super.clientTick();
		wheelLRotOld = wheelLRot;
		wheelRRotOld = wheelRRot;
		wheelLRot += xzSpeed * wheelRate;
		wheelRRot += xzSpeed * wheelRate;
	}
	
	public float getWheelLeftRotation(float partialTicks) {
		return Mth.lerp(partialTicks, wheelLRotOld, wheelLRot);
	}
	
	public float getWheelRightRotation(float partialTicks) {
		return Mth.lerp(partialTicks, wheelRRotOld, wheelRRot);
	}
	
	@Override
	public boolean isLandingGear() {
		return true;
    }
	
	@Override
    protected AABB makeBoundingBox() {
		return getDimensions(getPose()).makeBoundingBox(position());
    }

	@Override
	protected float getTorqueDragMag() {
		return 0.35f;
	}
	
	@Override
	public float getMaxDeltaYaw() {
    	return entityData.get(MAX_YAW);
    }
	
	@Override
	public void updateControls(float throttle, float pitch, float roll, float yaw,
			boolean mouseMode, boolean flare, boolean shoot, boolean select,
			boolean openMenu, boolean special) {
		super.updateControls(throttle, pitch, roll, yaw, mouseMode, flare, shoot, 
				select, openMenu, special);
		this.inputThrottle = pitch;
		this.inputPitch = throttle;
	}

}
