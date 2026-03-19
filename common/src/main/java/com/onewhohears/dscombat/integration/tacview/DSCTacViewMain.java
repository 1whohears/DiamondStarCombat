package com.onewhohears.dscombat.integration.tacview;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.tacview.common.core.EntityKeyframe;
import com.onewhohears.tacview.common.core.EntityRecorders;
import com.onewhohears.tacview.common.core.SessionManager;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class DSCTacViewMain {

    public static void registerDSCRecorders() {
        // vehicle recorders
        EntityRecorders.registerEntityRecorder(ModEntities.PLANE.get(),
                (entity, recordRate) -> new VehicleRecorder.Generic((EntityVehicle) entity, recordRate),
                VehicleRecorder.Generic::new);
        EntityRecorders.registerEntityRecorder(ModEntities.HELICOPTER.get(),
                (entity, recordRate) -> new VehicleRecorder.Generic((EntityVehicle) entity, recordRate),
                VehicleRecorder.Generic::new);
        EntityRecorders.registerEntityRecorder(ModEntities.CAR.get(),
                (entity, recordRate) -> new VehicleRecorder.Generic((EntityVehicle) entity, recordRate),
                VehicleRecorder.Generic::new);
        EntityRecorders.registerEntityRecorder(ModEntities.STATIONARY.get(),
                (entity, recordRate) -> new VehicleRecorder.Generic((EntityVehicle) entity, recordRate),
                VehicleRecorder.Generic::new);
        EntityRecorders.registerEntityRecorder(ModEntities.BOAT.get(),
                (entity, recordRate) -> new VehicleRecorder.Generic((EntityVehicle) entity, recordRate),
                VehicleRecorder.Generic::new);
        EntityRecorders.registerEntityRecorder(ModEntities.SUBMARINE.get(),
                (entity, recordRate) -> new VehicleRecorder.Generic((EntityVehicle) entity, recordRate),
                VehicleRecorder.Generic::new);
        // weapon recorders
        EntityRecorders.registerEntityRecorder(ModEntities.BULLET.get(),
                (entity, recordRate) -> new WeaponRecorder.Generic((EntityWeapon) entity, recordRate),
                WeaponRecorder.Generic::new);
        EntityRecorders.registerEntityRecorder(ModEntities.BOMB.get(),
                (entity, recordRate) -> new WeaponRecorder.Generic((EntityWeapon) entity, recordRate),
                WeaponRecorder.Generic::new);
        EntityRecorders.registerEntityRecorder(ModEntities.BUNKER_BUSTER.get(),
                (entity, recordRate) -> new WeaponRecorder.Generic((EntityWeapon) entity, recordRate),
                WeaponRecorder.Generic::new);
        EntityRecorders.registerEntityRecorder(ModEntities.POS_MISSILE.get(),
                (entity, recordRate) -> new WeaponRecorder.Missile((EntityMissile<?>) entity, recordRate),
                WeaponRecorder.Missile::new);
        EntityRecorders.registerEntityRecorder(ModEntities.IR_MISSILE.get(),
                (entity, recordRate) -> new WeaponRecorder.Missile((EntityMissile<?>) entity, recordRate),
                WeaponRecorder.Missile::new);
        EntityRecorders.registerEntityRecorder(ModEntities.TRACK_MISSILE.get(),
                (entity, recordRate) -> new WeaponRecorder.Missile((EntityMissile<?>) entity, recordRate),
                WeaponRecorder.Missile::new);
        EntityRecorders.registerEntityRecorder(ModEntities.ANTI_RADAR_MISSILE.get(),
                (entity, recordRate) -> new WeaponRecorder.Missile((EntityMissile<?>) entity, recordRate),
                WeaponRecorder.Missile::new);
        EntityRecorders.registerEntityRecorder(ModEntities.TORPEDO_MISSILE.get(),
                (entity, recordRate) -> new WeaponRecorder.Missile((EntityMissile<?>) entity, recordRate),
                WeaponRecorder.Missile::new);
        EntityRecorders.registerEntityRecorder(ModEntities.DUMB_TORPEDO_MISSILE.get(),
                (entity, recordRate) -> new WeaponRecorder.Missile((EntityMissile<?>) entity, recordRate),
                WeaponRecorder.Missile::new);
        // visible values
        EntityKeyframe.VISIBLE_VALUES.add("shield");
        EntityKeyframe.VISIBLE_VALUES.add("throttle");
        EntityKeyframe.VISIBLE_VALUES.add("age");
    }

    public static void onWeaponShoot(@NotNull EntityWeapon weapon) {
        Entity owner = weapon.getOwner();
        if (owner == null) return;
        SessionManager.get().recordEvent(new ShootEvent(weapon), true, weapon, owner);
    }
}
