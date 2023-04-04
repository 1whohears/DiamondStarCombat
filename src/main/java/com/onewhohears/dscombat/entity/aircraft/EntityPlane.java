package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.AircraftTextures;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntityPlane extends EntityAircraft {
	
	public static final double CO_LIFT = 0.500;
	
	private final float propellerRate = 3.141f, flapsAOABias = 10f;
	private float propellerRot = 0, propellerRotOld = 0, aoa = 0, liftK = 0, airFoilSpeedSqr = 0;
	private Vec3 liftDir = Vec3.ZERO, airFoilAxes = Vec3.ZERO;
	
	public EntityPlane(EntityType<? extends EntityPlane> entity, Level level, 
			AircraftTextures textures, RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item) {
		super(entity, level, textures, engineSound, item, false);
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
		propellerRot += th * propellerRate;
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
		addTorqueX(inputPitch * getAccelerationPitch(), true);
		addTorqueY(inputYaw * getAccelerationYaw(), true);
		if (inputBothRoll) flatten(q, 0, getAccelerationRoll());
		else addTorqueZ(inputRoll * getAccelerationRoll(), true);
	}
	
	@Override
	public void tickAlways(Quaternion q) {
		super.tickAlways(q);
		forces = forces.add(getLiftForce(q));
	}
	
	@Override
	protected void calcMoveStatsPre(Quaternion q) {
		super.calcMoveStatsPre(q);
		calculateAOA(q);
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
	public void tickAir(Quaternion q) {
		// TODO turn assist button
		super.tickAir(q);
	}
	
	protected void calculateAOA(Quaternion q) {
		Vec3 u = getDeltaMovement();
		//System.out.println("u = "+u);
		liftDir = UtilAngles.getYawAxis(q);
		//System.out.println("liftDir = "+liftDir);
		airFoilAxes = UtilAngles.getRollAxis(q);
		//System.out.println("airFoilAxes = "+airFoilAxes);
		airFoilSpeedSqr = (float) UtilGeometry.componentOfVecByAxis(u, airFoilAxes).lengthSqr();
		//System.out.println("airFoilSpeedSqr = "+airFoilSpeedSqr);
		if (isOnGround() || UtilGeometry.isZero(u)) {
			aoa = 0;
		} else {
			aoa = (float) UtilGeometry.angleBetweenDegrees(airFoilAxes, u);
			if (liftDir.dot(u) > 0) aoa *= -1;
		}
		if (inputSpecial) aoa += flapsAOABias;
		liftK = (float) getLiftK();
		//System.out.println("liftK = "+liftK);
	}
	
	public Vec3 getLiftForce(Quaternion q) {
		Vec3 liftForce = liftDir.scale(getLiftMag());
		return liftForce;
	}
	
	public double getLiftMag() {
		// Lift = (angle of attack coefficient) * (air density) * (speed)^2 * (wing surface area) / 2
		double air = UtilEntity.getAirPressure(getY());
		double wing = getWingSurfaceArea();
		double lift = liftK * air * airFoilSpeedSqr * wing * CO_LIFT;
		return lift;
	}
	
	public double getLiftK() {
		float maxAngle = 30.0f;
		if (aoa > maxAngle || aoa < -maxAngle) return 0;
		float stallAngle = 20.0f;
		double stallK = 0.210;
		float aoaAbs = Math.abs(aoa);
		double r = 0;
		if (aoaAbs <= stallAngle) {
			double a = -stallK / (stallAngle*stallAngle);
			double b = -2*stallAngle*a;
			r =  a*aoaAbs*aoaAbs + b*aoaAbs;
		} else if (aoaAbs > stallAngle) {
			double a = -stallK / (maxAngle*maxAngle + stallAngle*stallAngle - 2*maxAngle*stallAngle);
			double b = -2*stallAngle*a;
			double c = stallK + a*stallAngle*stallAngle;
			r = a*aoaAbs*aoaAbs + b*aoaAbs + c;
		}
		return Math.signum(aoa) * r;
	}
	
	@Override
	public Vec3 getThrustForce(Quaternion q) {
		Vec3 direction = UtilAngles.getRollAxis(q);
		Vec3 thrustForce = direction.scale(getThrustMag());
		return thrustForce;
	}
	
	@Override
	public double getSurfaceArea() {
		double a = super.getSurfaceArea();
		a += getWingSurfaceArea() * Math.sin(Math.toRadians(aoa));
		if (isLandingGear()) a += 2.0 * Math.cos(Math.toRadians(aoa));
		if (inputSpecial) a += getWingSurfaceArea() / 8 * Math.cos(Math.toRadians(aoa));
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

}
