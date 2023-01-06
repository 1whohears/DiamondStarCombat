package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntityGroundVehicle extends EntityAircraft {
	
	protected final float wheelRate = 1.5f;
	private float wheelLRot, wheelLRotOld, wheelRRot, wheelRRotOld;
	
	public EntityGroundVehicle(EntityType<? extends EntityGroundVehicle> entity, Level level, ResourceLocation texture,
			RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item) {
		super(entity, level, texture, engineSound, item);
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
		addTorqueY(torque, true);
		super.controlDirection(q);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public void tickGround(Quaternion q) {
		super.tickGround(q);
		Vec3 motion = getDeltaMovement();
		
		setDeltaMovement(motion);
	}
	
	@Override
	public Vec3 getThrustForce(Quaternion q) {
		return Vec3.ZERO;
	}
	
	@Override
	public double getThrustMag() {
		return super.getThrustMag();
	}
	
	@Override
	public double getFrictionMag() {
		double m = super.getFrictionMag();
		return m;
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
		// TODO for cars, limit based on speed
    	return entityData.get(MAX_YAW);
    }

}
