package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.AntiRadarMissileStats;
import com.onewhohears.dscombat.entity.weapon.AntiRadarMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

public class AntiRadarMissileInstance<T extends AntiRadarMissileStats> extends MissileInstance<T> {

	public AntiRadarMissileInstance(T stats) {
		super(stats);
	}
	
	@Override
	public EntityWeapon<?> getShootEntity(WeaponShootParameters params) {
		AntiRadarMissile<?> missile = (AntiRadarMissile<?>) super.getShootEntity(params);
		if (missile == null) return null;
		return missile;
	}

}
