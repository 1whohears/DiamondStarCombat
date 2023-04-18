package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.AircraftPresets;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntityPlane extends EntityAircraft {
	
	public static final EntityDataAccessor<Float> WING_AREA = SynchedEntityData.defineId(EntityAircraft.class, EntityDataSerializers.FLOAT);
	
	public static final double CO_LIFT = 0.500;
	
	// TODO 9.1 animate flaps down
	private final float propellerRate = 3.141f, flapsAOABias = 10f;
	private float propellerRot = 0, propellerRotOld = 0, aoa = 0, liftK = 0, airFoilSpeedSqr = 0;
	private Vec3 liftDir = Vec3.ZERO, airFoilAxes = Vec3.ZERO;
	
	public EntityPlane(EntityType<? extends EntityPlane> entity, Level level, 
			RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item,
			float Ix, float Iy, float Iz) {
		super(entity, level, engineSound, item, false, Ix, Iy, Iz);
	}
	
	@Override
	public void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(WING_AREA, 10f);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		CompoundTag presetNbt = AircraftPresets.getPreset(defaultPreset);
		setWingSurfaceArea(UtilEntity.fixFloatNbt(nbt, "wing_area", presetNbt, 1));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putFloat("wing_area", getWingSurfaceArea());
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
		if (!isOperational()) return;
		// IDEA 3 turn assist button
		addMomentX(inputPitch * getPitchTorque(), true);
		addMomentY(inputYaw * getYawTorque(), true);
		if (inputBothRoll) flatten(q, 0, getRollTorque(), false);
		else addMomentZ(inputRoll * getRollTorque(), true);
	}
	
	@Override
	public void tickAlways(Quaternion q) {
		super.tickAlways(q);
		setForces(getForces().add(getLiftForce(q)));
		// TODO 6 inputSpecial2 can lower front weapon angle on some planes
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
	public boolean isBreaking() {
		return inputSpecial2 && isOnGround();
	}
	
	public boolean isFlapsDown() {
		return inputSpecial;
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
			aoa = (float) UtilGeometry.angleBetweenDegrees(airFoilAxes, u);
			if (liftDir.dot(u) > 0) aoa *= -1;
		}
		if (isFlapsDown()) aoa += flapsAOABias;
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
		// TODO 7 use a LiftK vs AOA graph
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
	public double getCrossSectionArea() {
		double a = super.getCrossSectionArea();
		a += getWingSurfaceArea() * Math.sin(Math.toRadians(aoa));
		if (isLandingGear()) a += 4.0 * Math.cos(Math.toRadians(aoa));
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
		return entityData.get(WING_AREA);
	}
	
	public final void setWingSurfaceArea(float area) {
		if (area < 0) area = 0;
		entityData.set(WING_AREA, area);
	}

}
