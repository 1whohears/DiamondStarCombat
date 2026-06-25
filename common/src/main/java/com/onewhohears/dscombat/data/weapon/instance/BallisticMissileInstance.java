package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.BallisticMissileStats;
import com.onewhohears.dscombat.entity.weapon.EntityBallisticMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.world.phys.Vec3;

public class BallisticMissileInstance<T extends BallisticMissileStats> extends MissileInstance<T> {

    public BallisticMissileInstance(T stats) {
        super(stats);
    }

    @Override
    public EntityWeapon<?> getShootEntity(WeaponShootParameters params) {
        EntityBallisticMissile<?> missile = (EntityBallisticMissile<?>) super.getShootEntity(params);
        if (missile == null) return null;

        // Set target position
        if (params.vehicle != null && params.isPlayer) {
            Vec3 vehicleTarget = params.vehicle.weaponSystem.getTargetPos();
            missile.targetPos = vehicleTarget != null
                    ? vehicleTarget
                    : UtilEntity.getLookingAtBlockPos(params.owner, 2000);
        } else {
            missile.targetPos = UtilEntity.getLookingAtBlockPos(params.owner, 2000);
        }

        // Initial velocity: small upward push so it clears the launcher
        Vec3 vel = new Vec3(0, getStats().getSpeed() * 0.5, 0);
        if (params.vehicle != null) vel = vel.add(params.vehicle.getDeltaMovement());
        missile.setDeltaMovement(vel);
        missile.setXRot(-80f);
        missile.setYRot(params.owner.getYRot());

        return missile;
    }
}
