package com.onewhohears.dscombat.entity.vehicle;

import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.vehicle.VehicleType;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.dscombat.util.UtilVehicleEntity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityHelicopter extends EntityVehicle {

	private int altitudeWarningTicks;
	// Simulated rotor power (spools towards throttle)
	private double rotorPower = 0.0;

	public double getRotorPower() { return rotorPower; }

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
			// Account for lift multipliers (IGE, ETL, VRS) so hover throttle is stable
			double liftMul = computeLiftMultiplier(q);
			float effMaxTh = (float)(max_th * liftMul);
			if (effMaxTh != 0) inputs.setThrottleOverride(yForceNoLift / effMaxTh, this);
			// Configurable hover damping applies to all axes
			double hoverD = Config.SERVER.heliHoverDamping.get();
			setDeltaMovement(getDeltaMovement().scale(hoverD));
		} else {
			// Mild lateral damping to reduce floatiness while airborne
			// Keeps vertical speed unchanged to respect climb/fall dynamics
			Vec3 dm = getDeltaMovement();
			double lateral = Config.SERVER.heliLateralDampingXZ.get();
			// Extra IGE damping near ground
			double igeExtra = Config.SERVER.igeExtraLateralDamping.get();
			if (igeExtra > 0) {
				double hMax = Config.SERVER.igeDampingMaxHeight.get();
				if (hMax > 0) {
					int limit = (int)Math.ceil(hMax) + 1;
					int aglBlocks = UtilVehicleEntity.getDistFromGround(this, limit, true);
					double h = Math.min(aglBlocks, hMax);
					double f = Math.max(0.0, 1.0 - (h / hMax));
					double extraMul = 1.0 - igeExtra * f; // reduce XZ more near ground
					lateral *= extraMul;
				}
			}
			setDeltaMovement(dm.multiply(lateral, 1.0, lateral));
		}
	}

	@Override
	public Vec3 getThrustForce(QuaternionF q) {
		Vec3 direction = UtilAngles.getYawAxis(q);
		// Apply realistic heli physics multipliers to thrust
		double liftMul = computeLiftMultiplier(q);
		return direction.scale(getPushThrustMag() * liftMul);
	}
	
	@Override
	public float getMaxPushThrust() {
		return getMaxSpinThrust() * (float) getFluidDensity() * getStats().asHeli().heliLiftFactor;
	}

	@Override
	public double getPushThrustMag() {
		if (getCurrentFuel() <= 0 || !isOperational()) return 0;
		return rotorPower * getMaxPushThrust();
	}

	/**
	 * Computes lift multipliers to simulate heli-specific aerodynamics.
	 * Includes:
	 *  - Ground effect (IGE): extra lift near the ground.
	 *  - Effective translational lift (ETL): extra lift with forward airspeed.
	 *  - Vortex Ring State (VRS): reduced lift during high-rate vertical descents at low forward speed.
	 */
	protected double computeLiftMultiplier(Quaternion q) {
		double mult = 1.0;

		// Ground Effect (IGE)
		if (Config.SERVER.enableGroundEffect.get()) {
			double maxH = Config.SERVER.groundEffectMaxHeight.get();
			if (maxH > 0) {
				int limit = (int)Math.ceil(maxH) + 1;
				int aglBlocks = UtilVehicleEntity.getDistFromGround(this, limit, true);
				double h = Math.min(aglBlocks, maxH);
				double factor = Math.max(0.0, 1.0 - (h / maxH));
				double ge = Config.SERVER.groundEffectStrength.get() * factor;
				mult += ge;
			}
		}

		// Effective Translational Lift (ETL)
		if (Config.SERVER.enableTranslationalLift.get()) {
			Vec3 dm = getDeltaMovement();
			double speedXZ = Math.sqrt(dm.x * dm.x + dm.z * dm.z);
			double full = Math.max(1e-6, Config.SERVER.translationalLiftFullSpeed.get());
			double factor = Math.min(1.0, speedXZ / full);
			double tl = Config.SERVER.translationalLiftMaxBonus.get() * factor;
			mult += tl;
		}

		// Vortex Ring State (VRS)
		if (Config.SERVER.enableVRS.get()) {
			Vec3 dm = getDeltaMovement();
			double vy = dm.y;
			double descent = vy < 0 ? -vy : 0.0;
			double speedXZ = Math.sqrt(dm.x * dm.x + dm.z * dm.z);
			double trigger = Config.SERVER.vrsDescentTrigger.get();
			double horizMax = Config.SERVER.vrsHorizMaxSpeed.get();
			if (descent > trigger && speedXZ < horizMax) {
				// Scale severity as descent increases above trigger up to ~2x trigger
				double severity = Math.min(1.0, (descent - trigger) / Math.max(1e-6, trigger));
				double penalty = Config.SERVER.vrsMaxPenalty.get() * severity;
				mult *= Math.max(0.0, 1.0 - penalty);
			}
		}

		// Clamp to sensible range
		if (Double.isNaN(mult) || Double.isInfinite(mult)) return 1.0;
		return Math.max(0.0, mult);
	}
	
	@Override
	public void addControllingTorques(Quaternion q) {
        boolean scaleTorque = Config.SERVER.scaleTorqueWithRotorPower.get();
        if (scaleTorque) {
            // Apply torques ourselves, scaled by rotor power authority
            if (canTurnViaTorque()) {
                double minAuth = Config.SERVER.minControlAuthorityAtIdle.get();
                double authority = Math.max(0.0, Math.min(1.0, minAuth + (1.0 - minAuth) * rotorPower));
                float torqueScale = (float) authority;
                if (canControlPitch()) {
                    if (isHardCodedRotAcc()) hardCodedAccPitch();
                    else addMomentX(inputs.pitch * getPitchTorque() * torqueScale, true);
                }
                if (canControlYaw()) {
                    if (isHardCodedRotAcc()) hardCodedAccYaw();
                    else addMomentY(inputs.yaw * getYawTorque() * torqueScale, true);
                }
                if (canControlRoll()) {
                    if (isHardCodedRotAcc()) hardCodedAccRoll();
                    else {
                        if (inputs.bothRoll) flatten(q, 0, getMaxDeltaRoll(), false);
                        else addMomentZ(inputs.roll * getRollTorque() * torqueScale, true);
                    }
                }
            }
        } else {
            // Base input-driven torques
            super.addControllingTorques(q);
            if (!isOperational()) return;
            // Post-clamp authority scaling (only if pre-clamp is disabled)
            if (!Config.SERVER.scaleAuthorityPreClamp.get()) {
                double minAuth = Config.SERVER.minControlAuthorityAtIdle.get();
                double authority = Math.max(0.0, Math.min(1.0, minAuth + (1.0 - minAuth) * rotorPower));
                if (authority < 1.0) {
                    Vec3 cm = getControlMoment();
                    if (cm.x != 0 || cm.y != 0 || cm.z != 0) setControlMoment(cm.scale(authority));
                }
            }
        }

        // Anti-torque (main rotor torque -> yaw moment); tail rotor compensation implied
        if (Config.SERVER.enableAntiTorque.get()) {
            double coeff = Config.SERVER.antiTorqueCoeff.get();
            double dir = Config.SERVER.antiTorqueDirection.get();
            double thrust = getPushThrustMag() * computeLiftMultiplier(q);
            double yawMoment = dir * coeff * thrust;
            addMoment(new Vec3(0, yawMoment, 0), false, true);
            // Tail rotor roll coupling (rolling due to tail rotor thrust side-force)
            if (Config.SERVER.enableTailRotorRollCoupling.get()) {
                double rollCoeff = Config.SERVER.tailRotorRollCoeff.get();
                if (rollCoeff != 0) {
                    addMoment(new Vec3(0, 0, -yawMoment * rollCoeff), false, true);
                }
            }
        }

        // Yaw damper (stabilize heading by damping yaw rate)
        double ydGain = Config.SERVER.yawDamperGain.get();
        if (ydGain > 0) {
            Vec3 I = getTotalRotInertia();
            Vec3 av = getAngularVel();
            addMoment(new Vec3(0, -av.y * ydGain * I.y, 0), false, true);
        }

        // ETL pitch-up coupling: nose-up moment with forward speed
        if (Config.SERVER.enableETLPitchUp.get()) {
            Vec3 dm = getDeltaMovement();
            double speedXZ = Math.sqrt(dm.x * dm.x + dm.z * dm.z);
            double full = Math.max(1e-6, Config.SERVER.translationalLiftFullSpeed.get());
            double factor = Math.min(1.0, speedXZ / full);
            double gain = Config.SERVER.etlPitchGain.get();
            if (gain != 0 && factor > 0) {
                // Nose-up is negative X in our convention (match flattenPitch behavior sign)
                addMoment(new Vec3(-gain * factor * getTotalRotInertia().x, 0, 0), false, true);
            }
        }

        // ETL trim assist: small pitch-down with forward speed to reduce manual trim load
        if (Config.SERVER.enableETLTrimAssist.get()) {
            Vec3 dm = getDeltaMovement();
            double speedXZ = Math.sqrt(dm.x * dm.x + dm.z * dm.z);
            double full = Math.max(1e-6, Config.SERVER.translationalLiftFullSpeed.get());
            double factor = Math.min(1.0, speedXZ / full);
            double gain = Config.SERVER.etlTrimGain.get();
            if (gain != 0 && factor > 0) {
                addMoment(new Vec3(gain * factor * getTotalRotInertia().x, 0, 0), false, true);
            }
        }
    }

	@Override
	public float getControlMaxDeltaPitch() {
		float base = super.getControlMaxDeltaPitch();
		if (!Config.SERVER.scaleAuthorityPreClamp.get()) return base;
		double minAuth = Config.SERVER.minControlAuthorityAtIdle.get();
		double authority = Math.max(0.0, Math.min(1.0, minAuth + (1.0 - minAuth) * rotorPower));
		return (float)(base * authority);
	}

	@Override
	public float getControlMaxDeltaYaw() {
		float base = super.getControlMaxDeltaYaw();
		if (!Config.SERVER.scaleAuthorityPreClamp.get()) return base;
		double minAuth = Config.SERVER.minControlAuthorityAtIdle.get();
		double authority = Math.max(0.0, Math.min(1.0, minAuth + (1.0 - minAuth) * rotorPower));
		return (float)(base * authority);
	}

	@Override
	public float getControlMaxDeltaRoll() {
		float base = super.getControlMaxDeltaRoll();
		if (!Config.SERVER.scaleAuthorityPreClamp.get()) return base;
		double minAuth = Config.SERVER.minControlAuthorityAtIdle.get();
		double authority = Math.max(0.0, Math.min(1.0, minAuth + (1.0 - minAuth) * rotorPower));
		return (float)(base * authority);
	}
	

	@Deprecated // currently unused; reserved for potential hover taxi tuning
	public float getAccForward() {
		return getStats().asHeli().accForward;
	}
	
	@Deprecated // currently unused; reserved for potential hover taxi tuning
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
		return amount * DSCGameRules.getBulletDamageHeliFactor(getWorld());
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
		// Rotor spool dynamics: rotorPower approaches commanded throttle with separate up/down rates
		double goal = getCurrentThrottle();
		double up = Config.SERVER.rotorSpoolUpRate.get();
		double down = Config.SERVER.rotorSpoolDownRate.get();
		double step = goal >= rotorPower ? up : down;
		if (step < 0) step = 0;
		if (step > 1) step = 1;
		if (rotorPower < goal) rotorPower = Math.min(goal, rotorPower + step);
		else if (rotorPower > goal) rotorPower = Math.max(goal, rotorPower - step);
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
