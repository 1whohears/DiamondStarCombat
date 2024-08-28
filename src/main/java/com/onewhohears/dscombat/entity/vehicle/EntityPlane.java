package com.onewhohears.dscombat.entity.vehicle;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.graph.AoaLiftKGraph;
import com.onewhohears.dscombat.data.graph.TurnRatesBySpeedGraph;
import com.onewhohears.dscombat.data.vehicle.DSCPhyCons;
import com.onewhohears.dscombat.data.vehicle.VehicleType;
import com.onewhohears.dscombat.data.vehicle.stats.PlaneStats;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityPlane extends EntityVehicle {

	private static final float AOA_CHANGE_RATE = 0.5f;

	private float aoa, liftK, airFoilSpeedSqr, airSpeed, fuselageAoa, fuselageLiftK;
	private float centripetalForce, centrifugalForce; 
	private double wingLiftMag, maxSpeedMod = 1, arcadeIgnoreGravityFactor;
	private Vec3 liftDir = Vec3.ZERO, liftForce = Vec3.ZERO;
	private boolean isArcadeMode = false;
	
	public EntityPlane(EntityType<? extends EntityPlane> entity, Level level, String defaultPreset) {
		super(entity, level, defaultPreset);
	}
	
	@Override
	public VehicleType getVehicleType() {
		return VehicleType.PLANE;
	}
	
	@Override
	public void directionAir(Quaternion q) {
		super.directionAir(q);
		if (!isOperational()) return;
		if (canControlPitch()) addMomentX(inputs.pitch * getPitchTorque(), true);
		if (canControlYaw()) addMomentY(inputs.yaw * getYawTorque(), true);
		if (canControlRoll()) {
			if (inputs.bothRoll) flatten(q, 0, getRollTorque(), false);
			else addMomentZ(inputs.roll * getRollTorque(), true);
		}
	}

	@Override
	public void tick() {
		if (tickCount % 10 == 0) isArcadeMode = DSCGameRules.isPlaneArcadeMode(getLevel());
		super.tick();
	}

	@Override
	public void tickAlways(Quaternion q) {
		super.tickAlways(q);
		if (isArcadeMode) {
			setForces(getForces().add(getWeightForce().scale(-getArcadeIgnoreGravityFactor())));
			if (isOnGround() && isFlapsDown()) setForces(getForces().add(0, 200, 0));
		} else setForces(getForces().add(getLiftForce(q)));
	}

	protected void calcIgnoreGravityFactor(Quaternion q) {
		Vec3 u = getDeltaMovement();
		Vec3 rollAxis = UtilAngles.getRollAxis(q);
		double speed = UtilGeometry.vecCompByNormAxis(u, rollAxis).length();
		double minTakeOffSpeed = getStats().max_speed * 0.5;
		arcadeIgnoreGravityFactor = Math.min(speed / minTakeOffSpeed, 1);
	}

	public double getArcadeIgnoreGravityFactor() {
		return arcadeIgnoreGravityFactor;
	}

	@Override
	public void calcAcc() {
		super.calcAcc();
		if (isArcadeMode && !isOnGround() && getArcadeIgnoreGravityFactor() == 1) {
			double speed = getDeltaMovement().length();
			Vec3 look = getLookAngle();
			setDeltaMovement(look.scale(speed));
		}
	}
	
	@Override
	protected void calcMoveStatsPre(Quaternion q) {
		super.calcMoveStatsPre(q);
		calcMaxSpeedMod();
		if (isArcadeMode) {
			aoa = 0;
			calcIgnoreGravityFactor(q);
			return;
		}
		calculateAOA(q);
		calculateLift(q);
		calculateCentripetalForce();
	}
	
	@Override
	public double getDriveAcc() {
		return 0;
	}
	
	@Override
	public double getMaxSpeedForMotion() {
		return super.getMaxSpeedForMotion() * getPlaneSpeedPercent() * getMaxSpeedFromThrottleMod();
	}
	
	public double getPlaneSpeedPercent() {
		return level.getGameRules().getInt(DSCGameRules.PLANE_SPEED_PERCENT) * 0.01;
	}
	
	public double getMaxSpeedFromThrottleMod() {
		return maxSpeedMod;
	}

	protected void calcMaxSpeedMod() {
		if (isOnGround()) maxSpeedMod = 1;
		float th = getCurrentThrottle();
		double goal;
		if (th < 0.5) goal = 0.7;
		else goal = 0.7 + 0.6 * (th - 0.5);
		maxSpeedMod = Mth.lerp(0.015, maxSpeedMod, goal);
	}
	
	@Override
	public boolean isBraking() {
		return inputs.special2 && isOnGround();
	}
	
	@Override
	public boolean isFlapsDown() {
		return inputs.special;
	}
	
	@Override
	public void tickAir(Quaternion q) {
		super.tickAir(q);
	}
	
	protected void calculateAOA(Quaternion q) {
		Vec3 u = getDeltaMovement();
		Vec3 pitchAxis = UtilAngles.getPitchAxis(q);
		liftDir = u.cross(pitchAxis).normalize();
        Vec3 airFoilAxes = UtilAngles.getRollAxis(q);
		airFoilSpeedSqr = (float)UtilGeometry.vecCompByNormAxis(u, airFoilAxes).lengthSqr();
		airSpeed = Mth.sqrt(airFoilSpeedSqr);
		float goalAOA, goalFuselageAOA;
		if (isOnGround() || UtilGeometry.isZero(u)) {
			goalAOA = 0;
			goalFuselageAOA = 0;
		} else {
            Vec3 wingNormal = UtilAngles.getYawAxis(q).scale(-1);
			goalAOA = (float) UtilGeometry.angleBetweenVecPlaneDegrees(u, wingNormal);
			Vec3 fuselageNormal = UtilAngles.rotationToVector(getYRot(), getXRot() + 90);
			goalFuselageAOA = (float) UtilGeometry.angleBetweenVecPlaneDegrees(u, fuselageNormal);
		}
		if (isFlapsDown()) goalAOA += getPlaneStats().flapsAOABias;
		// change in AOA shouldn't be instant
		aoa = Mth.lerp(AOA_CHANGE_RATE, aoa, goalAOA);
		fuselageAoa = Mth.lerp(AOA_CHANGE_RATE, fuselageAoa, goalFuselageAOA);
		// find liftK
		liftK = getWingLiftKGraph().getLerpFloat(aoa);
		fuselageLiftK = getFuselageLiftKGraph().getLerpFloat(fuselageAoa);
	}
	
	protected void calculateLift(Quaternion q) {
		// Lift = (angle of attack coefficient) * (air density) * (speed)^2 * (wing surface area) / 2
		wingLiftMag = liftK * airPressure * airFoilSpeedSqr * getWingSurfaceArea() * DSCPhyCons.LIFT * getWingLiftPercent();
        double fuselageLift = fuselageLiftK * airPressure * airFoilSpeedSqr * getFuselageLiftArea() * DSCPhyCons.LIFT;
		double cenScale = getCentripetalScale();
		liftForce = liftDir.scale(getLiftMag()).multiply(cenScale, 1, cenScale).add(0, fuselageLift, 0);
	}

	protected void calculateCentripetalForce() {
		Vec3 cenAxis = UtilAngles.getRollAxis(0, (getYRot()+90)*Mth.DEG_TO_RAD);
		centripetalForce = (float) UtilGeometry.vecCompMagDirByNormAxis(liftForce, cenAxis);
		if (Mth.abs(centripetalForce) < 0.01) centripetalForce = 0;
		if (Mth.abs(centrifugalForce) < 0.01) centrifugalForce = 0;
		// F = m * v * w
		centrifugalForce = getTotalMass() * xzSpeed * getYawRate()*Mth.DEG_TO_RAD;
	}
	
	public Vec3 getLiftForce(Quaternion q) {
		return liftForce;
	}
	
	public double getLiftMag() {
		return wingLiftMag;
	}
	
	@Override
	public Vec3 getThrustForce(Quaternion q) {
		return UtilAngles.getRollAxis(q).scale(getPushThrustMag());
	}
	
	@Override
	public double getCrossSectionArea() {
		double area = super.getCrossSectionArea();
		double aoaSin = Math.sin(Math.toRadians(aoa));
		area += getWingSurfaceArea() * aoaSin * getAOADragFactor();
		double aoaCos = Math.cos(Math.toRadians(aoa));
		if (isLandingGear()) area += 10.0 * aoaCos;
		if (isFlapsDown()) area += getWingSurfaceArea() / 4 * aoaCos;
		return area;
	}

	public float getAOA() {
		return aoa;
	}
	
	@Override
	public boolean isCustomBoundingBox() {
    	return true;
    }
	
	/**
	 * @return the surface area of the plane wings
	 */
	public final float getWingSurfaceArea() {
		return getPlaneStats().wing_area;
	}
	
	public float getFuselageLiftArea() {
		return getPlaneStats().fuselage_lift_area;
	}
	
	@Override
	public boolean isWeaponAngledDown() {
		return getPlaneStats().canAimDown && !onGround && inputs.special2;
	}
	
	@Override
	public boolean canAngleWeaponDown() {
    	return getPlaneStats().canAimDown;
    }
	
	public AoaLiftKGraph getWingLiftKGraph() {
		return getPlaneStats().getWingLiftKGraph();
	}
	
	public AoaLiftKGraph getFuselageLiftKGraph() {
		return getPlaneStats().getFuselageLiftKGraph();
	}

	@Override
	public boolean canBrake() {
		return onGround;
	}

	@Override
	public boolean canToggleLandingGear() {
		return true;
	}
	
	@Override
	public boolean canFlapsDown() {
    	return true;
    }
	
	public float getCentripetalForce() {
		return centripetalForce;
	}
	
	public float getCentrifugalForce() {
		return centrifugalForce;
	}
	
	@Override
	public boolean isStalling() {
		return Math.abs(getAOA()) >= getWingLiftKGraph().getCriticalAOA() || liftLost();
	}
	
	@Override
	public boolean isAboutToStall() {
		return Math.abs(getAOA()) >= getWingLiftKGraph().getWarnAOA() && !isFlapsDown();
	}
	
	@Override
	public boolean liftLost() {
		return !isOnGround() && getForces().y < -10 && getDeltaMovement().y < -0.1 && Math.abs(zRot) > 15;
	}
	
	@Override
	protected float calcDamageFromBullet(DamageSource source, float amount) {
		return amount * DSCGameRules.getBulletDamagePlaneFactor(level);
	}
	
	public float getWingLiftPercent() {
		float total = getPlaneStats().wingLiftHitboxNames.length;
		if (total == 0) return 1;
		float num = getNumberOfAliveHitboxes(getPlaneStats().wingLiftHitboxNames);
		return num / total;
	}

	@Override
	public float getControlMaxDeltaPitch() {
		if (isArcadeMode) return super.getControlMaxDeltaPitch();
		return getTurnRateGraph().getMaxPitchRate(airSpeed);
	}

	@Override
	public float getControlMaxDeltaYaw() {
		if (isArcadeMode) return super.getControlMaxDeltaYaw();
		return getTurnRateGraph().getMaxYawRate(airSpeed);
	}

	@Override
	public float getControlMaxDeltaRoll() {
		if (isArcadeMode) return super.getControlMaxDeltaRoll();
		return getTurnRateGraph().getMaxRollRate(airSpeed);
	}

	public TurnRatesBySpeedGraph getTurnRateGraph() {
		return getPlaneStats().getTurnRatesGraph();
	}

	public double getAOADragFactor() {
		return getPlaneStats().aoa_drag_factor;
	}

	public PlaneStats getPlaneStats() {
		return getStats().asPlane();
	}

	public double getCentripetalScale() {
		return getPlaneStats().centripetal_scale;
	}

}
