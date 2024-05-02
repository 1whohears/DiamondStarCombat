package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.MissileStats;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import net.minecraft.world.phys.Vec3;

public abstract class MissileInstance<T extends MissileStats> extends BulletInstance<T> {

	public MissileInstance(T stats) {
		super(stats);
	}
	
	@Override
	public EntityWeapon<?> getShootEntity(WeaponShootParameters params) {
		EntityMissile<?> missile = (EntityMissile<?>) super.getShootEntity(params);
		if (missile == null) return null;
		if (params.vehicle != null) {
			missile.setPos(missile.position().add(params.vehicle.getDeltaMovement()));
			Vec3 move = params.vehicle.getDeltaMovement();
			if (params.isTurret) move = move.add(params.direction.scale(0.5));
			missile.setDeltaMovement(move);
		} else {
			missile.setDeltaMovement(params.direction.scale(0.5));
		}
		return missile;
	}

}
