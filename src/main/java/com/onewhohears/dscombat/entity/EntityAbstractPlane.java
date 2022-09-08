package com.onewhohears.dscombat.entity;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class EntityAbstractPlane extends EntityAbstractAircraft {

	public EntityAbstractPlane(EntityType<? extends EntityAbstractAircraft> entity, Level level) {
		super(entity, level);
	}
	
	@Override
	public void tickGround(Quaternion q) {
		super.tickAir(q);
		Vec3 motion = getDeltaMovement();
		motion.add(getLiftForce(q));
		setDeltaMovement(motion);
	}
	
	@Override
	public void tickAir(Quaternion q) {
		super.tickAir(q);
		Vec3 motion = getDeltaMovement();
		motion.add(getLiftForce(q));
		setDeltaMovement(motion);
	}
	
	public Vec3 getLiftForce(Quaternion q) {
		Vec3 direction = UtilAngles.getYawAxis(q);
		//System.out.println("lift direction = "+direction);
		Vec3 u = getDeltaMovement();
		Vec3 v = UtilAngles.getRollAxis(q);
		double vl2 = v.lengthSqr();
		double zSpeedSqr = v.scale(u.dot(v) / vl2).lengthSqr();
		Vec3 liftForce = direction.scale(getLift(zSpeedSqr));
		//System.out.println("lift force = "+liftForce);
		return liftForce;
	}
	
	public double getLift(double zSpeedSqr) {
		// Lift = (angle of attack coefficient) * (air density) * (speed)^2 * (wing surface area) / 2
		double ac = 0.05;
		double air = 1;
		//double speedSqr = zSpeedSqr;
		double wing = 1;
		return ac * air * zSpeedSqr * wing / 2;
	}

}
