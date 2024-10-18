package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import com.onewhohears.dscombat.data.weapon.stats.PosMissileStats;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.PositionMissile;

public class PosMissileInstance<T extends PosMissileStats> extends MissileInstance<T> {

	public PosMissileInstance(T stats) {
		super(stats);
	}
	
	@Override
	public EntityWeapon<?> getShootEntity(WeaponShootParameters params) {
		PositionMissile<?> missile = (PositionMissile<?>) super.getShootEntity(params);
		if (missile == null) return null;
		if (params.vehicle == null) setTargetPosByLooker(params, missile);
		else {
			WeaponSystem.TargetMode mode = params.vehicle.weaponSystem.getTargetMode();
			if (mode == WeaponSystem.TargetMode.LOOK) setTargetPosByLooker(params, missile);
			else if (mode == WeaponSystem.TargetMode.COORDS) setTargetPosByCoordsCommand(params, missile);
		}
		return missile;
	}

}
