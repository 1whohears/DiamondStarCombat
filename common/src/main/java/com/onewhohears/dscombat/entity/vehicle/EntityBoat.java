package com.onewhohears.dscombat.entity.vehicle;

import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.dscombat.data.vehicle.VehicleType;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import com.onewhohears.onewholibs.util.math.UtilGeometry;
import net.minecraft.core.BlockPos;
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
        maxUpStep = 0.2f;
	}
	
	@Override
	public VehicleType getVehicleType() {
		return VehicleType.BOAT;
	}

	@Override
	public void applyGroundBreaks() {

	}

	@Override
	public void applyAirBreaks() {
		throttleToZero();
		super.applyAirBreaks();
	}

	@Override
	public boolean canGroundBrake() {
		return isInWater() && getStats().break_deacc_air > 0 && isOperational();
	}

	@Override
	public boolean canAirBrake() {
		return false;
	}

	public boolean canWaterBrake() {
		return isInWater() && getStats().break_deacc_air > 0 && isOperational();
	}

	@Override
	public double getDriveAcc() {
		return 0;
	}
	
	@Override
	public void calcWaterMovement(QuaternionF q) {
		super.calcWaterMovement(q);
		if (!checkInWater()) return;
		flatten(q, 2f, 2f, true);
		tickFloat();
		if (canWaterBrake() && isAirBreaking()) applyAirBreaks();
	}
	
	protected void tickFloat() {
		Vec3 weightF = getWeightForce();
		float F = getBbWidth()*getBbWidth()*DSCPhyCons.FLOAT;
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
	public boolean isGroundBraking() {
		return inputs.special;
	}
	
	public boolean couldFloat() {
		return isOperational();
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
			FluidState fluidstate = getWorld().getFluidState(mbp);
			if (fluidstate.is(FluidTags.WATER)) {
				float f = (float)l1 + fluidstate.getHeight(getWorld(), mbp);
				waterLevel = Math.max((double)f, waterLevel);
				flag = aabb.minY < (double)f;
			}
		}}}
		return flag;
	}
	
	@Override
	public double getMaxSpeedForMotion() {
		float th = getCurrentThrottle();
		double max = super.getMaxSpeedForMotion();
		if (th < 0) return max * 0.25;
    	return max;
    }

	@Override
	public double getMaxSpeedFactor() {
		return super.getMaxSpeedFactor() * Config.SERVER.boatSpeedFactor.get();
	}
	
	@Override
	public double getPushThrustMag() {
		return super.getPushThrustMag();
	}

	@Override
	public Vec3 getThrustForce(QuaternionF q) {
		if (!isInWater()) return Vec3.ZERO;
		Vec3 direction = UtilAngles.getRollAxis(q);
        return direction.scale(getPushThrustMag());
	}

	@Override
	public double getDragArea() {
		double area = super.getDragArea();
		if (isInWater()) {
			double angle = UtilGeometry.angleBetweenDegrees(getDeltaMovement(), getLookAngle());
			area = Math.max(area * 0.025, area * Math.sin(Mth.DEG_TO_RAD*angle));
		}
		return area;
	}
	
	@Override
	public boolean isLandingGear() {
		return false;
    }
	
	@Override
	public void waterDamage() {
		if (waterLevel > getBoundingBox().maxY) super.waterDamage();
	}
	
	@Override
	public boolean canOpenPartsMenu() {
		return xzSpeed < 0.1 || isTestMode();
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
	public boolean isPitchControllable() {
		return false;
	}

	@Override
	public boolean isRollControllable() {
		return false;
	}

	@Override
	public boolean canDriveOnGround() {
		return false;
	}

	@Override
	public boolean ignoreToItemFlyCheck() {
		return true;
	}

}
