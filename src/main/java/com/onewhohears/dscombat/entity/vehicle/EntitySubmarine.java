package com.onewhohears.dscombat.entity.vehicle;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.vehicle.VehicleType;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntitySubmarine extends EntityBoat {
	
	public EntitySubmarine(EntityType<? extends EntitySubmarine> entity, Level level, String defaultPreset) {
		super(entity, level, defaultPreset);
	}
	
	@Override
	public VehicleType getVehicleType() {
		return VehicleType.SUBMARINE;
	}
	
	@Override
	public void directionWater(Quaternion q) {
		if (!isOperational()) return;
		if (!isDriverCameraLocked()) flatten(q, getMaxDeltaPitch(), getMaxDeltaRoll(), false);
		else {
			addMomentX(inputs.pitch * getPitchTorque(), true);
			addMomentZ(inputs.roll * getRollTorque(), true);
		}
		addMomentY(inputs.yaw * getYawTorque(), true);
	}
	
	@Override
	public double getDriveAcc() {
		return 0;
	}
	
	@Override
	public void tickWater(Quaternion q) {
		super.tickWater(q);
		Vec3 move = getDeltaMovement();
		if (!isDriverCameraLocked() && isOperational()) {
			if (inputs.pitch == 0) move = move.multiply(1, 0.9, 1);
			else move = move.add(0, inputs.pitch * 0.02, 0);
			double max = 0.2;
			if (Math.abs(move.y) > max) move = new Vec3(move.x, max*Math.signum(move.y), move.z);
		}
		setDeltaMovement(move);
	}
	
	@Override
	protected void tickFloat() {
		if (inputFloat()) super.tickFloat();
		else forces = forces.add(getWeightForce().scale(-1));
	}
	
	@Override
	public double getFloatSpeed() {
		if (!isOperational()) return super.getFloatSpeed();
		if (inputFloat()) return 0.04;
		return 0;
	}
	
	@Override
	public boolean couldFloat() {
		if (!isOperational()) return false;
		return inputFloat();
	}
	
	@Override
	public float getMaxPushThrust() {
		return getMaxSpinThrust();
	}
	
	public boolean inputFloat() {
		return inputs.special2;
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
	public float getStepHeight() {
		return 0.2f;
	}
	
	@Override
	public void waterDamage() {
	}
	
	@Override
	public boolean canBrake() {
		return true;
	}

}
