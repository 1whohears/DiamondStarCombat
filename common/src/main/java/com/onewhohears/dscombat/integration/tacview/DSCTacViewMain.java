package com.onewhohears.dscombat.integration.tacview;

import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.tacview.common.core.EntityKeyframe;
import com.onewhohears.tacview.common.core.EntityRecorders;
import com.onewhohears.tacview.common.core.SessionManager;
import com.onewhohears.tacview.common.event.TacviewEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DSCTacViewMain {

    public static void init() {
        registerDSCRecorders();
        TacviewEvents.GET_PLAYER_VEHICLE_TO_SAVE_EVENT.register(DSCTacViewMain::onGetPlayerVehicleToSave);
    }

    private static @Nullable Entity onGetPlayerVehicleToSave(@NotNull Entity entity) {
        if (entity.getRootVehicle() instanceof EntityVehicle vehicle) return vehicle;
        return null;
    }

    public static void registerDSCRecorders() {
        // vehicle recorders
        registerVehicleRecorder(ModEntities.PLANE.get());
        registerVehicleRecorder(ModEntities.HELICOPTER.get());
        registerVehicleRecorder(ModEntities.CAR.get());
        registerVehicleRecorder(ModEntities.STATIONARY.get());
        registerVehicleRecorder(ModEntities.BOAT.get());
        registerVehicleRecorder(ModEntities.SUBMARINE.get());
        // weapon recorders
        registerGenericWeaponRecorder(ModEntities.BULLET.get());
        registerGenericWeaponRecorder(ModEntities.BOMB.get());
        registerGenericWeaponRecorder(ModEntities.BUNKER_BUSTER.get());
        registerMissileRecorder(ModEntities.POS_MISSILE.get());
        registerMissileRecorder(ModEntities.IR_MISSILE.get());
        registerMissileRecorder(ModEntities.TRACK_MISSILE.get());
        registerMissileRecorder(ModEntities.ANTI_RADAR_MISSILE.get());
        registerMissileRecorder(ModEntities.TORPEDO_MISSILE.get());
        registerMissileRecorder(ModEntities.DUMB_TORPEDO_MISSILE.get());
        // part recorders
        registerEntityPartRecorder(ModEntities.SEAT.get());
        registerEntityPartRecorder(ModEntities.TURRET.get());
        registerEntityPartRecorder(ModEntities.CHAIN_HOOK.get());
        registerEntityPartRecorder(ModEntities.GIMBAL_CAMERA.get());
        registerEntityPartRecorder(ModEntities.EXTERNAL_ENGINE.get());
        registerEntityPartRecorder(ModEntities.EXTERNAL_RADAR.get());
        registerEntityPartRecorder(ModEntities.EXTERNAL_WEAPON_PART.get());
        registerEntityPartRecorder(ModEntities.EXTERNAL_FUEL_TANK.get());
        // visible values
        EntityKeyframe.VISIBLE_VALUES.add("shield");
        EntityKeyframe.VISIBLE_VALUES.add("throttle");
        EntityKeyframe.VISIBLE_VALUES.add("age");
    }

    public static void registerVehicleRecorder(EntityType<?> type) {
        EntityRecorders.registerEntityRecorder(type,
                (entity, recordRate) -> new VehicleRecorder.Generic((EntityVehicle) entity, recordRate),
                VehicleRecorder.Generic::new);
    }

    public static void registerGenericWeaponRecorder(EntityType<?> type) {
        EntityRecorders.registerEntityRecorder(type,
                (entity, recordRate) -> new WeaponRecorder.Generic((EntityWeapon<?>) entity, recordRate),
                WeaponRecorder.Generic::new);
    }

    public static void registerMissileRecorder(EntityType<?> type) {
        EntityRecorders.registerEntityRecorder(type,
                (entity, recordRate) -> new WeaponRecorder.Missile((EntityMissile<?>) entity, recordRate),
                WeaponRecorder.Missile::new);
    }

    public static void registerEntityPartRecorder(EntityType<?> type) {
        EntityRecorders.registerEntityRecorder(type,
                (entity, recordRate) -> new ExternalPartRecorder((EntityPart) entity, recordRate),
                ExternalPartRecorder::new);
    }

    public static void onWeaponShoot(@NotNull EntityWeapon<?> weapon) {
        List<Entity> addEntities = new ArrayList<>();
        addEntities.add(weapon);
        Entity owner = weapon.getOwner();
        if (owner != null) {
            addEntities.add(owner);
            Entity vehicle = owner.getVehicle();
            if (vehicle != null) {
                addEntities.add(vehicle);
                Entity root = owner.getRootVehicle();
                if (!vehicle.equals(root)) {
                    addEntities.add(root);
                }
            }
        }
        Entity target = weapon.getTarget();
        if (target != null) {
            Entity controller = target.getControllingPassenger();
            if (controller != null) target = controller;
            addEntities.add(target);
        }
        SessionManager.get().recordEvent(new ShootEvent(weapon), true, addEntities.toArray(new Entity[0]));
    }
}
