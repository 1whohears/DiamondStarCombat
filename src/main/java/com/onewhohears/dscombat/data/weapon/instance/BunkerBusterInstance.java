package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.BunkerBusterStats;
import com.onewhohears.dscombat.entity.weapon.EntityBunkerBuster;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

public class BunkerBusterInstance<T extends BunkerBusterStats> extends BombInstance<T> {

	public BunkerBusterInstance(T stats) {
		super(stats);
	}
	
	@Override
	public EntityWeapon<?> getShootEntity(WeaponShootParameters params) {
		EntityBunkerBuster<?> bomb = (EntityBunkerBuster<?>) super.getShootEntity(params);
		if (bomb == null) return null;
		return bomb;
	}

}
