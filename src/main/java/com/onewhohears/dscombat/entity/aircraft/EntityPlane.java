package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.data.AircraftTextures;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntityPlane extends EntityAircraft {
	
	private final float propellerRate = 3.141f, gearAOABias = 10f;
	private float propellerRot = 0, propellerRotOld = 0, aoa = 0, liftK = 0, airFoilSpeedSqr = 0;
	private Vec3 liftDir = Vec3.ZERO, airFoilAxes = Vec3.ZERO;
	
	public EntityPlane(EntityType<? extends EntityPlane> entity, Level level, 
			AircraftTextures textures, RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item) {
		super(entity, level, textures, engineSound, item);
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
		if (isOnGround()) {
			resetTorque();
			EulerAngles angles = UtilAngles.toDegrees(q);
			float dRoll = 5f;
			float dPitch = 5f;
			float roll, pitch;
			if (Math.abs(angles.roll) < dRoll) roll = (float) -angles.roll;
			else roll = -(float)Math.signum(angles.roll) * dRoll;
			if (Math.abs(angles.pitch) < dPitch) pitch = (float) -angles.pitch;
			else pitch = -(float)Math.signum(angles.pitch) * dPitch;
			q.mul(Vector3f.XP.rotationDegrees(pitch));
			q.mul(Vector3f.ZP.rotationDegrees(roll));
			q.mul(Vector3f.YN.rotationDegrees(inputYaw*getMaxDeltaYaw()));
		} else {
			addTorqueX(inputPitch * getAccelerationPitch(), true);
			addTorqueY(inputYaw * getAccelerationYaw(), true);
			addTorqueZ(inputRoll * getAccelerationRoll(), true);
		}
		super.controlDirection(q);
	}
	
	@Override
	public void tickMovement(Quaternion q) {
		super.tickMovement(q);
	}
	
	@Override
	public void tickGround(Quaternion q) {
		super.tickGround(q);
		calculateAOA(q);
		Vec3 motion = getDeltaMovement();
		motion = motion.add(getLiftForce(q));
		setDeltaMovement(motion);
	}
	
	@Override
	public void tickAir(Quaternion q) {
		super.tickAir(q);
		calculateAOA(q);
		Vec3 motion = getDeltaMovement();
		motion = motion.add(getLiftForce(q));
		setDeltaMovement(motion);
	}
	
	public void calculateAOA(Quaternion q) {
		Vec3 u = getDeltaMovement();
		liftDir = UtilAngles.getYawAxis(q);
		airFoilAxes = UtilAngles.getRollAxis(q);
		airFoilSpeedSqr = (float) UtilGeometry.componentOfVecByAxis(u, airFoilAxes).lengthSqr();
		if (isOnGround()) {
			aoa = 0;
		} else {
			aoa = (float) UtilGeometry.angleBetweenDegrees(airFoilAxes, u);
			double uy = UtilGeometry.componentMagSqrDirByAxis(u.normalize(), liftDir);
			double vy = UtilGeometry.componentMagSqrDirByAxis(airFoilAxes, liftDir);
			if (vy < uy) aoa *= -1;
		}
		if (isLandingGear()) aoa += gearAOABias;
		liftK = (float) getLiftK();
	}
	
	public Vec3 getLiftForce(Quaternion q) {
		Vec3 liftForce = liftDir.scale(getLiftMag());
		return liftForce;
	}
	
	public double getLiftMag() {
		// Lift = (angle of attack coefficient) * (air density) * (speed)^2 * (wing surface area) / 2
		double air = UtilEntity.getAirPressure(getY());
		double wing = getSurfaceArea();
		double lift = liftK * air * airFoilSpeedSqr * wing / 2.0;
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
	
	public float getPropellerRotation(float partialTicks) {
		return Mth.lerp(partialTicks, propellerRotOld, propellerRot);
	}

	public float getAOA() {
		return aoa;
	}
	
	@Override
	protected float getTorqueDragMag() {
		return 0.15f;
	}

}
