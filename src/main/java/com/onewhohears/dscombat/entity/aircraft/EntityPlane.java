package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityPlane extends EntityAbstractAircraft {
	
	public EntityPlane(EntityType<? extends EntityPlane> entity, Level level, ResourceLocation texture) {
		super(entity, level, texture);
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
		double vl2 = v.lengthSqr();
		double zSpeedSqr = v.scale(u.dot(v) / vl2).lengthSqr();
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

}
