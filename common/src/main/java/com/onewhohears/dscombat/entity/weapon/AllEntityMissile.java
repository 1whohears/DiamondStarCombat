package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.stats.PosMissileStats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AllEntityMissile<T extends PosMissileStats> extends EntityMissile<T> {

	public AllEntityMissile(EntityType<? extends AllEntityMissile<?>> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
	}
	
	@Override
	public WeaponType getWeaponType() {
		return WeaponType.ALL_MISSILE;
	}
	
	@Override
	public void tickGuide() {
		guideToPosition();
		// TODO ALL MISSILE GUIDANCE SYSTEM
	}

}
