package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.TrackMissileData;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class TrackEntityMissile extends EntityMissile {
	
	protected TrackMissileData trackMissileData;
	
	public TrackEntityMissile(EntityType<? extends TrackEntityMissile> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
	}
	
	@Override
	protected void castWeaponData() {
		super.castWeaponData();
		trackMissileData = (TrackMissileData)weaponData;
	}

	@Override
	public void tickGuide() {
		if (!trackMissileData.isActiveTrack() && !level.isClientSide) notActiveCheckTarget();
		guideToTarget();
		if (!level.isClientSide && tickCount % 10 == 0 && target instanceof EntityVehicle plane) {
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
	
	@Override
	public WeaponData.WeaponType getWeaponType() {
		return WeaponData.WeaponType.TRACK_MISSILE;
	}

}
