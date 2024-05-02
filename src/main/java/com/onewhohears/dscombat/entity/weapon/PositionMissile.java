package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.stats.PosMissileStats;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class PositionMissile<T extends PosMissileStats> extends EntityMissile<T> {
	
	public PositionMissile(EntityType<? extends PositionMissile<?>> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
	}
	
	@Override
	public void tickGuide() {
		guideToPosition();
	}

}
