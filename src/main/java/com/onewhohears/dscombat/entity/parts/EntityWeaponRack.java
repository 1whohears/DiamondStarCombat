package com.onewhohears.dscombat.entity.parts;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityWeaponRack extends EntityPart {
	
	private String weaponModelId;
	private EntityType<?> weaponEntityType;
	
	public EntityWeaponRack(EntityType<?> type, Level level) {
		super(type, level);
	}
	
	public int getAmmoNum() {
		if (!(getVehicle() instanceof EntityVehicle plane)) return 0;
		WeaponData wd = plane.weaponSystem.get(getSlotId());
		if (wd == null) return 0;
		return wd.getCurrentAmmo();
	}
	
	public String getWeaponModelId() {
		if (weaponModelId == null) {
			weaponModelId = "";
			if (!(getVehicle() instanceof EntityVehicle plane)) return weaponModelId;
			WeaponData wd = plane.weaponSystem.get(getSlotId());
			if (wd == null) return weaponModelId;
			weaponModelId = wd.getModelId();
		}
		return weaponModelId;
	}
	
	public EntityType<?> getWeaponEntityType() {
		if (weaponEntityType == null) {
			weaponEntityType = ModEntities.BULLET.get();
			if (!(getVehicle() instanceof EntityVehicle plane)) return weaponEntityType;
			WeaponData wd = plane.weaponSystem.get(getSlotId());
			if (wd == null) return weaponEntityType;
			weaponEntityType = wd.getEntityType();
		}
		return weaponEntityType;
	}

	@Override
	public boolean shouldRender() {
		return true;
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 25600;
	}
	
	@Override
	public PartType getPartType() {
		return PartType.WEAPON_RACK;
	}
	
	@Override
	public boolean canGetHurt() {
		return false;
	}

}
