package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityTurrent extends EntitySeat {
	
	public static final EntityDataAccessor<Integer> AMMO = SynchedEntityData.defineId(EntityTurrent.class, EntityDataSerializers.INT);
	
	public final String weaponId;
	
	private WeaponData weapon;
	
	public EntityTurrent(EntityType<?> type, Level level, String weaponId) {
		super(type, level);
		this.weaponId = weaponId;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(AMMO, 0);
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
	}
	
	public void init() {
		super.init();
		if (!level.isClientSide) {
			getWeapon().setCurrentAmmo(getAmmo());
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!level.isClientSide) {
			setAmmo(getWeapon().getCurrentAmmo());
		}
	}
	
	@Override
	public boolean shouldRender() {
		return true;
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 25600;
	}
	
	public WeaponData getWeapon() {
		if (weapon == null) {
			weapon = WeaponPresets.getNewById(weaponId);
			if (weapon == null) weapon = WeaponPresets.getById("20mm");
		}
		return weapon;
	}
	
	public void setAmmo(int ammo) {
		entityData.set(AMMO, ammo);
	}
	
	public int getAmmo() {
		return entityData.get(AMMO);
	}

}
