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
	
	private final float propellerRate = 3.141f, gearAOABias = 10f;
	private float propellerRot = 0, propellerRotOld = 0, aoa = 0, liftK = 0, airFoilSpeedSqr = 0;
	private Vec3 liftDir = Vec3.ZERO, airFoilAxes = Vec3.ZERO;
	
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
		if (isOnGround()) {
			torqueX = torqueY = torqueZ = 0;
			EulerAngles angles = UtilAngles.toDegrees(q);
			//System.out.println("degrees "+angles);
			float dRoll = 5f;
			float dPitch = 5f;
			float roll, pitch;
			if (Math.abs(angles.roll) < dRoll) roll = (float) -angles.roll;
			else roll = -(float)Math.signum(angles.roll) * dRoll;
			if (Math.abs(angles.pitch) < dPitch) pitch = (float) -angles.pitch;
			else pitch = -(float)Math.signum(angles.pitch) * dPitch;
			q.mul(new Quaternion(Vector3f.XP, pitch, true));
			q.mul(new Quaternion(Vector3f.YN, inputYaw*getMaxDeltaYaw(), true));
			q.mul(new Quaternion(Vector3f.ZP, roll, true));
		} else {
			torqueX += inputPitch * 0.5f;
			torqueY += inputYaw * 0.5f;
			torqueZ += inputRoll * 0.5f;
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
		if (this.isOnGround()) {
			aoa = 0;
		} else {
			aoa = (float) UtilGeometry.angleBetweenDegrees(airFoilAxes, u);
			double uy = UtilGeometry.componentMagSqrDirByAxis(u.normalize(), liftDir);
			double vy = UtilGeometry.componentMagSqrDirByAxis(airFoilAxes, liftDir);
			if (vy < uy) aoa *= -1;
			//System.out.println("plane y = "+vy+" move y = "+uy);
		}
		if (isLandingGear()) aoa += gearAOABias;
		liftK = (float) getLiftK();
	}
	
	public Vec3 getLiftForce(Quaternion q) {
		//System.out.println("LIFT DIRECTION = "+liftDir);
		//System.out.println("aoa = "+aoa);
		//System.out.println("liftK = "+liftK);
		//System.out.println("airFoilSpeedSqr = "+airFoilSpeedSqr);
		Vec3 liftForce = liftDir.scale(getLiftMag());
		//System.out.println("lift force = "+liftForce);
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
		/*double minAngle = -10.0;
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
		}*/
		return Math.signum(aoa) * r;
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

	public float getAOA() {
		return aoa;
	}

}
