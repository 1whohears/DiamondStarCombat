package com.onewhohears.dscombat.data.vehicle.physics;

import com.onewhohears.dscombat.entity.PhysicsBody;

public abstract class PhysicsComponentInstance<D extends PhysicsComponentData> {

    private final D data;

    public PhysicsComponentInstance(D data) {
        this.data = data;
    }

    public void tick(PhysicsBody body) {
        if (canCalcPhysics(body)) calcPhysics(body);
    }

    protected boolean canCalcPhysics(PhysicsBody body) {
        return body.isTestMode() || getData().getHitbox().isEmpty() || !body.areAllHitboxesDead(getData().getHitbox());
    }

    protected abstract void calcPhysics(PhysicsBody body);

    public D getData() {
        return data;
    }
}
