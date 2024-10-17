package com.onewhohears.dscombat.entity.vehicle;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.vehicle.VehicleType;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class EntityGroundVehicle extends EntityVehicle {
	
	public EntityGroundVehicle(EntityType<? extends EntityGroundVehicle> entity, Level level, String defaultPreset) {
		super(entity, level, defaultPreset);
	}
	
	@Override
	public VehicleType getVehicleType() {
		return VehicleType.CAR;
	}
	
	@Override
	public void directionGround(Quaternionf q) {
		if (getStats().asCar().isTank && isOperational()) {
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
	public Vec3 getThrustForce(Quaternionf q) {
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
	public double getMaxSpeedFactor() {
		return super.getMaxSpeedFactor() * Config.COMMON.carSpeedFactor.get();
	}

}
