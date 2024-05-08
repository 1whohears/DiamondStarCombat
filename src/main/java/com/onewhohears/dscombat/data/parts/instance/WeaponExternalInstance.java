package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.WeaponExternalStats;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.world.phys.Vec3;

public class WeaponExternalInstance<T extends WeaponExternalStats> extends WeaponPartInstance<T> {

	public WeaponExternalInstance(T stats) {
		super(stats);
	}
	
	@Override
	public void serverSetup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.serverSetup(craft, slotId, pos);
		WeaponInstance<?> data = craft.weaponSystem.get(weapon, slotId);
		if (data == null) return;
		data.setChangeLaunchPitch(getStats().getChangeLaunchPitch());
		if (!isEntitySetup(slotId, craft)) {
			EntityWeaponRack rack = (EntityWeaponRack) data.getStats().getRackEntityType().create(craft.level);
			setUpPartEntity(rack, craft, slotId, pos, 5);
			craft.level.addFreshEntity(rack);
		}
	}
	
	@Override
	public void serverRemove(String slotId) {
		super.serverRemove(slotId);
		removeEntity(slotId);
	}

}
