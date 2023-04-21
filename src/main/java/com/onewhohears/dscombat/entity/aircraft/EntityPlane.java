package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.aircraft.AircraftPresets;
import com.onewhohears.dscombat.data.aircraft.LiftKGraph;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.UtilParse;
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
	
	public static final EntityDataAccessor<Float> WING_AREA = SynchedEntityData.defineId(EntityPlane.class, EntityDataSerializers.FLOAT);
	
	public static final double CO_LIFT = 0.110;
	
	public final LiftKGraph liftKGraph;
	public final float flapsAOABias; // TODO 9.1 animate flaps down
	public final boolean canAimDown;
	
	private final float propellerRate = 3.141f;
	private float propellerRot = 0, propellerRotOld = 0, aoa = 0, liftK = 0, airFoilSpeedSqr = 0;
	private Vec3 liftDir = Vec3.ZERO, airFoilAxes = Vec3.ZERO;
	
	public EntityPlane(EntityType<? extends EntityPlane> entity, Level level, 
			RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item,
			float Ix, float Iy, float Iz, float explodeSize,
			LiftKGraph liftKGraph, float flapsAOABias, boolean canAimDown) {
		super(entity, level, engineSound, item, 
				false, Ix, Iy, Iz, explodeSize);
		this.liftKGraph = liftKGraph;
		this.flapsAOABias = flapsAOABias;
		this.canAimDown = canAimDown;
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
		setWingSurfaceArea(UtilParse.fixFloatNbt(nbt, "wing_area", presetNbt, 1));
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
		//debug("air        = "+air);
		//debug("wing speed = "+airFoilSpeedSqr);
		//debug("aoa        = "+aoa);
		//debug("liftK      = "+liftK);
		//debug("lift mag   = "+lift);
		return lift;
	}
	
	public double getLiftK() {
		return liftKGraph.getLift(aoa);
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
	
	@Override
	public boolean isWeaponAngledDown() {
		return canAimDown && !onGround && inputSpecial2;
	}

	@Override
	public boolean canBreak() {
		return onGround;
	}

}
