package com.onewhohears.dscombat.entity.vehicle;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.graph.AoaLiftKGraph;
import com.onewhohears.dscombat.data.graph.FloatFloatGraph;
import com.onewhohears.dscombat.data.graph.TurnRatesBySpeedGraph;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.dscombat.data.vehicle.VehicleType;
import com.onewhohears.dscombat.data.vehicle.physics.LiftSurfaceInstance;
import com.onewhohears.dscombat.data.vehicle.stats.PlaneStats;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityPlane extends EntityVehicle {

	private float aoa, liftK, airFoilSpeedSqr, airSpeed, fuselageAoa, fuselageLiftK, dragC;
	private float centripetalForce, centrifugalForce, aoaTurnRateMod = 1;
	private double wingLiftMag, arcadeIgnoreGravityFactor;
	private Vec3 liftDir = Vec3.ZERO, liftForce = Vec3.ZERO;
	private boolean isArcadeMode = false;
	private int pullUpWarningTicks, altitudeWarningTicks;
	
	public EntityPlane(EntityType<? extends EntityPlane> entity, Level level, String defaultPreset) {
		super(entity, level, defaultPreset);
	}
	
	@Override
	public VehicleType getVehicleType() {
		return VehicleType.PLANE;
	}

	@Override
	public void tick() {
		if (tickCount % 10 == 0) isArcadeMode = DSCGameRules.isPlaneArcadeMode(getLevel());
		super.tick();
	}

	@Override
	public void calcUniversalForces(Quaternion q) {
		super.calcUniversalForces(q);
		if (isArcadeMode) {
			addForce(getWeightForce().scale(-getArcadeIgnoreGravityFactor()));
			if (isOnGround() && isFlapsDown()) addForce(new Vec3(0, 200, 0));
		} //else addForce(getLiftForce(q));
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
	public void calcMoveStatsPre(Quaternion q) {
		super.calcMoveStatsPre(q);
		if (isArcadeMode) {
			aoa = 0;
			calcIgnoreGravityFactor(q);
			return;
		}
		calculateAOA(q);
		calculateLift(q);
		calculateCentripetalForce();
		double ym = getDeltaMovement().y;
		if (ym <= -DSCPhyCons.COLLIDE_SPEED && getAltitude() / -ym <= 80) ++pullUpWarningTicks;
		else pullUpWarningTicks = 0;
		if (getDeltaMovement().y < 0 && getAltitude() < 40) ++altitudeWarningTicks;
		else altitudeWarningTicks = 0;
	}
	
	@Override
	public double getDriveAcc() {
		return 0;
	}
	
	@Override
	public double getMaxSpeedForMotion() {
		return super.getMaxSpeedForMotion();
	}

	@Override
	public double getMaxSpeedFactor() {
		return super.getMaxSpeedFactor() * Config.SERVER.planeSpeedFactor.get();
	}
	
	@Override
	public boolean isGroundBraking() {
		return inputs.special2;
	}
	
	@Override
	public boolean isFlapsDown() {
		return inputs.special;
	}
	
	protected void calculateAOA(Quaternion q) {
		Vec3 u = getDeltaMovement();
		Vec3 pitchAxis = UtilAngles.getPitchAxis(q);
		liftDir = u.cross(pitchAxis).normalize();
        Vec3 airFoilAxes = UtilAngles.getRollAxis(q);
		airFoilSpeedSqr = (float)UtilGeometry.vecCompByNormAxis(u, airFoilAxes).lengthSqr();
		airSpeed = Mth.sqrt(airFoilSpeedSqr);
		float goalAOA;
		//float goalFuselageAOA;
		if (isOnGround() || UtilGeometry.isZero(u)) {
			goalAOA = 0;
			//goalFuselageAOA = 0;
		} else {
            Vec3 wingNormal = UtilAngles.getYawAxis(q).scale(-1);
			goalAOA = (float) UtilGeometry.angleBetweenVecPlaneDegrees(u, wingNormal);
			//Vec3 fuselageNormal = UtilAngles.rotationToVector(getYRot(), getXRot() + 90);
			//goalFuselageAOA = (float) UtilGeometry.angleBetweenVecPlaneDegrees(u, fuselageNormal);
		}
		if (isFlapsDown()) goalAOA += getPlaneStats().flapsAOABias;
		// change in AOA shouldn't be instant
		aoa = Mth.lerp(LiftSurfaceInstance.getAOAChangeRate(this), aoa, goalAOA);
		//fuselageAoa = Mth.lerp(DSCPhyCons.AOA_CHANGE_RATE, fuselageAoa, goalFuselageAOA);
		// find liftK
		float speedScaleSqr = (float) (1 / getHorizontalSpeedScale() / getHorizontalSpeedScale() * 400);
        liftK = getWingLiftKGraph().getLerpFloat(aoa) * speedScaleSqr;
		//fuselageLiftK = getFuselageLiftKGraph().getLerpFloat(fuselageAoa) * speedScaleSqr;
		// dragC
		//dragC = getDragAoaGraph().getLerpFloat(aoa) * DSCPhyCons.DRAG_SCALE;
		// aoaTurnRateMod
		double goalAoaTurnRateMod;
		if (isAboutToStall()) {
			goalAoaTurnRateMod = 0.05;
		} else {
			goalAoaTurnRateMod = 1;
		}
		if (goalAoaTurnRateMod < aoaTurnRateMod) {
			aoaTurnRateMod = (float) Mth.lerp(0.05, aoaTurnRateMod, goalAoaTurnRateMod);
		} else {
			aoaTurnRateMod = (float) Mth.lerp(0.5, aoaTurnRateMod, goalAoaTurnRateMod);
		}
	}
	
	protected void calculateLift(Quaternion q) {
		// Lift = (angle of attack coefficient) * (air density) * (speed)^2 * (wing surface area) / 2
		wingLiftMag = liftK * getFluidDensity() * airFoilSpeedSqr * getWingSurfaceArea() * getWingLiftPercent();
        double fuselageLift = fuselageLiftK * getFluidDensity() * airFoilSpeedSqr * getFuselageLiftArea();
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

	/*@Override
	public double getDragCoefficient() {
		return dragC;
	}*/

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

	public FloatFloatGraph getDragAoaGraph() {
		return getPlaneStats().getDragAoaGraph();
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
		if (isArcadeMode || isTestMode()) return super.getControlMaxDeltaPitch();
		return getTurnRateGraph().getMaxPitchRate(airSpeed) * aoaTurnRateMod;
	}

	@Override
	public float getControlMaxDeltaYaw() {
		if (isArcadeMode || isTestMode()) return super.getControlMaxDeltaYaw();
		return getTurnRateGraph().getMaxYawRate(airSpeed) * aoaTurnRateMod;
	}

	@Override
	public float getControlMaxDeltaRoll() {
		if (isArcadeMode || isTestMode()) return super.getControlMaxDeltaRoll();
		return getTurnRateGraph().getMaxRollRate(airSpeed) * aoaTurnRateMod;
	}

	public TurnRatesBySpeedGraph getTurnRateGraph() {
		return getPlaneStats().getTurnRatesGraph();
	}

	public PlaneStats getPlaneStats() {
		return getStats().asPlane();
	}

	public double getCentripetalScale() {
		return getPlaneStats().centripetal_scale;
	}

	@Override
	public int getPullUpWarningTicks() {
		return pullUpWarningTicks;
	}

	@Override
	public int getAltitudeWarningTicks() {
		return altitudeWarningTicks;
	}

}
