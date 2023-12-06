package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.PosMissileData;
import com.onewhohears.dscombat.data.weapon.WeaponData;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class PositionMissile extends EntityMissile {
	
	public PositionMissile(EntityType<? extends PositionMissile> type, Level level) {
		super(type, level);
	}
	
	public PositionMissile(Level level, Entity owner, PosMissileData data) {
		super(level, owner, data);
	}
	
	@Override
	public void tickGuide() {
		guideToPosition();
	}
	
	@Override
	public WeaponData.WeaponType getWeaponType() {
		return WeaponData.WeaponType.POS_MISSILE;
	}

}
