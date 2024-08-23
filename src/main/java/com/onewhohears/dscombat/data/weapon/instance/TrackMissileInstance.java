package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.TrackMissileStats;
import com.onewhohears.dscombat.data.weapon.stats.TrackMissileStats.TargetType;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.TrackEntityMissile;
import com.onewhohears.dscombat.util.UtilVehicleEntity;

import net.minecraft.world.entity.Entity;

public class TrackMissileInstance<T extends TrackMissileStats> extends MissileInstance<T> {

	public TrackMissileInstance(T stats) {
		super(stats);
	}
	
	@Override
	public boolean couldRadarWeaponTargetEntity(Entity entity, Entity radar) {
		if (!super.couldRadarWeaponTargetEntity(entity, radar)) return false;
		boolean groundWater = UtilVehicleEntity.isOnGroundOrWater(entity);
		TargetType targetType = getStats().getTargetType();
		if (targetType == TargetType.AIR && groundWater) return false;
		else if (targetType == TargetType.GROUND && !groundWater) return false;
		else if (targetType == TargetType.WATER && !entity.isInWater()) return false;
		return true;
	}
	
	@Override
	public EntityWeapon<?> getShootEntity(WeaponShootParameters params) {
		TrackEntityMissile<?> missile = (TrackEntityMissile<?>) super.getShootEntity(params);
		if (missile == null) return null;
		if (params.vehicle == null) return missile;
		RadarSystem radar = params.vehicle.radarSystem;
		if (!radar.hasRadar()) {
			setLaunchFail("error.dscombat.no_radar");
			return null;
		}
		Entity target = radar.getSelectedTarget();
		if (target == null) {
			setLaunchFail("error.dscombat.no_target_selected");
			return null;
		}
		boolean groundWater = UtilVehicleEntity.isOnGroundOrWater(target);
		TargetType targetType = getStats().getTargetType();
		if (targetType == TargetType.AIR && groundWater) {
			setLaunchFail("error.dscombat.air_target_only");
			return null;
		} else if (targetType == TargetType.GROUND && !groundWater) {
			setLaunchFail("error.dscombat.ground_target_only");
			return null;
		} else if (targetType == TargetType.WATER && !target.isInWater()) {
			setLaunchFail("error.dscombat.water_target_only");
			return null;
		}
		missile.target = target;
		return missile;
	}

}
