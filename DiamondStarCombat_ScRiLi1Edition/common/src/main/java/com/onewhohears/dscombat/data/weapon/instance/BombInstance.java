package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.BombStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityBomb;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import net.minecraft.world.phys.Vec3;

public class BombInstance<T extends BombStats> extends BulletInstance<T> {
	
	public BombInstance(T stats) {
		super(stats);
	}

	@Override
	public EntityWeapon<?> getShootEntity(WeaponShootParameters params) {
		EntityBomb<?> bomb = (EntityBomb<?>) super.getShootEntity(params);
		if (bomb == null) return null;
		if (params.vehicle != null) {
			bomb.setDeltaMovement(params.vehicle.getDeltaMovement());
		}
		return bomb;
	}
	
	@Override
	protected Vec3 getStartMove(EntityVehicle vehicle) {
		return vehicle.getDeltaMovement();
	}
	
}
