package com.onewhohears.dscombat.data;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityAbstractWeapon;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MissileData extends BulletData {
	
	public static enum GuidanceType {
		OWNER_RADAR,
		IR,
		PITBULL
	}
	
	public static enum TargetType {
		GROUND,
		AIR,
		POS
	}
	
	private TargetType targetType;
	private GuidanceType guidanceType;
	private float maxRot;
	private double acceleration;
	private double fuseDist;
	
	public MissileData(String id, Vec3 launchPos, int maxAge, int maxAmmo, int fireRate, 
			float damage, double speed, boolean explosive, boolean destroyTerrain, 
			boolean causesFire, double explosiveDamage, float explosionRadius,
			TargetType tType, GuidanceType gType, float maxRot, 
			double acceleration, double fuseDist) {
		super(id, launchPos, maxAge, maxAmmo, fireRate, damage, speed, 
				explosive, destroyTerrain, causesFire, explosiveDamage, explosionRadius);
		this.targetType = tType;
		this.guidanceType = gType;
		this.maxRot = maxRot;
		this.acceleration = acceleration;
		this.fuseDist = fuseDist;
	}
	
	public MissileData(CompoundTag tag) {
		super(tag);
		targetType = TargetType.values()[tag.getInt("targetType")];
		guidanceType = GuidanceType.values()[tag.getInt("guidanceType")];
		maxRot = tag.getFloat("maxRot");
		acceleration = tag.getDouble("acceleration");
		fuseDist = tag.getDouble("fuseDist");
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putInt("targetType", targetType.ordinal());
		tag.putInt("guidanceType", guidanceType.ordinal());
		tag.putFloat("maxRot", maxRot);
		tag.putDouble("acceleration", acceleration);
		tag.putDouble("fuseDist", fuseDist);
		return tag;
	}
	
	public MissileData(FriendlyByteBuf buffer) {
		super(buffer);
		targetType = TargetType.values()[buffer.readInt()];
		guidanceType = GuidanceType.values()[buffer.readInt()];
		maxRot = buffer.readFloat();
		acceleration = buffer.readDouble();
		fuseDist = buffer.readDouble();
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeInt(targetType.ordinal());
		buffer.writeInt(guidanceType.ordinal());
		buffer.writeFloat(maxRot);
		buffer.writeDouble(acceleration);
		buffer.writeDouble(fuseDist);
	}
	
	@Override
	public EntityAbstractWeapon shoot(Level level, Entity vehicle, Entity owner, Vec3 direction, Quaternion vehicleQ) {
		if (!this.checkRecoil()) {
			this.setLaunchFail(null);
			return null;
		}
		if (!this.checkShoot(1)) {
			this.setLaunchFail("not enough ammo");
			return null;
		}
		System.out.println(this.getId()+" ammo "+this.getCurrentAmmo());
		EntityMissile rocket = new EntityMissile(level, owner, this);
		rocket.setPos(vehicle.position()
				.add(UtilAngles.rotateVector(this.getLaunchPos(), vehicleQ)));
		rocket.setDeltaMovement(direction.scale(this.getSpeed()).add(vehicle.getDeltaMovement()));
		rocket.parent = vehicle;
		if (targetType == TargetType.POS) {
			rocket.targetPos = Vec3.ZERO;
		} else if (guidanceType != GuidanceType.IR) {
			if (!(vehicle instanceof EntityAbstractAircraft plane)) {
				this.setLaunchFail("this rocket must be launched from an aircraft");
				return null;
			}
			RadarData radar = plane.getRadar();
			if (radar == null) {
				this.setLaunchFail("this rocket requires a radar on this aircraft");
				return null;
			}
			Entity target = radar.getSelectedTarget(level);
			if (target == null) {
				this.setLaunchFail("you have not selected an enemy to shoot");
				return null;
			}
			if (targetType == TargetType.AIR && target.isOnGround()) {
				this.setLaunchFail("this missile can only shoot AIRBORN targets");
				return null;
			} else if (targetType == TargetType.GROUND && !target.isOnGround()) {
				this.setLaunchFail("this missile can only shoot GROUNDED targets");
				return null;
			}
			if (guidanceType == GuidanceType.OWNER_RADAR) {
				rocket.targetPos = target.position();
				rocket.target = target;
				radar.addRocket(rocket);
			} else if (guidanceType == GuidanceType.PITBULL) {
				rocket.target = target;
			}
		}
		EulerAngles a = UtilAngles.toDegrees(vehicleQ);
		rocket.setXRot((float)a.pitch);
		rocket.setYRot((float)a.yaw);
		level.addFreshEntity(rocket);
		this.setLaunchSuccess(1);
		return rocket;
	}
	
	public void tickGuide(EntityMissile missile) {
		if (getTargetType() == TargetType.POS) {
			guideToTarget(missile, missile.targetPos);
		} else if (getGuidanceType() == GuidanceType.OWNER_RADAR) {
			guideToTarget(missile, missile.targetPos);
		} else if (getGuidanceType() == GuidanceType.PITBULL) {
			guideToTarget(missile, missile.target);
		} else if (getGuidanceType() == GuidanceType.IR) {
			irGuidance(missile);
		}
	}
	
	public void guideToTarget(EntityMissile missile, Entity target) {
		if (target == null) return;
		this.guideToTarget(missile, target.position());
	}
	
	public void guideToTarget(EntityMissile missile, Vec3 target) {
		if (target == null) {
			missile.discard();
			return;
		}
		double gx = target.x - missile.getX();
		double gy = target.y - missile.getY();
		double gz = target.z - missile.getZ();
		Vec3 gm = new Vec3(gx, gy, gz);
		float grx = UtilAngles.getPitch(gm);
		float gry = UtilAngles.getYaw(gm);
		float orx = missile.getXRot();
		float ory = missile.getYRot();
		float nrx = orx, nry = ory;
		if (Math.abs(nrx-orx) < this.getMaxRot()) {
			nrx = grx;
		} else if (grx > orx) {
			nrx += this.getMaxRot();
		} else if (grx < orx) {
			nrx -= this.getMaxRot();
		}
		if (Math.abs(nry-ory) < this.getMaxRot()) {
			nry = gry;
		} else {
			if (gry > 90 && ory < -90) {
				nry -= this.getMaxRot();
				if (nry < -180) nry += 360;
			} else if (ory > 90 && gry < -90) {
				nry += this.getMaxRot();
				if (nry > 180) nry -= 360;
			} else {
				if (gry > ory) {
					nry += this.getMaxRot();
				} else if (gry < ory) {
					nry -= this.getMaxRot();
				}
			}
		}
		Vec3 motion = missile.getDeltaMovement();
		Vec3 n = UtilAngles.rotationToVector(nry, nrx, this.getAcceleration());
		missile.setDeltaMovement(motion.add(n));
		missile.setXRot(nrx);
		missile.setYRot(nry);
	}
	
	public void irGuidance(EntityMissile missile) {
		
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.ROCKET;
	}
	
	public TargetType getTargetType() {
		return targetType;
	}
	
	public GuidanceType getGuidanceType() {
		return guidanceType;
	}

	public float getMaxRot() {
		return maxRot;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public double getFuseDist() {
		return fuseDist;
	}

}
