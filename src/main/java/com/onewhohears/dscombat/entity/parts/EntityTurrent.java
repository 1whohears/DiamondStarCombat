package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityTurrent extends EntitySeat {
	
	public final String weaponId;
	private WeaponData data;
	
	public EntityTurrent(EntityType<?> type, Level level, String weaponId) {
		super(type, level);
		this.weaponId = weaponId;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
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
		if (!level.isClientSide && getRootVehicle() instanceof EntityAircraft plane) {
			data = plane.weaponSystem.get(weaponId, getSlotId());
			if (data == null) {
				data = WeaponPresets.getNewById(weaponId);
				if (data != null) plane.weaponSystem.addWeapon(data, true);
			}
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		/*if (!level.isClientSide) {
			if (data != null) setAmmo(data.getCurrentAmmo());
			else setAmmo(0);
		}*/
	}
	
	@Override
	public boolean shouldRender() {
		return true;
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 25600;
	}
	
	public void setAmmo(int ammo) {
		if (data == null) return;
		data.setCurrentAmmo(ammo);
	}
	
	public int getAmmo() {
		if (data == null) return 0;
		return data.getCurrentAmmo();
	}

	@Override
	public void remove(RemovalReason reason) {
		if (!level.isClientSide && getRootVehicle() instanceof EntityAircraft plane) {
			plane.weaponSystem.removeWeapon(weaponId, getSlotId(), true);
		}
		super.remove(reason);
	}

}
