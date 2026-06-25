package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.PosMissileStats;
import com.onewhohears.dscombat.data.weapon.stats.TargetMode;
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
		missile.targetPos = params.targetParams.targetPos;
		return missile;
	}

	@Override
	public TargetMode fixTargetMode(TargetMode currentTargetMode, TargetMode preferedPosTargetMode) {
		if (!currentTargetMode.isPosition()) {
			return preferedPosTargetMode;
		}
		return currentTargetMode;
	}

}
