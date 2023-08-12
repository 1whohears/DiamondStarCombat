package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.aircraft.AircraftPresets;
import com.onewhohears.dscombat.data.aircraft.LiftKGraph;
import com.onewhohears.dscombat.util.UtilParse;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntityPlane extends EntityAircraft {
	
	public static final EntityDataAccessor<Float> WING_AREA = SynchedEntityData.defineId(EntityPlane.class, EntityDataSerializers.FLOAT);
	
	public static final double CO_LIFT = Config.SERVER.coLift.get();
	
	public final LiftKGraph liftKGraph;
	public final float propellerRate, flapsAOABias;
	public final boolean canAimDown;
	
	private float propellerRot = 0, propellerRotOld = 0, aoa = 0, liftK = 0, airFoilSpeedSqr = 0;
	private float centripetalForce, centrifugalForce; 
	private double liftMag;
	private Vec3 liftDir = Vec3.ZERO, airFoilAxes = Vec3.ZERO;
	
	public EntityPlane(EntityType<? extends EntityPlane> entity, Level level, 
			AircraftPreset defaultPreset,
			RegistryObject<SoundEvent> engineSound,
			float Ix, float Iy, float Iz, float explodeSize,
			LiftKGraph liftKGraph, float flapsAOABias, boolean canAimDown, float propellerRate) {
		super(entity, level, defaultPreset, engineSound,
				false, Ix, Iy, Iz, explodeSize);
		this.liftKGraph = liftKGraph;
		this.flapsAOABias = flapsAOABias;
		this.canAimDown = canAimDown;
		this.propellerRate = propellerRate;
	}
	
	@Override
	public void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(WING_AREA, 10f);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		CompoundTag presetNbt = AircraftPresets.get().getPreset(preset).getDataAsNBT();
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
		// IDEA 1 turn assist button? (maybe have it as a server config if it's available or not)
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
	public boolean isBreaking() {
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
		if (isFlapsDown()) aoa += flapsAOABias;
		liftK = (float) getLiftK();
		//System.out.println("liftK = "+liftK);
	}
	
	public float getYawRate() {
		return getYRot() - yRotO;
	}
	
	protected void calculateLift(Quaternion q) {
		// Lift = (angle of attack coefficient) * (air density) * (speed)^2 * (wing surface area) / 2
		double wing = getWingSurfaceArea();
		liftMag = liftK * airPressure * airFoilSpeedSqr * wing * CO_LIFT;
		Vec3 lift = getLiftForce(q);
		Vec3 cenAxis = UtilAngles.getRollAxis(0, (getYRot()+90)*Mth.DEG_TO_RAD);
		centripetalForce = (float) UtilGeometry.vecCompMagDirByNormAxis(lift, cenAxis);
		// F = m * v * w
		centrifugalForce = getTotalMass() * xzSpeed * getYawRate()*Mth.DEG_TO_RAD;
		if (Mth.abs(centripetalForce) < 0.01) centripetalForce = 0;
		if (Mth.abs(centrifugalForce) < 0.01) centrifugalForce = 0;
		//debug("#####");
		//debug("centripetalForce = "+centripetalForce);
		//debug("centrifugalForce = "+centrifugalForce);
	}
	
	public Vec3 getLiftForce(Quaternion q) {
		Vec3 liftForce = liftDir.scale(getLiftMag());
		return liftForce;
	}
	
	public double getLiftMag() {
		return liftMag;
	}
	
	public double getLiftK() {
		return liftKGraph.getLift(aoa);
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
		return canAimDown && !onGround && inputs.special2;
	}
	
	@Override
	public boolean canAngleWeaponDown() {
    	return canAimDown;
    }

	@Override
	public boolean canBreak() {
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
	public void sounds() {
		super.sounds();
    	if (level.isClientSide && isOperational()) {
    		Minecraft m = Minecraft.getInstance();
    		Player p = m.player;
    		if (!isVehicleOf(p)) return;
    		// TODO 5.2 bitchin betty
    		// pull up
    		// over g
    		// aoa stall
    		// gear still out
    	}
    } 

}
