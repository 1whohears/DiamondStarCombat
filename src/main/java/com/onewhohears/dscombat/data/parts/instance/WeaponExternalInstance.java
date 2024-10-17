package com.onewhohears.dscombat.data.parts.instance;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.stats.WeaponExternalStats;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.parts.EntityPart;
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
	}

	@Override @Nullable
	protected EntityPart createEntity(EntityVehicle vehicle, String slotId) {
		WeaponInstance<?> data = vehicle.weaponSystem.get(weapon, slotId);
		if (data == null) return null;
		return (EntityPart) data.getStats().getRackEntityType().create(vehicle.level());
	}
	
}
