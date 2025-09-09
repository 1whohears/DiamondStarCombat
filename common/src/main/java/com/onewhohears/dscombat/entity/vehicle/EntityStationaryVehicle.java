package com.onewhohears.dscombat.entity.vehicle;

import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.dscombat.data.vehicle.VehicleType;
import com.onewhohears.dscombat.data.vehicle.physics.PhysicsComponentInstance;
import com.onewhohears.dscombat.init.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityStationaryVehicle extends EntityVehicle {

    public EntityStationaryVehicle(EntityType<? extends EntityVehicle> entityType, Level level, String defaultPreset) {
        super(entityType, level, defaultPreset);
    }

    @Override
    public void serverTick() {
        super.serverTick();
        if (getStats().isStationaryRadar() && tickCount % 40 == 0 && radarSystem.hasTargets() && isOperational())
            getLevel().playSound(null, this, ModSounds.TARGETS_FOUND, SoundSource.PLAYERS, 1, 1);
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.STATIONARY;
    }

    @Override
    public boolean canGroundBrake() {
        return false;
    }

    @Override
    public boolean canToggleLandingGear() {
        return false;
    }

    @Override
    public boolean canDriveOnGround() {
        return false;
    }

    @Override
    public Vec3 getThrustForce(QuaternionF q) {
        return Vec3.ZERO;
    }

}
