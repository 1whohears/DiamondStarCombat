package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.aircraft.DSCPhysicsConstants;
import com.onewhohears.dscombat.data.aircraft.VehicleStats;
import com.onewhohears.dscombat.data.aircraft.VehicleStats.BoatStats;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityBoat extends EntityVehicle {
	
	protected double waterLevel;
	
	public EntityBoat(EntityType<? extends EntityBoat> entity, Level level, String defaultPreset) {
		super(entity, level, defaultPreset);
	}
	
	@Override
	public AircraftType getAircraftType() {
		return AircraftType.BOAT;
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
		flatten(q, 4f, 4f, true);
		if (!isOperational()) return;
		addMomentY(inputs.yaw * getYawTorque() * 0.1f, true);
	}
	
	@Override
	public void directionAir(Quaternion q) {
		super.directionAir(q);
	}
	
	@Override
	public void directionWater(Quaternion q) {
		if (!isOperational()) return;
		flatten(q, 2f, 2f, true);
		addMomentY(inputs.yaw * getYawTorque(), true);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public void tickMovement(Quaternion q) {
		if (inputs.special) throttleToZero();
		super.tickMovement(q);
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
		super.tickAir(q);
	}
	
	@Override
	public void tickWater(Quaternion q) {
		super.tickWater(q);
		if (!checkInWater()) return;
		if (canBrake() && isBraking()) addFrictionForce(2000);
		Vec3 weightF = getWeightForce();
		float F = getBbWidth()*getBbWidth()*DSCPhysicsConstants.FLOAT;
		float maxF = F*getBbHeight();
		if (maxF < Math.abs(weightF.y)) {
			forces = forces.add(0, maxF, 0);
			return;
		} else if (!couldFloat()) {
			forces = forces.add(weightF.scale(-0.9));
			return;
		}
		double bbMin = getBoundingBox().minY;
		double actualF = F * (waterLevel - bbMin);
		Vec3 floatF = new Vec3(0, actualF, 0);
		forces = forces.add(floatF);
		Vec3 move = getDeltaMovement();
		double stableDisplacement = Math.abs(weightF.y) / F;
		double stableY = waterLevel - stableDisplacement;
		double stableDiff = stableY - bbMin;
		if (Math.abs(move.y) < 0.1 && Math.abs(stableDiff) < 0.1) {
			double posBBDiff = getY() - bbMin;
			setPos(position().x, stableY+posBBDiff, position().z);
			move = move.multiply(1, 0, 1);
			forces = forces.multiply(1, 0, 1);
		} else if (stableDiff > 0 && move.y == 0) {
			/*
			 * HOW 8 for some reason Entity#move(...) cancels the vertical movement sometimes
			 * so the boat never reaches a balanced state. if the pilot turns the boat 
			 * in this state vertical movement will stop getting canceled and bounce like normal.
			 * I have no idea why this happens but this is a temporary solution.
			 * the vertical movement gets canceled by Block#updateEntityAfterFallOn(...) which is called within Entity#move(...). 
			 */
			setPos(position().add(0, forces.y / getTotalMass(), 0));
		} else {
			move = move.multiply(1, 0.9, 1);
		}
		setDeltaMovement(move);
	}
	
	public double getFloatSpeed() {
		return 0.02;
	}
	
	@Override
	public boolean isBraking() {
		return inputs.special;
	}
	
	public boolean couldFloat() {
		return isOperational();
	}
	
	@Override
	public void tickGroundWater(Quaternion q) {
		tickWater(q);
		Vec3 motion = getDeltaMovement();
		if (motion.y < 0) motion = motion.multiply(1, 0, 1);
		motion = motion.multiply(0.5, 1, 0.5);
		setDeltaMovement(motion);
	}
	
	protected boolean checkInWater() {
		AABB aabb = getBoundingBox();
		int i = Mth.floor(aabb.minX);
		int j = Mth.ceil(aabb.maxX);
		int k = Mth.floor(aabb.minY);
		int l = Mth.ceil(aabb.maxY);
		int i1 = Mth.floor(aabb.minZ);
		int j1 = Mth.ceil(aabb.maxZ);
		boolean flag = false;
		this.waterLevel = -Double.MAX_VALUE;
		BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();
		for(int k1=i;k1<j;++k1){for(int l1=k;l1<l;++l1){for(int i2=i1;i2<j1;++i2){
			mbp.set(k1, l1, i2);
			FluidState fluidstate = level.getFluidState(mbp);
			if (fluidstate.is(FluidTags.WATER)) {
				float f = (float)l1 + fluidstate.getHeight(level, mbp);
				waterLevel = Math.max((double)f, waterLevel);
				flag = aabb.minY < (double)f;
			}
		}}}
		return flag;
	}
	
	@Override
	public double getMaxSpeedForMotion() {
		float th = getCurrentThrottle();
		double max = getMaxSpeed();
		if (th < 0) return max * 0.25;
    	return max;
    }
	
	@Override
	public double getPushThrustMag() {
		return super.getPushThrustMag();
	}

	@Override
	public Vec3 getThrustForce(Quaternion q) {
		if (!isInWater()) return Vec3.ZERO;
		Vec3 direction = UtilAngles.getRollAxis(q);
		Vec3 thrustForce = direction.scale(getPushThrustMag());
		return thrustForce;
	}
	
	@Override
	public boolean isLandingGear() {
		return false;
    }
	
	@Override
	public float getStepHeight() {
		return 0.2f;
	}
	
	@Override
	public void waterDamage() {
		if (waterLevel > getBoundingBox().maxY) super.waterDamage();
	}
	
	@Override
	public boolean canOpenMenu() {
		return xzSpeed < 0.1 || isTestMode();
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
		return new BoatStats();
	}

}
