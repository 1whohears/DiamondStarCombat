package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.aircraft.VehicleStats;
import com.onewhohears.dscombat.data.aircraft.VehicleStats.HeliStats;
import com.onewhohears.dscombat.entity.damagesource.WeaponDamageSource.WeaponDamageType;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityHelicopter extends EntityVehicle {
	
	protected HeliStats heliStats;
	
	public EntityHelicopter(EntityType<? extends EntityHelicopter> entity, Level level, String defaultPreset) {
		super(entity, level, defaultPreset);
		heliStats = (HeliStats)vehicleStats;
	}
	
	@Override
	public AircraftType getAircraftType() {
		return AircraftType.HELICOPTER;
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
	public void tickGround(Quaternion q) {
		addFrictionForce(kineticFric);
	}
	
	@Override
	public double getDriveAcc() {
		return 0;
	}
	
	@Override
	public void tickAir(Quaternion q) {
		if (inputs.special && isOperational()) {
			float max_th = getMaxPushThrust();
			if (max_th != 0) setCurrentThrottle((float)-getWeightForce().y / max_th);
			setDeltaMovement(getDeltaMovement().multiply(1, 0.95, 1));
		}
		super.tickAir(q);
		Vec3 motion = getDeltaMovement();
		if (!isDriverCameraLocked() && isOperational()) {
			motion = motion.multiply(0.95, 1, 0.95);
			EulerAngles a = UtilAngles.toDegrees(q);
			// pitch forward backward
			Vec3 fDir = UtilAngles.rotationToVector(a.yaw, 0);
			motion = motion.add(fDir.scale(inputs.pitch).scale(getAccForward()));
			// roll left right
			Vec3 sDir = UtilAngles.rotationToVector(a.yaw+90, 0);
			motion = motion.add(sDir.scale(inputs.roll).scale(getAccSide()));
		}
		setDeltaMovement(motion);
		// IDEA 4 helicopter hover auto pilot mode with inputSpecial2
	}
	
	@Override
	public void controlDirection(Quaternion q) {
		super.controlDirection(q);
	}
	
	@Override
	public void directionGround(Quaternion q) {
		if (!isOperational()) return;
		flatten(q, 4f, 4f, true);
	}
	
	@Override
	public void directionAir(Quaternion q) {
		super.directionAir(q);
		if (!isOperational()) return;
		addMomentY(inputs.yaw * getYawTorque(), true);
		if (isDriverCameraLocked()) {
			addMomentX(inputs.pitch * getPitchTorque(), true);
			addMomentZ(inputs.roll * getRollTorque(), true);
		} else flatten(q, getMaxDeltaPitch(), getMaxDeltaRoll(), false);
	}

	@Override
	public Vec3 getThrustForce(Quaternion q) {
		Vec3 direction = UtilAngles.getYawAxis(q);
		Vec3 thrustForce = direction.scale(getPushThrustMag());
		return thrustForce;
	}
	
	@Override
	public float getMaxPushThrust() {
		return getMaxSpinThrust() * (float)airPressure * heliStats.heliLiftFactor;
	}
	
	@Override
	public boolean isLandingGear() {
		if (heliStats.alwaysLandingGear) return true;
    	return super.isLandingGear();
    }
	
	public float getAccForward() {
		return heliStats.accForward;
	}
	
	public float getAccSide() {
		return heliStats.accSide;
	}
	
	@Override
	public boolean isCustomBoundingBox() {
    	return true;
    }

	@Override
	public boolean canBrake() {
		return false;
	}

	@Override
	public boolean canToggleLandingGear() {
		return !heliStats.alwaysLandingGear;
	}
	
	@Override
	public boolean canHover() {
    	return true;
    }
	
	@Override
	public float calcProjDamageBySource(DamageSource source, float amount) {
		WeaponDamageType wdt = WeaponDamageType.byId(source.getMsgId());
		if (wdt != null && wdt.isContact()) return amount*DSCGameRules.getBulletDamageHeliFactor(level);
		return super.calcProjDamageBySource(source, amount);
	}

	@Override
	protected VehicleStats createVehicleStats() {
		return new HeliStats();
	}

}
