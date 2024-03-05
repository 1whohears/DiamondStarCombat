package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityWeaponRack extends EntityPart {
	
	private String weaponModelId;
	
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
