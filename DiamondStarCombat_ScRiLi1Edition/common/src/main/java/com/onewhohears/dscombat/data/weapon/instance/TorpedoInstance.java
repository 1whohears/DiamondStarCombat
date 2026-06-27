package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.TorpedoStats;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.TorpedoMissile;

public class TorpedoInstance<T extends TorpedoStats> extends TrackMissileInstance<T> {

	public TorpedoInstance(T stats) {
		super(stats);
	}
	
	@Override
	public EntityWeapon<?> getShootEntity(WeaponShootParameters params) {
		TorpedoMissile<?> missile = (TorpedoMissile<?>) super.getShootEntity(params);
		if (missile == null) return null;
		return missile;
	}

}
