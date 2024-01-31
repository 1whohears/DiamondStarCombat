package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData;
import com.onewhohears.dscombat.data.aircraft.VehicleStats;
import com.onewhohears.dscombat.data.aircraft.VehicleStats.CarStats;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityGroundVehicle extends EntityVehicle {
	
	private float wheelLRot, wheelLRotOld, wheelRRot, wheelRRotOld;
	
	public EntityGroundVehicle(EntityType<? extends EntityGroundVehicle> entity, Level level, ImmutableVehicleData vehicleData) {
		super(entity, level, vehicleData);
	}
	
	@Override
	public AircraftType getAircraftType() {
		return AircraftType.CAR;
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
		if (vehicleData.isTank && isOperational()) {
			flatten(q, 4f, 4f, true);
			addMomentY(inputs.yaw * getYawTorque(), true);
		} else super.directionGround(q);
	}
	
	@Override
	public void directionAir(Quaternion q) {
		super.directionAir(q);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public void tickGround(Quaternion q) {
		super.tickGround(q);
	}
	
	@Override
	public boolean isBraking() {
		return inputs.special;
	}
	
	@Override
	public void applyBreaks() {
		throttleToZero();
		super.applyBreaks();
	}
	
	@Override
	public double getPushThrustMag() {
		return super.getPushThrustMag();
	}
	
	@Override
	public Vec3 getThrustForce(Quaternion q) {
		return Vec3.ZERO;
	}
	
	@Override
	public void clientTick() {
		super.clientTick();
		wheelLRotOld = wheelLRot;
		wheelRRotOld = wheelRRot;
		wheelLRot += xzSpeed * vehicleData.spinRate * xzSpeedDir;
		wheelRRot += xzSpeed * vehicleData.spinRate * xzSpeedDir;
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
	public float getStepHeight() {
		return 1.0f;
	}
	
	@Override
	public String getOpenMenuError() {
		return "error.dscombat.no_menu_moving";
	}

	@Override
	public boolean canBrake() {
		return true;
	}

	@Override
	public boolean canToggleLandingGear() {
		return false;
	}

	@Override
	protected VehicleStats createVehicleStats() {
		return new CarStats();
	}

}
