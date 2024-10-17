package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.stats.TrackMissileStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class TrackEntityMissile<T extends TrackMissileStats> extends EntityMissile<T> {
	
	public TrackEntityMissile(EntityType<? extends TrackEntityMissile<?>> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
	}
	
	@Override
	public WeaponType getWeaponType() {
		return WeaponType.TRACK_MISSILE;
	}

	@Override
	public void tickGuide() {
		if (!getWeaponStats().isActiveTrack() && !level().isClientSide) notActiveCheckTarget();
		guideToTarget();
		if (!level().isClientSide && tickCount % 10 == 0 && target instanceof EntityVehicle plane) {
			plane.trackedByMissile(this);
		}
	}
	
	public void notActiveCheckTarget() {
		if (target == null || tickCount % 10 != 0) return;
		Entity owner = getOwner();
		if (owner == null) {
			target = null;
			return;
		}
		if (!(owner.getRootVehicle() instanceof EntityVehicle plane)) {
			target = null;
			return;
		}
		if (!plane.radarSystem.hasTarget(target.getId())) {
			target = null;
			return;
		}
	}

}
