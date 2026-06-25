package com.onewhohears.dscombat.entity.weapon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Entity для отображения 3D вспышки дульного огня
 * Живет очень короткое время (5-10 тиков) и затухает
 */
public class EntityMuzzleFlash extends Entity {
	
	private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(EntityMuzzleFlash.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Integer> MAX_LIFETIME = SynchedEntityData.defineId(EntityMuzzleFlash.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> DIR_X = SynchedEntityData.defineId(EntityMuzzleFlash.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DIR_Y = SynchedEntityData.defineId(EntityMuzzleFlash.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DIR_Z = SynchedEntityData.defineId(EntityMuzzleFlash.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> RANDOM_YAW = SynchedEntityData.defineId(EntityMuzzleFlash.class, EntityDataSerializers.FLOAT);
	
	private int lifetime = 0;
	
	public EntityMuzzleFlash(EntityType<?> type, Level level) {
		super(type, level);
		this.noPhysics = true;
		this.noCulling = true;
	}
	
	@Override
	protected void defineSynchedData() {
		entityData.define(SCALE, 1.0f);
		entityData.define(MAX_LIFETIME, 5);
		entityData.define(DIR_X, 0.0f);
		entityData.define(DIR_Y, 0.0f);
		entityData.define(DIR_Z, 0.0f);
		// Случайный поворот на ±45° по оси Y для живого эффекта
		float randomYaw = (random.nextFloat() - 0.5f) * 90f; // От -45° до +45°
		entityData.define(RANDOM_YAW, randomYaw);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		lifetime++;
		
		// Удаляем entity после истечения времени жизни
		if (lifetime >= getMaxLifetime()) {
			discard();
		}
	}
	
	public void setShootDirection(Vec3 direction) {
		Vec3 normalized = direction.normalize();
		entityData.set(DIR_X, (float)normalized.x);
		entityData.set(DIR_Y, (float)normalized.y);
		entityData.set(DIR_Z, (float)normalized.z);
	}
	
	public Vec3 getShootDirection() {
		float x = entityData.get(DIR_X);
		float y = entityData.get(DIR_Y);
		float z = entityData.get(DIR_Z);
		return new Vec3(x, y, z);
	}
	
	public void setScale(float scale) {
		entityData.set(SCALE, scale);
	}
	
	public float getScale() {
		return entityData.get(SCALE);
	}
	
	public void setMaxLifetime(int ticks) {
		entityData.set(MAX_LIFETIME, ticks);
	}
	
	public int getMaxLifetime() {
		return entityData.get(MAX_LIFETIME);
	}
	
	public int getLifetime() {
		return lifetime;
	}
	
	public float getRandomYaw() {
		return entityData.get(RANDOM_YAW);
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		lifetime = tag.getInt("lifetime");
		// Direction теперь в synced data, но читаем из NBT для совместимости
		if (tag.contains("dirX")) {
			Vec3 dir = new Vec3(
				tag.getDouble("dirX"),
				tag.getDouble("dirY"),
				tag.getDouble("dirZ")
			);
			setShootDirection(dir);
		}
		if (tag.contains("randomYaw")) {
			entityData.set(RANDOM_YAW, tag.getFloat("randomYaw"));
		}
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putInt("lifetime", lifetime);
		Vec3 dir = getShootDirection();
		tag.putDouble("dirX", dir.x);
		tag.putDouble("dirY", dir.y);
		tag.putDouble("dirZ", dir.z);
		tag.putFloat("randomYaw", getRandomYaw());
	}
	
	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return dev.architectury.networking.NetworkManager.createAddEntityPacket(this);
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		// Рендерим на большом расстоянии
		return distance < 16384.0; // 128 блоков
	}
}
