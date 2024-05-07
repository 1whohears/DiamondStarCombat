package com.onewhohears.dscombat.entity.vehicle;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.aircraft.VehicleStats;
import com.onewhohears.dscombat.data.aircraft.VehicleStats.CarStats;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityGroundVehicle extends EntityVehicle {
	
	protected CarStats carStats;
	
	public EntityGroundVehicle(EntityType<? extends EntityGroundVehicle> entity, Level level, String defaultPreset) {
		super(entity, level, defaultPreset);
		carStats = (CarStats)vehicleStats;
	}
	
	@Override
	public AircraftType getAircraftType() {
		return AircraftType.CAR;
	}
	
	@Override
	public void directionGround(Quaternion q) {
		if (carStats.isTank && isOperational()) {
			flatten(q, 4f, 4f, true);
			addMomentY(inputs.yaw * getYawTorque(), true);
		} else super.directionGround(q);
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
	public Vec3 getThrustForce(Quaternion q) {
		return Vec3.ZERO;
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
