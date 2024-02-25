package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.PosMissileData;
import com.onewhohears.dscombat.data.weapon.WeaponData;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class PositionMissile extends EntityMissile {
	
	protected PosMissileData posMissileData;
	
	public PositionMissile(EntityType<? extends PositionMissile> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
	}
	
	@Override
	protected void castWeaponData() {
		super.castWeaponData();
		posMissileData = (PosMissileData)weaponData;
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
