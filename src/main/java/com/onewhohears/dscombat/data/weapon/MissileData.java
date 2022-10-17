package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.RadarSystem;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityAbstractWeapon;
import com.onewhohears.dscombat.entity.weapon.EntityFlare;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
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
	private float fov;
	
	public MissileData(String id, Vec3 launchPos, int maxAge, int maxAmmo, int fireRate, 
			float damage, double speed, float innacuracy, boolean explosive, boolean destroyTerrain, 
			boolean causesFire, double explosiveDamage, float explosionRadius,
			TargetType tType, GuidanceType gType, float maxRot, 
			double acceleration, double fuseDist, float fov) {
		super(id, launchPos, maxAge, maxAmmo, fireRate, damage, speed, innacuracy, 
				explosive, destroyTerrain, causesFire, explosiveDamage, explosionRadius);
		this.targetType = tType;
		this.guidanceType = gType;
		this.maxRot = maxRot;
		this.acceleration = acceleration;
		this.fuseDist = fuseDist;
		this.fov = fov;
	}
	
	public MissileData(CompoundTag tag) {
		super(tag);
		targetType = TargetType.values()[tag.getInt("targetType")];
		guidanceType = GuidanceType.values()[tag.getInt("guidanceType")];
		maxRot = tag.getFloat("maxRot");
		acceleration = tag.getDouble("acceleration");
		fuseDist = tag.getDouble("fuseDist");
		fov = tag.getFloat("fov");
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putInt("targetType", targetType.ordinal());
		tag.putInt("guidanceType", guidanceType.ordinal());
		tag.putFloat("maxRot", maxRot);
		tag.putDouble("acceleration", acceleration);
		tag.putDouble("fuseDist", fuseDist);
		tag.putFloat("fov", fov);
		return tag;
	}
	
	public MissileData(FriendlyByteBuf buffer) {
		super(buffer);
	}
	
	@Override
	public void read(FriendlyByteBuf buffer) {
		super.read(buffer);
		targetType = TargetType.values()[buffer.readInt()];
		guidanceType = GuidanceType.values()[buffer.readInt()];
		maxRot = buffer.readFloat();
		acceleration = buffer.readDouble();
		fuseDist = buffer.readDouble();
		fov = buffer.readFloat();
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeInt(targetType.ordinal());
		buffer.writeInt(guidanceType.ordinal());
		buffer.writeFloat(maxRot);
		buffer.writeDouble(acceleration);
		buffer.writeDouble(fuseDist);
		buffer.writeFloat(fov);
	}
	
	@Override
	public EntityAbstractWeapon shoot(Level level, EntityAbstractAircraft vehicle, Entity owner, Vec3 direction, Quaternion vehicleQ) {
		if (!this.checkRecoil()) {
			this.setLaunchFail(null);
			return null;
		}
		if (!this.checkAmmo(1, owner)) {
			this.setLaunchFail("not enough ammo");
			return null;
		}
		System.out.println(this.getId()+" ammo "+this.getCurrentAmmo());
		EntityMissile rocket = new EntityMissile(level, owner, this);
		rocket.setDeltaMovement(vehicle.getDeltaMovement());
		rocket.setPos(vehicle.position()
				.add(UtilAngles.rotateVector(this.getLaunchPos(), vehicleQ))
				.add(rocket.getDeltaMovement()));
		rocket.parent = vehicle;
		if (targetType == TargetType.POS) {
			rocket.targetPos = Vec3.ZERO.add(0, -60,0);
		} else if (guidanceType != GuidanceType.IR) {
			/*if (!(vehicle instanceof EntityAbstractAircraft plane)) {
				this.setLaunchFail("this rocket must be launched from an aircraft");
				return null;
			}*/
			RadarSystem radar = vehicle.radarSystem;
			System.out.println(radar);
			if (!radar.hasRadar()) {
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
		rocket.setXRot(UtilAngles.getPitch(direction));
		rocket.setYRot(UtilAngles.getYaw(direction));
		level.addFreshEntity(rocket);
		this.setLaunchSuccess(1, owner);
		updateClientAmmo(vehicle);
		return rocket;
	}
	
	public void tickGuide(EntityMissile missile) {
		if (missile.tickCount < 10) return;
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
		if (target == null) {
			//missile.discard();
			missile.targetPos = null;
			return;
		}
		if (target.isRemoved()) {
			//missile.discard();
			missile.targetPos = null;
			return;
		}
		if (missile.tickCount % 4 == 0) {
			if (!checkTargetRange(missile, target, 10000)) {
				//missile.discard();
				missile.targetPos = null;
				return;
			}
			if (!checkCanSee(missile, target)) {
				//missile.discard();
				//System.out.println("can't see target");
				missile.targetPos = null;
				return;
			}
		}
		//System.out.println("==========");
		Vec3 tVel = target.getDeltaMovement();
		if (target.isOnGround()) tVel = tVel.add(0, -tVel.y, 0); // some entities have -0.07 y vel when on ground
		Vec3 pos = UtilGeometry.interceptPos( 
				missile.position(), missile.getDeltaMovement(), 
				target.position(), tVel);
		//System.out.println("TARGET POS = "+target.position());
		//System.out.println("INTER  POS = "+pos);
		missile.targetPos = pos;
		this.guideToTarget(missile, pos);
		if (getGuidanceType() == GuidanceType.PITBULL && target instanceof EntityAbstractAircraft plane) {
			plane.trackedByMissile();
		}
	}
	
	public void guideToTarget(EntityMissile missile, Vec3 target) {
		if (target == null) {
			//missile.discard();
			return;
		}
		Vec3 gm = target.subtract(missile.position());
		float grx = UtilAngles.getPitch(gm);
		float gry = UtilAngles.getYaw(gm);
		float orx = missile.getXRot();
		float ory = missile.getYRot();
		float nrx = orx, nry = ory;
		if (Math.abs(grx-orx) < this.getMaxRot()) {
			nrx = grx;
		} else if (grx > orx) {
			nrx += this.getMaxRot();
		} else if (grx < orx) {
			nrx -= this.getMaxRot();
		}
		if (Math.abs(gry-ory) < this.getMaxRot()) {
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
		/*Vec3 motion = missile.getDeltaMovement();
		Vec3 n = UtilAngles.rotationToVector(nry, nrx, this.getAcceleration());
		missile.setDeltaMovement(motion.add(n));*/
		missile.setXRot(nrx);
		missile.setYRot(nry);
	}
	
	public void irGuidance(EntityMissile missile) {
		if (missile.tickCount % 5 == 0) findIrTarget(missile);
		if (missile.target != null) this.guideToTarget(missile, missile.target);
	}
	
	private List<IrTarget> targets = new ArrayList<IrTarget>();
	
	public void findIrTarget(EntityMissile missile) {
		Level level = missile.level;
		targets.clear();
		// planes
		List<EntityAbstractAircraft> planes = level.getEntitiesOfClass(
				EntityAbstractAircraft.class, getIrBoundingBox(missile));
		for (int i = 0; i < planes.size(); ++i) {
			if (planes.get(i).isVehicleOf(missile.getOwner())) continue;
			if (!basicCheck(missile, planes.get(i), false)) continue;
			float distSqr = (float)missile.distanceToSqr(planes.get(i));
			targets.add(new IrTarget(planes.get(i), planes.get(i).getHeat() / distSqr));
		}
		// players
		List<Player> players = level.getEntitiesOfClass(
				Player.class, getIrBoundingBox(missile));
		for (int i = 0; i < players.size(); ++i) {
			if (players.get(i).getRootVehicle() instanceof EntityAbstractAircraft) continue;
			if (!basicCheck(missile, players.get(i), true)) continue;
			float distSqr = (float)missile.distanceToSqr(players.get(i));
			targets.add(new IrTarget(players.get(i), 1f / distSqr));
		}
		// mobs
		List<Mob> mobs = level.getEntitiesOfClass(
				Mob.class, getIrBoundingBox(missile));
		for (int i = 0; i < mobs.size(); ++i) {
			if (mobs.get(i).getRootVehicle() instanceof EntityAbstractAircraft) continue;
			if (!basicCheck(missile, mobs.get(i), true)) continue;
			float distSqr = (float)missile.distanceToSqr(mobs.get(i));
			targets.add(new IrTarget(mobs.get(i), getMobHeat(mobs.get(i)) / distSqr));
		}
		// missiles
		List<EntityMissile> missiles = level.getEntitiesOfClass(
				EntityMissile.class, getIrBoundingBox(missile));
		for (int i = 0; i < missiles.size(); ++i) {
			if (missile.equals(missiles.get(i))) continue;
 			if (!basicCheck(missile, missiles.get(i), false)) continue;
			float distSqr = (float)missile.distanceToSqr(missiles.get(i));
			targets.add(new IrTarget(missiles.get(i), missiles.get(i).getHeat() / distSqr));
		}	
		// flares
		List<EntityFlare> flares = level.getEntitiesOfClass(
				EntityFlare.class, getIrBoundingBox(missile));
		for (int i = 0; i < flares.size(); ++i) {
			if (!basicCheck(missile, flares.get(i), true)) continue;
			float distSqr = (float)missile.distanceToSqr(flares.get(i));
			targets.add(new IrTarget(flares.get(i), flares.get(i).getHeat() / distSqr));
		}
		// fire arrows
		List<AbstractArrow> arrows = level.getEntitiesOfClass(
				AbstractArrow.class, getIrBoundingBox(missile));
		for (int i = 0; i < arrows.size(); ++i) {
			if (!arrows.get(i).isOnFire()) continue;
			if (!basicCheck(missile, arrows.get(i), false)) continue;
			float distSqr = (float)missile.distanceToSqr(arrows.get(i));
			targets.add(new IrTarget(arrows.get(i), 2f / distSqr));
		}
		// fire arrows
		List<FireworkRocketEntity> foreworks = level.getEntitiesOfClass(
				FireworkRocketEntity.class, getIrBoundingBox(missile));
		for (int i = 0; i < foreworks.size(); ++i) {
			if (!basicCheck(missile, foreworks.get(i), false)) continue;
			float distSqr = (float)missile.distanceToSqr(foreworks.get(i));
			targets.add(new IrTarget(foreworks.get(i), 2f / distSqr));
		}
		if (targets.size() == 0) {
			missile.target = null;
			missile.targetPos = null;
			//System.out.println("NO TARGET");
			return;
		}
		IrTarget max = targets.get(0);
		for (int i = 1; i < targets.size(); ++i) {
			if (targets.get(i).heat > max.heat) max = targets.get(i);
		}
		missile.target = max.entity;
		missile.targetPos = max.entity.position();
		//System.out.println("TARGET FOUND "+missile.target);
	}
	
	private boolean basicCheck(EntityMissile missile, Entity ping, boolean checkGround) {
		//System.out.println("target? "+ping);
		if (checkGround && ping.isOnGround()) {
			//System.out.println("on ground");
			return false;
		}
		if (missile.getOwner() != null && ping.equals(missile.getOwner())) {
			//System.out.println("is owner");
			return false;
		}
		if (!checkTargetRange(missile, ping, irRange)) {
			//System.out.println("not in cone");
			return false;
		}
		if (!checkCanSee(missile, ping)) {
			//System.out.println("can't see");
			return false;
		}
		//System.out.println("POSSIBLE");
		return true;
	}
	
	private static float getMobHeat(Mob mob) {
		if (mob.getType().equals(EntityType.BLAZE)) return 10f;
		if (mob.getType().equals(EntityType.MAGMA_CUBE)) return 8f;
		return 1f;
	}
	
	private static final double irRange = 300d;
	
	private boolean checkTargetRange(Entity missile, Entity target, double range) {
		if (fov == -1) return missile.distanceTo(target) <= range;
		return UtilGeometry.isPointInsideCone(
				target.position(), 
				missile.position(),
				missile.getLookAngle(), 
				fov, range);
	}
	
	private boolean checkCanSee(Entity radar, Entity target) {
		return UtilEntity.canEntitySeeEntity(radar, target);
	}
	
	private AABB getIrBoundingBox(Entity missile) {
		double x = missile.getX();
		double y = missile.getY();
		double z = missile.getZ();
		double w = irRange;
		return new AABB(x+w, y+w, z+w, x-w, y-w, z-w);
	}
	
	public void clientGuidance(EntityMissile missile) {
		guideToTarget(missile, missile.targetPos);
	}
	
	public static class IrTarget {
		
		public final Entity entity;
		public final float heat;
		
		public IrTarget(Entity entity, float heat) {
			this.entity = entity;
			this.heat = heat;
		}
		
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
