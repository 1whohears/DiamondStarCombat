package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.TurretData;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityTurret extends EntitySeat {
	
	public static final EntityDataAccessor<Integer> AMMO = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.INT);
	
	/**
	 * only used on server side
	 */
	private String weaponId;
	/**
	 * only used on server side
	 */
	private WeaponData data;
	
	public EntityTurret(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(AMMO, 0);
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		data = UtilParse.parseWeaponFromCompound(tag.getCompound("weapondata"));
		if (data == null) data = WeaponPresets.getNewById(weaponId);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (data != null) tag.put("weapondata", data.write());
	}
	
	public void init() {
		super.init();
		/*if (!level.isClientSide && getRootVehicle() instanceof EntityAircraft plane) {
			data = plane.weaponSystem.getTurret(getSlotId());
			if (data == null) {
				data = WeaponPresets.getNewById(weaponId);
				if (data != null) plane.weaponSystem.addTurret(getSlotId(), data, true);
			}
		}*/
		if (data != null) data.setCurrentAmmo(getAmmo());
	}
	
	@Override
	public void tick() {
		super.tick();
		/*if (!level.isClientSide && tickCount % 10 == 0 && getRootVehicle() instanceof EntityAircraft plane) {
			PartSlot slot = plane.partsManager.getSlot(getSlotId());
			if (slot != null && slot.filled() && slot.getPartData().getType() == PartType.TURRENT) { 
				TurretData td = (TurretData) slot.getPartData();
				td.setAmmo(getAmmo());
				td.setMax(getMax());
			}
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
		entityData.set(AMMO, ammo);
	}
	
	public int getAmmo() {
		return entityData.get(AMMO);
	}
	
	public void setWeaponId(String wid) {
		weaponId = wid;
	}
	
	public void shoot(Entity shooter) {
		// TODO data is null when shooting
		System.out.println(shooter+" shooting "+this+" with "+data);
		if (level.isClientSide || data == null) return;
		data.shoot(level, shooter, shooter.getLookAngle(), position(), null);
		if (data.isFailedLaunch()) {
			if (shooter instanceof ServerPlayer player) {
				player.displayClientMessage(Component.translatable(data.getFailedLaunchReason()), true);
			}
		} else {
			setAmmo(data.getCurrentAmmo());
			if (getRootVehicle() instanceof EntityAircraft plane) {
				PartSlot slot = plane.partsManager.getSlot(getSlotId());
				if (slot != null && slot.filled() && slot.getPartData().getType() == PartType.TURRENT) { 
					TurretData td = (TurretData) slot.getPartData();
					td.setAmmo(data.getCurrentAmmo());
				}
			}
		}
	}

}
