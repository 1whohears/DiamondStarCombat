package com.onewhohears.dscombat.data.vehicle.physics;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

public abstract class PhysicsComponentInstance<D extends PhysicsComponentData> {

    private final D data;

    public PhysicsComponentInstance(D data) {
        this.data = data;
    }

    public void tick(EntityVehicle vehicle) {
        if (canCalcPhysics(vehicle)) calcPhysics(vehicle);
    }

    protected boolean canCalcPhysics(EntityVehicle vehicle) {
        return getData().getHitbox().isEmpty() || !vehicle.areAllHitboxesDead(getData().getHitbox());
    }

    protected abstract void calcPhysics(EntityVehicle vehicle);

    public D getData() {
        return data;
    }
}
