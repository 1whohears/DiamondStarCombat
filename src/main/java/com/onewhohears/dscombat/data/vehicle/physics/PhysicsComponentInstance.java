package com.onewhohears.dscombat.data.vehicle.physics;

import com.onewhohears.dscombat.entity.PhysicsBody;
import net.minecraft.world.phys.Vec3;

public abstract class PhysicsComponentInstance<D extends PhysicsComponentData> {

    private final D data;

    public PhysicsComponentInstance(D data) {
        this.data = data;
    }

    public void tick(PhysicsBody body) {
        if (canCalcPhysics(body)) calcPhysics(body);
    }

    protected boolean canCalcPhysics(PhysicsBody body) {
        if (body.isTestMode()) return true;
        if (getData().getHitbox().equals("NONE")) return body.isOperational();
        else return !body.areAllHitboxesDead(getData().getHitbox());
    }

    protected abstract void calcPhysics(PhysicsBody body);

    public D getData() {
        return data;
    }

    public abstract Vec3 getDragForce();
    public abstract Vec3 getLiftForce();
    public abstract float getAOA();
}
