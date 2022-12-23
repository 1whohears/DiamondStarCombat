package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.TurretData;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityTurret extends EntitySeat {
	
	public final String weaponId;
	/**
	 * only used on server side
	 */
	private WeaponData data;
	
	public EntityTurret(EntityType<?> type, Level level, String weaponId) {
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
			data = plane.weaponSystem.getTurret(getSlotId());
			if (data == null) {
				data = WeaponPresets.getNewById(weaponId);
				if (data != null) plane.weaponSystem.addTurret(getSlotId(), data, true);
			}
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!level.isClientSide && tickCount % 10 == 0 && getRootVehicle() instanceof EntityAircraft plane) {
			PartSlot slot = plane.partsManager.getSlot(getSlotId());
			if (slot != null && slot.filled() && slot.getPartData().getType() == PartType.TURRENT) { 
				TurretData td = (TurretData) slot.getPartData();
				td.setAmmo(getAmmo());
				td.setMax(getMax());
			}
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
	
	public void setAmmo(int ammo) {
		if (data == null) return;
		data.setCurrentAmmo(ammo);
	}
	
	public int getAmmo() {
		if (data == null) return 0;
		return data.getCurrentAmmo();
	}
	
	public int getMax() {
		if (data == null) return 0;
		return data.getMaxAmmo();
	}

	@Override
	public void remove(RemovalReason reason) {
		if (!level.isClientSide && getRootVehicle() instanceof EntityAircraft plane) {
			plane.weaponSystem.removeTurret(getSlotId(), true);
		}
		super.remove(reason);
	}
	
	public void shoot() {
		if (level.isClientSide) return;
		// TODO shoot the turret
	}

}
