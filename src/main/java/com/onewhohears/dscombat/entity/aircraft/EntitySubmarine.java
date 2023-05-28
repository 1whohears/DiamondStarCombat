package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntitySubmarine extends EntityBoat {
	
	public EntitySubmarine(EntityType<? extends EntitySubmarine> entity, Level level, 
			AircraftPreset defaultPreset,
			RegistryObject<SoundEvent> engineSound, float explodeSize) {
		super(entity, level, defaultPreset, engineSound, explodeSize);
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
		super.controlDirection(q);
	}
	
	@Override
	public void directionGround(Quaternion q) {
		super.directionGround(q);
	}
	
	@Override
	public void directionAir(Quaternion q) {
		super.directionAir(q);
	}
	
	@Override
	public void directionWater(Quaternion q) {
		if (!isOperational()) return;
		if (isFreeLook()) flatten(q, getMaxDeltaPitch(), getMaxDeltaRoll(), false);
		else {
			addMomentX(inputs.pitch * getPitchTorque(), true);
			addMomentZ(inputs.roll * getRollTorque(), true);
		}
		addMomentY(inputs.yaw * getYawTorque(), true);
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
	public double getDriveAcc() {
		return 0;
	}
	
	@Override
	public void tickAir(Quaternion q) {
		super.tickAir(q);
	}
	
	@Override
	public void tickWater(Quaternion q) {
		super.tickWater(q);
		Vec3 move = getDeltaMovement();
		if (isFreeLook() && isOperational()) {
			move = move.add(0, inputs.pitch * 0.04, 0);
			double max = 0.2;
			if (Math.abs(move.y) > max) move.multiply(1, max/move.y, 1);
		}
		setDeltaMovement(move);
		// TODO 3 inputSpecial2 moves submarine to surface
	}
	
	@Override
	public double getFloatSpeed() {
		if (!isOperational()) return super.getFloatSpeed();
		if (inputs.special2) return 0.04;
		return 0;
	}
	
	@Override
	public boolean willFloat() {
		if (!isOperational()) return false;
		return inputs.special2;
	}
	
	@Override
	public void tickGroundWater(Quaternion q) {
		super.tickGroundWater(q);
	}
	
	@Override
	public float getMaxPushThrust() {
		return getMaxSpinThrust();
	}
	
	@Override
	public boolean isLandingGear() {
		return false;
    }
	
	@Override
	public boolean isCustomBoundingBox() {
    	return true;
    }
	
	@Override
	public void readInputs() {
		super.readInputs();
	}
	
	@Override
	public float getStepHeight() {
		return 0.2f;
	}
	
	@Override
	public void waterDamage() {
	}
	
	@Override
	public boolean canBreak() {
		return true;
	}

}
