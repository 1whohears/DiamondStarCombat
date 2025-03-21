package com.onewhohears.dscombat.data.vehicle.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilParse;

public abstract class PhysicsComponent {

    private final String hitbox;

    public PhysicsComponent(JsonObject json) {
        hitbox = UtilParse.getStringSafe(json, "hitbox", "");
    }

    public void tick(EntityVehicle vehicle) {
        if (canCalcPhysics(vehicle)) calcPhysics(vehicle);
    }

    protected boolean canCalcPhysics(EntityVehicle vehicle) {
        return getHitbox().isEmpty() || !vehicle.areAllHitboxesDead(getHitbox());
    }

    protected abstract void calcPhysics(EntityVehicle vehicle);

    public String getHitbox() {
        return hitbox;
    }
}
