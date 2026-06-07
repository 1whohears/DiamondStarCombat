package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.radar.RadarTarget;
import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.AllMissileStats;
import com.onewhohears.dscombat.data.weapon.stats.RadarTargetType;
import com.onewhohears.dscombat.data.weapon.stats.TargetMode;
import com.onewhohears.dscombat.entity.weapon.AllEntityMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class AllMissileInstance<T extends AllMissileStats> extends MissileInstance<T> {

	public AllMissileInstance(T stats) {
		super(stats);
	}

	@Override
	public boolean couldRadarWeaponTargetEntity(Entity entity, Entity radar) {
		if (!super.couldRadarWeaponTargetEntity(entity, radar)) return false;
		if (!getStats().isCanRadarGuide()) return false;
		boolean groundWater = UtilVehicleEntity.isOnGroundOrWater(entity);
		RadarTargetType targetType = getStats().getRadarTargetType();
		if (targetType == RadarTargetType.AIR && groundWater) return false;
		else if (targetType == RadarTargetType.GROUND && !groundWater) return false;
		else if (targetType == RadarTargetType.WATER && !entity.isInWater()) return false;
		return true;
	}

	@Override
	public EntityWeapon<?> getShootEntity(WeaponShootParameters params) {
		AllEntityMissile<?> missile = (AllEntityMissile<?>) super.getShootEntity(params);
		if (missile == null) return null;
		missile.targetMode = params.targetParams.targetMode;
        missile.selectedMarkerId = params.targetParams.selectedMarkerId;
		switch (params.targetParams.targetMode) {
            case LOOK, COORDS, MARKER -> {
				return shootPosGuided(params, missile);
            }
            case RADAR -> {
				return shootRadarGuided(params, missile);
            }
            case OPTICAL -> {
				return shootOpticalGuided(params, missile);
            }
			case NONE -> {
				return missile;
			}
        }
		return null;
	}

	protected AllEntityMissile<?> shootPosGuided(@NotNull WeaponShootParameters params,
												 @NotNull AllEntityMissile<?> missile) {
		if (!getStats().isCanPositionGuide()) {
			setLaunchFail("error.dscombat.not_pos_missile");
			return null;
		}
		missile.targetPos = params.targetParams.targetPos;
		return missile;
	}

	protected AllEntityMissile<?> shootOpticalGuided(@NotNull WeaponShootParameters params,
													 @NotNull AllEntityMissile<?> missile) {
		if (!getStats().isCanOpticalGuide()) {
			setLaunchFail("error.dscombat.not_optical_missile");
			return null;
		}
        if (params.vehicle == null) return missile;
        Entity target = UtilEntity.getLevel(missile).getEntity(params.targetParams.opticalTargetEntityId);
        if (target == null) {
            setLaunchFail("error.dscombat.no_target_selected");
            return null;
        }
        missile.targetPos = target.position();
        missile.target = target;
		return missile;
	}

	protected AllEntityMissile<?> shootRadarGuided(@NotNull WeaponShootParameters params,
												   @NotNull AllEntityMissile<?> missile) {
		if (!getStats().isCanRadarGuide()) {
			setLaunchFail("error.dscombat.not_radar_missile");
			return null;
		}
		if (params.vehicle == null) return missile;
		RadarSystem radar = params.vehicle.radarSystem;
		if (!radar.hasRadar()) {
			setLaunchFail("error.dscombat.no_radar");
			return null;
		}
		RadarTarget ping = radar.getServerSelectedTarget();
		if (ping == null) {
			setLaunchFail("error.dscombat.no_target_selected");
			return null;
		}
		Entity target = radar.getSelectedTargetEntity();
		if (target == null) {
			setLaunchFail("error.dscombat.no_target_selected");
			return null;
		}
		boolean groundWater = UtilVehicleEntity.isOnGroundOrWater(target);
		RadarTargetType targetType = getStats().getRadarTargetType();
		if (targetType == RadarTargetType.AIR && groundWater) {
			setLaunchFail("error.dscombat.air_target_only");
			return null;
		} else if (targetType == RadarTargetType.GROUND && !groundWater) {
			setLaunchFail("error.dscombat.ground_target_only");
			return null;
		} else if (targetType == RadarTargetType.WATER && !target.isInWater()) {
			setLaunchFail("error.dscombat.water_target_only");
			return null;
		}
		missile.target = target;
		return missile;
	}

	@Override
	public TargetMode fixTargetMode(TargetMode currentTargetMode, TargetMode preferedPosTargetMode) {
		switch (currentTargetMode) {
			case LOOK, COORDS, MARKER -> {
				if (!getStats().isCanPositionGuide()) {
					if (getStats().isCanRadarGuide()) return TargetMode.RADAR;
					else if (getStats().isCanOpticalGuide()) return TargetMode.OPTICAL;
				}
			}
			case RADAR -> {
				if (!getStats().isCanRadarGuide()) {
					if (getStats().isCanPositionGuide()) return preferedPosTargetMode;
					else if (getStats().isCanOpticalGuide()) return TargetMode.OPTICAL;
				}
			}
			case OPTICAL -> {
				if (!getStats().isCanOpticalGuide()) {
					if (getStats().isCanPositionGuide()) return preferedPosTargetMode;
					else if (getStats().isCanRadarGuide()) return TargetMode.RADAR;
				}
			}
		}
		return currentTargetMode;
	}

	@Override
	public TargetMode getDefaultTargetMode() {
		return fixTargetMode(TargetMode.RADAR, TargetMode.LOOK);
	}

}
