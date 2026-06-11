package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.common.core.PositionMarker;
import com.onewhohears.dscombat.common.core.PositionMarkerManager;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.stats.AllMissileStats;
import com.onewhohears.dscombat.data.weapon.stats.TargetDomainType;
import com.onewhohears.dscombat.data.weapon.stats.TargetMode;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.common.core.DistantVisibleManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AllEntityMissile<T extends AllMissileStats> extends EntityMissile<T> {

	public @NotNull TargetMode targetMode = TargetMode.LOOK;
    public int selectedMarkerId = -1;

	public AllEntityMissile(EntityType<? extends AllEntityMissile<?>> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
	}

	@Override
	public void tickGuide() {
        if (targetMode == TargetMode.MARKER) {
            if (!isClientSide() && selectedMarkerId != -1 && tickCount % 5 == 0) {
                PositionMarker marker = PositionMarkerManager.getServer().getMarker(selectedMarkerId);
                if (marker != null) targetPos = marker.getPosition();
            }
            guideToPosition();
        } else if (targetMode == TargetMode.RADAR) {
            if (!getStats().isRadarActive() && !isClientSide()) notActiveCheckTarget();
            guideToTarget();
            if (!isClientSide() && tickCount % 10 == 0 && target instanceof EntityVehicle plane) {
                plane.trackedByMissile(this);
            }
        } else if (targetMode == TargetMode.OPTICAL) {
            if (target != null && tickCount % 15 == 0) {
                Entity owner = getOwner();
                if (owner == null) {
                    resetTarget();
                    return;
                }
                if (!(owner.getRootVehicle() instanceof EntityVehicle vehicle)) {
                    resetTarget();
                    return;
                }
                if (vehicle.getGimbalForPilotCamera() == null) {
                    DistantVisibleManager.cancelFirstEntityQuery(vehicle.getId(), target.getId(), MISSILE_SCAN_HANDLER.typeId());
                    resetTarget();
                    return;
                }
                DistantVisibleManager.queryVisible(getServer(), vehicle, target, MISSILE_SCAN_HANDLER);
            }
            guideToTarget();
        } else if (targetMode.isPosition()) {
            guideToPosition();
        } else {
			guideToPosition();
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
	public WeaponType getWeaponType() {
		return WeaponType.ALL_MISSILE;
	}

    @Override
    public boolean isDieInWater() {
        return getStats().getTargetType() != TargetDomainType.WATER;
    }

    @Override
    public boolean isCheckTargetEntityVisible() {
        if (targetMode == TargetMode.OPTICAL) return false;
        if (targetMode == TargetMode.RADAR) return getWeaponStats().isRadarActive();
        return true;
    }

}
