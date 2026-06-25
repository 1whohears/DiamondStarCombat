package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.weapon.stats.SmokeGrenadeStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntitySmokeGrenade;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SmokeGrenadeInstance extends WeaponInstance<SmokeGrenadeStats> {

    public SmokeGrenadeInstance(SmokeGrenadeStats stats) {
        super(stats);
    }

    /**
     * Fires all launchers defined in the stats at once.
     * Each launcher has its own position and direction offsets.
     */
    public boolean fireAllLaunchers(Level level, Entity owner, EntityVehicle vehicle, boolean consume) {
        SmokeGrenadeStats.LauncherDef[] launchers = getStats().getLaunchers();
        if (launchers.length == 0) return false;
        // Check ammo and recoil once before firing all launchers
        if (!checkRecoil()) return false;
        if (!checkAmmo(1, owner)) {
            setLaunchFail("error.dscombat.no_ammo");
            return false;
        }

        boolean fired = false;
        for (SmokeGrenadeStats.LauncherDef launcher : launchers) {
            Vec3 worldPos = vehicle.position()
                    .add(UtilAngles.rotateVector(launcher.pos, vehicle.getQ()));
            float vehicleYaw   = vehicle.getYRot() + launcher.yaw;
            float vehiclePitch = -launcher.pitch;
            Vec3 direction = UtilAngles.rotationToVector(vehicleYaw, vehiclePitch);

            EntitySmokeGrenade w = new EntitySmokeGrenade(level);
            w.setPreset(getStatsId());
            w.setOwner(owner);
            w.setPos(worldPos);
            setDirection(w, direction);
            level.addFreshEntity(w);
            fired = true;
        }

        if (fired) {
            playShootSound(level, vehicle.position());
            setLaunchSuccess(1, owner, consume);
            updateClientAmmo(vehicle);
        }
        return fired;
    }

    @Override
    public EntityWeapon<?> getEntity(Level level) {
        EntitySmokeGrenade e = new EntitySmokeGrenade(level);
        e.setPreset(getStatsId());
        return e;
    }
}
