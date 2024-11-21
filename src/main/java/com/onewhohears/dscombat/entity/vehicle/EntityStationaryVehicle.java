package com.onewhohears.dscombat.entity.vehicle;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.vehicle.VehicleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityStationaryVehicle extends EntityVehicle {

    public EntityStationaryVehicle(EntityType<? extends EntityVehicle> entityType, Level level, String defaultPreset) {
        super(entityType, level, defaultPreset);
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.STATIONARY;
    }

    @Override
    public boolean canBrake() {
        return false;
    }

    @Override
    public Vec3 getThrustForce(Quaternion q) {
        return Vec3.ZERO;
    }

    @Override
    public boolean canToggleLandingGear() {
        return false;
    }
}
