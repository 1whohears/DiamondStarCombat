package com.onewhohears.dscombat.entity.aircraft;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class EntityPlane extends EntityAbstractAircraft {
	
	private final float propellerRate = 3.141f;
	private float propellerRot, propellerRotOld;
	
	public EntityPlane(EntityType<? extends EntityPlane> entity, Level level, 
			ResourceLocation texture, RegistryObject<SoundEvent> engineSound, RegistryObject<Item> item) {
		super(entity, level, texture, engineSound, item);
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
		if (isOnGround()) {
			EulerAngles angles = UtilAngles.toDegrees(q);
			//System.out.println("degrees "+angles);
			float dRoll = 5f;
			float dPitch = 5f;
			float roll, pitch;
			if (Math.abs(angles.roll) < dRoll) roll = (float) -angles.roll;
			else roll = -(float)Math.signum(angles.roll) * dRoll;
			if (Math.abs(angles.pitch) < dPitch) pitch = (float) -angles.pitch;
			else pitch = -(float)Math.signum(angles.pitch) * dPitch;
			q.mul(new Quaternion(Vector3f.ZP, roll, true));
			q.mul(new Quaternion(Vector3f.XP, pitch, true));
			q.mul(new Quaternion(Vector3f.YN, inputYaw*getMaxDeltaYaw(), true));
		} else {
			q.mul(new Quaternion(Vector3f.ZP, inputRoll*getMaxDeltaRoll(), true));
			q.mul(new Quaternion(Vector3f.XN, inputPitch*getMaxDeltaPitch(), true));
			q.mul(new Quaternion(Vector3f.YN, inputYaw*getMaxDeltaYaw(), true));
		}
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
		System.out.println("LIFT DIRECTION = "+direction);
		Vec3 u = getDeltaMovement();
		Vec3 v = UtilAngles.getRollAxis(q);
		double zSpeedSqr = UtilGeometry.componentOfVecByAxis(u, v).lengthSqr();
		double aoa = UtilGeometry.angleBetweenDegrees(v, u);
		double uy = UtilGeometry.componentMagSqrDirByAxis(u.normalize(), direction);
		double vy = UtilGeometry.componentMagSqrDirByAxis(v, direction);
		System.out.println("move y = "+uy+" plane y = "+vy);
		if (vy < uy) aoa *= -1;
		// TODO fix this madness
		//if (v.y < u.normalize().y) aoa *= -1;
 		System.out.println("zSpeedSqr = "+zSpeedSqr);
		System.out.println("aoa = "+aoa);
		Vec3 liftForce = direction.scale(getLiftMag(zSpeedSqr, aoa));
		System.out.println("lift force = "+liftForce);
		return liftForce;
	}
	
	public double getLiftMag(double zSpeedSqr, double aoa) {
		// Lift = (angle of attack coefficient) * (air density) * (speed)^2 * (wing surface area) / 2
		//double ac = 0.06;
		double ac = getLiftK(aoa);
		System.out.println("liftK = "+ac);
		double air = UtilEntity.getAirPressure(getY());
		double wing = getSurfaceArea();
		double lift = ac * air * zSpeedSqr * wing / 2.0;
		//System.out.println("LIFT = "+lift);
		return lift;
	}
	
	public double getLiftK(double aoa) {
		double minAngle = -10.0;
		double maxAngle = 30.0;
		if (aoa < minAngle) return 0;
		if (aoa > maxAngle) return 0;
		double zeroK = 0.150;
		double stallAngle = 20.0;
		double stallK = 0.210;
		if (aoa >= minAngle && aoa <= 0) {
			double c = zeroK;
			double b = 2*(stallK-zeroK)/stallAngle;
			double a = (-c - b*minAngle) / (minAngle*minAngle);
			return a*aoa*aoa + b*aoa + c;
		}
		if (aoa > 0 && aoa <= stallAngle) {
			double a = -(stallK - zeroK) / (stallAngle*stallAngle);
			double b = -2*stallAngle*a;
			double c = zeroK;
			return a*aoa*aoa + b*aoa + c;
		}
		if (aoa > stallAngle && aoa <= maxAngle) {
			double a = -stallK / (maxAngle*maxAngle + stallAngle*stallAngle - 2*maxAngle*stallAngle);
			double b = -2*stallAngle*a;
			double c = stallK + a*stallAngle*stallAngle;
			return a*aoa*aoa + b*aoa + c;
		}
		return 0;
	}
	
	@Override
	public Vec3 getThrustForce(Quaternion q) {
		Vec3 direction = UtilAngles.getRollAxis(q);
		Vec3 thrustForce = direction.scale(getThrustMag());
		//System.out.println("thrust force = "+thrustForce);
		return thrustForce;
	}
	
	public float getPropellerRotation(float partialTicks) {
		return Mth.lerp(partialTicks, propellerRotOld, propellerRot);
	}

}
