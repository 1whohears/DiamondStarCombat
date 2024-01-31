package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.aircraft.DSCPhysicsConstants;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData;
import com.onewhohears.dscombat.data.aircraft.VehicleStats;
import com.onewhohears.dscombat.data.aircraft.VehicleStats.PlaneStats;
import com.onewhohears.dscombat.entity.damagesource.WeaponDamageSource.WeaponDamageType;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityPlane extends EntityVehicle {
	
	protected PlaneStats planeStats;
	
	private float propellerRot = 0, propellerRotOld = 0;
	private float aoa = 0, liftK = 0, airFoilSpeedSqr = 0;
	private float centripetalForce, centrifugalForce; 
	private double liftMag;
	private Vec3 liftDir = Vec3.ZERO, airFoilAxes = Vec3.ZERO;
	
	public EntityPlane(EntityType<? extends EntityPlane> entity, Level level, ImmutableVehicleData vehicleData) {
		super(entity, level, vehicleData);
		planeStats = (PlaneStats)vehicleStats;
	}
	
	@Override
	public AircraftType getAircraftType() {
		return AircraftType.PLANE;
	}
	
	@Override
	public void clientTick() {
		super.clientTick();
		float th = getCurrentThrottle();
		propellerRotOld = propellerRot;
		propellerRot += th * vehicleData.spinRate;
	}
	
	@Override
	public void controlDirection(Quaternion q) {
		super.controlDirection(q);
	}
	
	@Override
	public void directionGround(Quaternion q) {
		super.directionGround(q);
	}
	
	@Override
	public void directionAir(Quaternion q) {
		super.directionAir(q);
		if (!isOperational()) return;
		addMomentX(inputs.pitch * getPitchTorque(), true);
		addMomentY(inputs.yaw * getYawTorque(), true);
		if (inputs.bothRoll) flatten(q, 0, getRollTorque(), false);
		else addMomentZ(inputs.roll * getRollTorque(), true);
	}
	
	@Override
	public void tickAlways(Quaternion q) {
		super.tickAlways(q);
		setForces(getForces().add(getLiftForce(q)));
	}
	
	@Override
	protected void calcMoveStatsPre(Quaternion q) {
		super.calcMoveStatsPre(q);
		calculateAOA(q);
		calculateLift(q);
	}
	
	@Override
	public void tickGround(Quaternion q) {
		super.tickGround(q);
	}
	
	@Override
	public double getDriveAcc() {
		return 0;
	}
	
	@Override
	public double getMaxSpeedForMotion() {
		return super.getMaxSpeedForMotion() * getPlaneSpeedPercent();
	}
	
	public double getPlaneSpeedPercent() {
		return level.getGameRules().getInt(DSCGameRules.PLANE_SPEED_PERCENT) * 0.01;
	}

	// TODO: nerf brakes? or make their strength configurable?
	@Override
	public boolean isBraking() {
		return inputs.special2 && isOnGround();
	}
	
	public boolean isFlapsDown() {
		return inputs.special;
	}
	
	@Override
	public void tickAir(Quaternion q) {
		super.tickAir(q);
	}
	
	protected void calculateAOA(Quaternion q) {
		Vec3 u = getDeltaMovement();
		//System.out.println("u = "+u);
		liftDir = UtilAngles.getYawAxis(q);
		//System.out.println("liftDir = "+liftDir);
		airFoilAxes = UtilAngles.getRollAxis(q);
		//System.out.println("airFoilAxes = "+airFoilAxes);
		airFoilSpeedSqr = (float)UtilGeometry.vecCompByNormAxis(u, airFoilAxes).lengthSqr();
		//System.out.println("airFoilSpeedSqr = "+airFoilSpeedSqr);
		if (isOnGround() || UtilGeometry.isZero(u)) {
			aoa = 0;
		} else {
			aoa = (float)UtilGeometry.angleBetweenVecPlaneDegrees(u, liftDir.scale(-1));
		}
		if (isFlapsDown()) aoa += vehicleData.flapsAOABias;
		liftK = (float) getLiftK();
		//System.out.println("liftK = "+liftK);
	}
	
	public float getYawRate() {
		return getYRot() - yRotO;
	}
	
	protected void calculateLift(Quaternion q) {
		// Lift = (angle of attack coefficient) * (air density) * (speed)^2 * (wing surface area) / 2
		double wing = getWingSurfaceArea();
		liftMag = liftK * airPressure * airFoilSpeedSqr * wing * DSCPhysicsConstants.LIFT;
		Vec3 lift = getLiftForce(q);
		Vec3 cenAxis = UtilAngles.getRollAxis(0, (getYRot()+90)*Mth.DEG_TO_RAD);
		centripetalForce = (float) UtilGeometry.vecCompMagDirByNormAxis(lift, cenAxis);
		if (Mth.abs(centripetalForce) < 0.01) centripetalForce = 0;
		if (Mth.abs(centrifugalForce) < 0.01) centrifugalForce = 0;
		// F = m * v * w
		centrifugalForce = getTotalMass() * xzSpeed * getYawRate()*Mth.DEG_TO_RAD;
		//debug("#####");
		//debug("centripetalForce = "+centripetalForce);
		//debug("centrifugalForce = "+centrifugalForce);
	}
	
	public Vec3 getLiftForce(Quaternion q) {
		double cenScale = DSCPhysicsConstants.CENTRIPETAL_SCALE;
		Vec3 liftForce = liftDir.scale(getLiftMag())
			.multiply(cenScale, 1, cenScale);
		return liftForce;
	}
	
	public double getLiftMag() {
		return liftMag;
	}
	
	public double getLiftK() {
		return vehicleData.liftKGraph.getLift(aoa);
	}
	
	@Override
	public Vec3 getThrustForce(Quaternion q) {
		Vec3 direction = UtilAngles.getRollAxis(q);
		Vec3 thrustForce = direction.scale(getPushThrustMag());
		return thrustForce;
	}
	
	@Override
	public double getCrossSectionArea() {
		double a = super.getCrossSectionArea();
		a += getWingSurfaceArea() * Math.sin(Math.toRadians(aoa));
		if (isLandingGear()) a += 10.0 * Math.cos(Math.toRadians(aoa));
		if (isFlapsDown()) a += getWingSurfaceArea() / 4 * Math.cos(Math.toRadians(aoa));
		return a;
	}
	
	public float getPropellerRotation(float partialTicks) {
		return Mth.lerp(partialTicks, propellerRotOld, propellerRot);
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
		return planeStats.wing_area;
	}
	
	@Override
	public boolean isWeaponAngledDown() {
		return vehicleData.canAimDown && !onGround && inputs.special2;
	}
	
	@Override
	public boolean canAngleWeaponDown() {
    	return vehicleData.canAimDown;
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
	public float calcProjDamageBySource(DamageSource source, float amount) {
		WeaponDamageType wdt = WeaponDamageType.byId(source.getMsgId());
		if (wdt != null && wdt.isContact()) return amount*DSCGameRules.getBulletDamagePlaneFactor(level);
		return super.calcProjDamageBySource(source, amount);
	}
	
	@Override
	public boolean isStalling() {
		return Math.abs(getAOA()) >= vehicleData.liftKGraph.getCriticalAOA() || liftLost();
	}
	
	@Override
	public boolean isAboutToStall() {
		return Math.abs(getAOA()) >= vehicleData.liftKGraph.getWarnAOA() && !isFlapsDown();
	}
	
	@Override
	public boolean liftLost() {
		return !isOnGround() && getForces().y < -10 && getDeltaMovement().y < -0.1 && Math.abs(zRot) > 15;
	}

	@Override
	protected VehicleStats createVehicleStats() {
		return new PlaneStats();
	}

}
