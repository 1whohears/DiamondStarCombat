package com.onewhohears.dscombat.data.weapon;

import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityAbstractWeapon;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class MissileData extends BulletData {
	
	private float maxRot;
	private double acceleration;
	private double fuseDist;
	private float fov;
	private double bleed;
	// TODO missiles have limited fuel and they bleed energy when they turn so with no fuel they die if they get to slow
	
	/*public MissileData(RegistryObject<EntityType<?>> entityType, RegistryObject<SoundEvent> shootSound, List<Ingredient> ingredients,
			String id, Vec3 launchPos, int maxAge, int maxAmmo, int fireRate, boolean canShootOnGround,
			float damage, double speed, float innacuracy, boolean explosive, boolean destroyTerrain, 
			boolean causesFire, double explosiveDamage, float explosionRadius,
			TargetType tType, GuidanceType gType, float maxRot, 
			double acceleration, double fuseDist, float fov, float flareResistance) {
		super(entityType, shootSound, ingredients,
				id, launchPos, maxAge, maxAmmo, fireRate, canShootOnGround, damage, speed, innacuracy, 
				explosive, destroyTerrain, causesFire, explosiveDamage, explosionRadius);
		this.targetType = tType;
		this.guidanceType = gType;
		this.maxRot = maxRot;
		this.acceleration = acceleration;
		this.fuseDist = fuseDist;
		this.fov = fov;
		this.flareResistance = flareResistance;
	}*/
	
	public MissileData(CompoundTag tag) {
		super(tag);
		maxRot = tag.getFloat("maxRot");
		acceleration = tag.getDouble("acceleration");
		fuseDist = tag.getDouble("fuseDist");
		fov = tag.getFloat("fov");
		bleed = tag.getDouble("bleed");
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putFloat("maxRot", maxRot);
		tag.putDouble("acceleration", acceleration);
		tag.putDouble("fuseDist", fuseDist);
		tag.putFloat("fov", fov);
		tag.putDouble("bleed", bleed);
		return tag;
	}
	
	public MissileData(FriendlyByteBuf buffer) {
		super(buffer);
		maxRot = buffer.readFloat();
		acceleration = buffer.readDouble();
		fuseDist = buffer.readDouble();
		fov = buffer.readFloat();
		bleed = buffer.readDouble();
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeFloat(maxRot);
		buffer.writeDouble(acceleration);
		buffer.writeDouble(fuseDist);
		buffer.writeFloat(fov);
		buffer.writeDouble(bleed);
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
	
	public float getFov() {
		return fov;
	}
	
	public double getBleed() {
		return bleed;
	}
	
	@Override
	public EntityAbstractWeapon getShootEntity(Level level, Entity owner, Vec3 pos, Vec3 direction) {
		EntityMissile missile = (EntityMissile) super.getShootEntity(level, owner, pos, direction);
		if (missile == null) return null;
		missile.setDeltaMovement(direction.scale(1.0));
		return missile;
	}
	
	@Override
	public EntityAbstractWeapon getShootEntity(Level level, Entity owner, Vec3 direction, EntityAbstractAircraft vehicle) {
		EntityMissile missile = (EntityMissile) super.getShootEntity(level, owner, direction, vehicle);
		if (missile == null) return null;
		missile.setPos(missile.position().add(vehicle.getDeltaMovement()));
		missile.setDeltaMovement(vehicle.getDeltaMovement());
		return missile;
	}

}
