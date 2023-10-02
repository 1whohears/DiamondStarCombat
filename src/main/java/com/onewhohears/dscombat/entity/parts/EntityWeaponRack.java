package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.world.phys.Vec3;

public class EntityWeaponRack extends EntityVehiclePart {
	
	public EntityWeaponRack(EntityAircraft parent, String slotId, Vec3 pos, float z_rot, float width, float height) {
		super(parent, slotId, pos, z_rot, width, height);
	}
	
	public int getAmmoNum() {
		WeaponData wd = getParent().weaponSystem.get(getSlotId());
		if (wd == null) return 0;
		return wd.getCurrentAmmo();
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
