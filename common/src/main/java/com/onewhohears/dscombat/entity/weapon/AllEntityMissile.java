package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.stats.AllMissileStats;
import com.onewhohears.dscombat.data.weapon.stats.RadarTargetType;
import com.onewhohears.dscombat.data.weapon.stats.TargetMode;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AllEntityMissile<T extends AllMissileStats> extends EntityMissile<T> {

	public @NotNull TargetMode targetMode = TargetMode.LOOK;

	public AllEntityMissile(EntityType<? extends AllEntityMissile<?>> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
	}

	@Override
	public void tickGuide() {
		if (targetMode.isPosition()) {
			guideToPosition();
		} else if (targetMode == TargetMode.RADAR) {
			if (!getStats().isRadarActive() && !isClientSide()) notActiveCheckTarget();
			guideToTarget();
			if (!isClientSide() && tickCount % 10 == 0 && target instanceof EntityVehicle plane) {
				plane.trackedByMissile(this);
			}
		} else if (targetMode == TargetMode.OPTICAL) {
			guideToPosition(); // TODO setup optical guidance system
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
        return getStats().getRadarTargetType() != RadarTargetType.WATER;
    }

}
