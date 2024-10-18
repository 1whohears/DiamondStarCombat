package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.MissileStats;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.world.entity.Entity;
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

	protected void setTargetPosByLooker(WeaponShootParameters params, EntityMissile<?> missile) {
		Entity looker = params.owner;
		if (params.vehicle != null && params.vehicle.getGimbalForPilotCamera() != null) {
			looker = params.vehicle.getGimbalForPilotCamera();
			looker.setXRot(params.owner.getXRot());
			looker.setYRot(params.owner.getYRot());
		}
		missile.targetPos = UtilEntity.getLookingAtBlockPos(looker, 300);
	}

	protected void setTargetPosByCoordsCommand(WeaponShootParameters params, EntityMissile<?> missile) {
		if (params.vehicle != null) missile.targetPos = params.vehicle.weaponSystem.getTargetPos();
	}

}
