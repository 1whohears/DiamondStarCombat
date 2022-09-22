package com.onewhohears.dscombat.data;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.entity.weapon.EntityAbstractWeapon;
import com.onewhohears.dscombat.entity.weapon.EntityRocket;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RocketData extends BulletData {
	
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
	
	public RocketData(String id, Vec3 launchPos, int maxAge, int maxAmmo, int fireRate, 
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
	
	public RocketData(CompoundTag tag) {
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
	
	public RocketData(FriendlyByteBuf buffer) {
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
		if (!this.checkShoot(1)) return null;
		System.out.println(this.getId()+" ammo "+this.getCurrentAmmo());
		EntityRocket rocket = new EntityRocket(level, owner, this);
		rocket.setPos(vehicle.position()
				.add(UtilAngles.rotateVector(this.getLaunchPos(), vehicleQ)));
		rocket.setDeltaMovement(direction.scale(this.getSpeed()).add(vehicle.getDeltaMovement()));
		level.addFreshEntity(rocket);
		return rocket;
	}
	
	/**
	 * called by a missile entity in server side
	 * @param missile
	 */
	public void guideToTarget(EntityAbstractWeapon missile, Entity target) {
		if (target == null) return;
		this.guideToTarget(missile, target.position());
	}
	
	/**
	 * called by a missile entity in server side
	 * @param missile
	 */
	public void guideToTarget(EntityAbstractWeapon missile, Vec3 target) {
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
	
	/**
	 * called by a missile entity in server side
	 * @param missile
	 */
	public void irGuidance(EntityAbstractWeapon missile) {
		
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
	
	@Override
	public boolean mustSelectTarget() {
		return true;
	}

}
