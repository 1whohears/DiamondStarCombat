package com.onewhohears.dscombat.entity.vehicle;

import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.vehicle.VehicleType;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityHelicopter extends EntityVehicle {

	private int altitudeWarningTicks;

	public EntityHelicopter(EntityType<? extends EntityHelicopter> entity, Level level, String defaultPreset) {
		super(entity, level, defaultPreset);
	}
	
	@Override
	public VehicleType getVehicleType() {
		return VehicleType.HELICOPTER;
	}

	@Override
	public double getDriveAcc() {
		return 0;
	}
	
	@Override
	public void calcAirMovement(QuaternionF q) {
		super.calcAirMovement(q);
		if (inputs.special && isOperational()) {
			flatten(q, getMaxDeltaPitch(), getMaxDeltaRoll(), false);
			float max_th = (float)UtilAngles.getYawAxis(q).y * getMaxPushThrust();
			float yForceNoLift = (float)-(getWeightForce().y + addForceBetweenTicks.y);
			if (max_th != 0) inputs.setThrottleOverride(yForceNoLift / max_th, this);
			setDeltaMovement(getDeltaMovement().multiply(0.95, 0.95, 0.95));
		}
	}

	@Override
	public Vec3 getThrustForce(QuaternionF q) {
		Vec3 direction = UtilAngles.getYawAxis(q);
        return direction.scale(getPushThrustMag());
	}
	
	@Override
	public float getMaxPushThrust() {
		return getMaxSpinThrust() * (float) getFluidDensity() * getStats().asHeli().heliLiftFactor;
	}
	
	@Override
	public boolean isLandingGear() {
		if (getStats().asHeli().alwaysLandingGear) return true;
    	return super.isLandingGear();
    }
	
	public float getAccForward() {
		return getStats().asHeli().accForward;
	}
	
	public float getAccSide() {
		return getStats().asHeli().accSide;
	}
	
	@Override
	public boolean isCustomBoundingBox() {
    	return true;
    }

	@Override
	public boolean canToggleLandingGear() {
		return !getStats().asHeli().alwaysLandingGear;
	}
	
	@Override
	public boolean canHover() {
    	return true;
    }
	
	@Override
	public boolean cutThrottleOnNoPilot() {
		return false;
	}
	
	@Override
	protected float calcDamageFromBullet(DamageSource source, float amount) {
		return amount * DSCGameRules.getBulletDamageHeliFactor(level);
	}

	@Override
	public double getMaxSpeedFactor() {
		return super.getMaxSpeedFactor() * Config.SERVER.heliSpeedFactor.get();
	}

	@Override
	public int getAltitudeWarningTicks() {
		return altitudeWarningTicks;
	}

	@Override
	public void calcMoveStatsPre(QuaternionF q) {
		super.calcMoveStatsPre(q);
		if (getDeltaMovement().y < 0 && getAltitude() < 40) ++altitudeWarningTicks;
		else altitudeWarningTicks = 0;
	}

	@Override
	public boolean canDriveOnGround() {
		return false;
	}

	@Override
	public double getMaxClimbSpeed() {
		return DSCPhyCons.MAX_HELICOPTER_CLIMB_SPEED;
	}
}
