package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.PosMissileStats;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.PositionMissile;
import com.onewhohears.dscombat.util.UtilEntity;

import net.minecraft.world.entity.Entity;

public class PosMissileInstance<T extends PosMissileStats> extends MissileInstance<T> {

	public PosMissileInstance(T stats) {
		super(stats);
	}
	
	@Override
	public EntityWeapon<?> getShootEntity(WeaponShootParameters params) {
		PositionMissile<?> missile = (PositionMissile<?>) super.getShootEntity(params);
		if (missile == null) return null;
		Entity looker = params.owner;
		if (params.vehicle != null && params.vehicle.getGimbalForPilotCamera() != null) {
			looker = params.vehicle.getGimbalForPilotCamera();
			looker.setXRot(params.owner.getXRot());
			looker.setYRot(params.owner.getYRot());
		}
		missile.targetPos = UtilEntity.getLookingAtBlockPos(looker, 300);
		return missile;
	}

}
