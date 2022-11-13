package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityPlane extends EntityAbstractAircraft {
	
	private final float propellerRate = 3.141f;
	private float propellerRot, propellerRotOld;
	
	public EntityPlane(EntityType<? extends EntityPlane> entity, Level level, ResourceLocation texture, SoundEvent engineSound) {
		super(entity, level, texture, engineSound);
	}
	
	@Override
	public void clientTick() {
		super.clientTick();
		float th = this.getCurrentThrottle();
		propellerRotOld = propellerRot;
		propellerRot += th * propellerRate;
		//System.out.println("plane client tick rot "+propellerRot);
	}
	
	@Override
	public void controlDirection(Quaternion q) {
		super.controlDirection(q);
		q.mul(new Quaternion(Vector3f.ZP, inputRoll*getMaxDeltaRoll(), true));
		q.mul(new Quaternion(Vector3f.XN, inputPitch*getMaxDeltaPitch(), true));
		q.mul(new Quaternion(Vector3f.YN, inputYaw*getMaxDeltaYaw(), true));
	}
	
	@Override
	public void tickGround(Quaternion q) {
		super.tickGround(q);
		Vec3 motion = getDeltaMovement();
		motion = motion.add(getLiftForce(q));
		setDeltaMovement(motion);
	}
	
	@Override
	public void tickAir(Quaternion q) {
		super.tickAir(q);
		Vec3 motion = getDeltaMovement();
		motion = motion.add(getLiftForce(q));
		setDeltaMovement(motion);
	}
	
	public Vec3 getLiftForce(Quaternion q) {
		Vec3 direction = UtilAngles.getYawAxis(q);
		//System.out.println("lift direction = "+direction);
		Vec3 u = getDeltaMovement();
		Vec3 v = UtilAngles.getRollAxis(q);
		double zSpeedSqr = UtilGeometry.componentOfVecByAxis(u, v).lengthSqr();
		Vec3 liftForce = direction.scale(getLift(zSpeedSqr));
		//System.out.println("lift force = "+liftForce);
		return liftForce;
	}
	
	public double getLift(double zSpeedSqr) {
		// Lift = (angle of attack coefficient) * (air density) * (speed)^2 * (wing surface area) / 2
		//double ac = 0.06;
		double ac = 0.120;
		double air = UtilEntity.getAirPressure(getY());
		//double speedSqr = zSpeedSqr;
		double wing = getSurfaceArea();
		double lift = ac * air * zSpeedSqr * wing / 2d;
		//System.out.println("LIFT = "+lift);
		return lift;
	}
	
	@Override
	public Vec3 getThrustForce(Quaternion q) {
		Vec3 direction = UtilAngles.getRollAxis(q);
		Vec3 thrustForce = direction.scale(getThrust());
		//System.out.println("thrust force = "+thrustForce);
		return thrustForce;
	}
	
	public float getPropellerRotation(float partialTicks) {
		return Mth.lerp(partialTicks, propellerRotOld, propellerRot);
	}

}
