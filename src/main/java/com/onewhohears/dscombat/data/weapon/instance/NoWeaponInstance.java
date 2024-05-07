package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.weapon.stats.NoWeaponStats;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import net.minecraft.world.level.Level;

public class NoWeaponInstance extends WeaponInstance<NoWeaponStats> {
	
	public static NoWeaponInstance get() {
		NoWeaponInstance nwi = new NoWeaponInstance(NoWeaponStats.get());
		nwi.setInternal();
		return nwi;
	}
	
	public NoWeaponInstance(NoWeaponStats stats) {
		super(stats);
	}
	
	@Override
	public EntityWeapon<?> getEntity(Level level) {
		return null;
	}
	
	@Override
	public int getCurrentAmmo() {
		return 0;
	}

}
