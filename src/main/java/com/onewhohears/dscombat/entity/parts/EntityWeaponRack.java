package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityWeaponRack extends EntityPart {
	
	public EntityWeaponRack(EntityType<?> type, Level level) {
		super(type, level);
	}
	
	public EntityWeaponRack(EntityType<?> type, Level level, String slotId, Vec3 pos) {
		super(type, level, slotId, pos);
	}
	
	public int getAmmoNum() {
		if (!(getVehicle() instanceof EntityAircraft plane)) return 0;
		WeaponData wd = plane.weaponSystem.get(getSlotId());
		if (wd == null) return 0;
		return wd.getCurrentAmmo();
	}

	@Override
	public boolean shouldRender() {
		return true;
	}

}
