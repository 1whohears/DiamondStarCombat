package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.AircraftTextures;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntitySubmarine extends EntityBoat {
	
	public EntitySubmarine(EntityType<? extends EntitySubmarine> entity, Level level, 
			AircraftTextures textures, RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item) {
		super(entity, level, textures, engineSound, item);
	}
	
	@Override
	public AircraftType getAircraftType() {
		return AircraftType.SUBMARINE;
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
		flatten(q, 5f, 5f);
		addTorqueY(inputYaw * getAccelerationYaw(), true);
		super.controlDirection(q);
	}
	
	@Override
	public void directionGround(Quaternion q) {
	}
	
	@Override
	public void directionAir(Quaternion q) {
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public void tickMovement(Quaternion q) {
		super.tickMovement(q);
	}
	
	@Override
	public void tickGround(Quaternion q) {
		super.tickGround(q);
	}
	
	@Override
	public void tickAir(Quaternion q) {
		super.tickAir(q);
	}
	
	@Override
	public void tickWater(Quaternion q) {
		Vec3 move = getDeltaMovement();
		move = move.multiply(0.990, 0.900, 0.990);
		float f = (float)getThrustMag();
		move = move.add(0, inputPitch * 0.04, 0);
		double max = 0.2;
		if (Math.abs(move.y) > 0.5) move.multiply(1, max/move.y, 1);
		move = move.add(
				(double)(Mth.sin(-getYRot()*Mth.DEG_TO_RAD)*f), 
				0.0D, 
				(double)(Mth.cos(getYRot()*Mth.DEG_TO_RAD)*f));
		setDeltaMovement(move);
	}
	
	@Override
	public void tickGroundWater(Quaternion q) {
		super.tickGroundWater(q);
	}
	
	@Override
	public double getThrustMag() {
		return super.getThrustMag();
	}

	@Override
	protected float getTorqueDragMag() {
		return 0.35f;
	}

	@Override
	public Vec3 getThrustForce(Quaternion q) {
		return Vec3.ZERO;
	}
	
	@Override
	public boolean isLandingGear() {
		return false;
    }
	
	@Override
    protected AABB makeBoundingBox() {
		return getDimensions(getPose()).makeBoundingBox(position());
    }
	
	@Override
	public float getMaxSpeed() {
		if (getCurrentThrottle() < 0) return super.getMaxSpeed() * 0.10f;
    	return entityData.get(MAX_SPEED);
    }
	
	@Override
	public void updateControls(float throttle, float pitch, float roll, float yaw,
			boolean mouseMode, boolean flare, boolean shoot, boolean select,
			boolean openMenu, boolean special) {
		super.updateControls(throttle, pitch, roll, yaw, mouseMode, flare, shoot, 
				select, openMenu, special);
	}
	
	@Override
	public float getStepHeight() {
		return 0.2f;
	}
	
	@Override
	public void waterDamage() {
	}

}
