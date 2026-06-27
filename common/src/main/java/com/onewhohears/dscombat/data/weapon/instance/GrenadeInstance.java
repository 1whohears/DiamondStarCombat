package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.GrenadeStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityBomb;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import net.minecraft.world.phys.Vec3;

public class GrenadeInstance extends BombInstance<GrenadeStats> {
	
	public GrenadeInstance(GrenadeStats stats) {
		super(stats);
	}


	
}
