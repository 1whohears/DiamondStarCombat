package com.onewhohears.dscombat.entity.vehicle;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.vehicle.VehicleType;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityGroundVehicle extends EntityVehicle {
	
	public EntityGroundVehicle(EntityType<? extends EntityGroundVehicle> entity, Level level, String defaultPreset) {
		super(entity, level, defaultPreset);
	}
	
	@Override
	public VehicleType getVehicleType() {
		return VehicleType.CAR;
	}
	
	@Override
	public boolean isGroundBraking() {
		return inputs.special;
	}
	
	@Override
	protected void applyGroundBreaks() {
		throttleToZero();
		super.applyGroundBreaks();
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
	public boolean canToggleLandingGear() {
		return false;
	}

	@Override
	public double getMaxSpeedFactor() {
		return super.getMaxSpeedFactor() * Config.SERVER.carSpeedFactor.get();
	}

	@Override
	public boolean isPitchControllable() {
		return false;
	}

	@Override
	public boolean isRollControllable() {
		return false;
	}

	@Override
	public boolean canTurnViaTorque() {
		return isOperational() && isOnGround() && getStats().asCar().isTank;
	}

	@Override
	public boolean canDriveOnGround() {
		return true;
	}

}
