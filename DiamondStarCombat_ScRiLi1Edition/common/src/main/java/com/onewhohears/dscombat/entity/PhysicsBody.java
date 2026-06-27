package com.onewhohears.dscombat.entity;

import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.dscombat.data.vehicle.physics.PhysicsComponentInstance;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PhysicsBody {

    /**
     * if an entity implements PhysicsBody, override setYRot and after calling super.setYRot(yRot)
     * call PhysicsBody.super.setYRot(yRot)
     */
    default void setYRot(float yRot) {
        setQBySide(UtilAngles.toQuaternionF(getYRot(), getXRot(), getZRot()));
    }
    /**
     * if an entity implements PhysicsBody, override setXRot and after calling super.setXRot(xRot)
     * call PhysicsBody.super.setXRot(xRot)
     */
    default void setXRot(float xRot) {
        setQBySide(UtilAngles.toQuaternionF(getYRot(), getXRot(), getZRot()));
    }
    /**
     * call this somewhere in {@link net.minecraft.world.entity.Entity#tick()} after super.tick()
     */
    default void tickPhysics() {
        QuaternionF q = getQBySide();
        // SET PREV/OLD
        setPrevDeltaMove(getDeltaMovement());
        setPrevForces(getForces());
        setPrevMoment(getMoment());
        setPrevZRot(getZRot());
        setPrevQ(q);
        // SET CURRENT FORCE/MOMENT TO 0
        setForces(Vec3.ZERO);
        setMoment(Vec3.ZERO);
        setControlMoment(Vec3.ZERO);
        // SET SPEED IF IN TEST MODE BUT NOT WIND TUNNEL
        boolean testMode = isTestMode();
        boolean windTunnel = isInWindTunnel();
        if (testMode && !windTunnel) setDeltaMovement(getLookAngle());
        // CALC NEW FORCE MOMENTS
        calcMoveStatsPre(q);
        calcForceMoment(q);
        getPhysicsInstances().forEach(instance -> instance.tick(this));
        // APPLY NEW FORCES
        calcAcc();
        motionClamp();
        if (!testMode) move(MoverType.SELF, getDeltaMovement());
        calcMoveStatsPost(q);
        // APPLY NEW MOMENT
        calcRotAcc(q);
        if (!testMode || !windTunnel) {
            setQBySide(q);
            updateEulerAngles();
        }
    }

    default boolean isInWindTunnel() {
        return !getPhysicsInstances().isEmpty() && getPhysicsInstances().get(0).getWindTunnel() != null;
    }

    default void updateEulerAngles() {
        QuaternionF q = getQBySide();
        UtilAngles.EulerAngles angles = UtilAngles.toDegrees(q);
        setXRotNoQ((float)angles.pitch);
        setYRotNoQ((float)angles.yaw);
        setZRot((float)angles.roll);
    }

    default void calcRotAcc(QuaternionF q) {
        clampControlMoment();
        addMoment(getControlMoment(), false, true);
        addMoment(getMomentBetweenTicks(), false, true);
        Vec3 m = getMoment().scale(getAccTimeScale());
        Vec3 av = getAngularVel();
        if (!UtilGeometry.isZero(m)) {
            Vec3 I = getTotalRotInertia();
            av = av.add(m.x/I.x, m.y/I.y, m.z/I.z);
            setAngularVel(av);
        }

        q.mul(rotateAngularVel(av));

        setMomentBetweenTicks(Vec3.ZERO);
        reducePitchRateWhileRolling();
    }

    static QuaternionF rotateAngularVel(Vec3 av) {
        return new QuaternionF((float)-av.x, (float)-av.y, (float)av.z, true);
    }

    default void reducePitchRateWhileRolling() {
        if (Math.abs(getZRot()) < 40 && getPitchInput() == 0 && isOperational())
            setAngularVel(getAngularVel().multiply(0.8, 1, 1));
    }

    default void addMoment(Vec3 moment, boolean control, boolean relative) {
        if (!relative) {
            /* FIXME angular velocities must be represented as a quaternion
             * angular velocities should not be applied relative to the current orientation
             * the relative parameter should be removed and assumed always false
             */

        }
        if (control && (!canUseTurnAssist() || isUsingTurnAssist())) {
            addControlMoment(moment);
        } else {
            setMoment(getMoment().add(moment));
        }
    }

    default void clampControlMoment() {
        Vec3 cm = getControlMoment();
        if (UtilGeometry.isZero(cm)) return;
        Vec3 I = getTotalRotInertia();
        Vec3 av = getAngularVel();
        double x = 0, y = 0, z = 0;
        if (cm.x != 0) x = getControlMomentComponent(cm.x, av.x, getControlMaxDeltaPitch(), I.x);
        if (cm.y != 0) y = getControlMomentComponent(cm.y, av.y, getControlMaxDeltaYaw(), I.y);
        if (cm.z != 0) z = getControlMomentComponent(cm.z, av.z, getControlMaxDeltaRoll(), I.z);
        setControlMoment(new Vec3(x, y, z));
    }

    float getControlMaxDeltaPitch();
    float getControlMaxDeltaYaw();
    float getControlMaxDeltaRoll();

    private double getControlMomentComponent(double cm, double v, float max, double I) {
        if (Math.abs(v) > max && Math.signum(v) == Math.signum(cm)) return 0;
        double a2 = cm / I * getAccTimeScale();
        double v2 = v + a2;
        double vd = Math.abs(v2) - max;
        if (vd > 0) cm -= vd * Math.signum(v2) * I / getAccTimeScale();
        return cm;
    }

    default void calcForceMoment(QuaternionF q) {
        calcUniversalForces(q);
        calcUniversalMoments(q);
        if (isOnGround() && isInWater()) {
            calcGroundMovement(q);
            calcWaterMovement(q);
        } else if (isOnGround()) calcGroundMovement(q);
        else if (isInWater()) calcWaterMovement(q);
        else calcAirMovement(q);
    }

    default void calcUniversalMoments(QuaternionF q) {
        applyAngularDrag();
        addControllingTorques(q);
    }

    default void applyAngularDrag() {
        Vec3 av = getAngularVel();
        float d = getAngularDrag();
        float dx = d, dy = d, dz = d;
        if (!isOnGround() || dontUseDriveTurnPhysics()) {
            if (getPitchInput() != 0 && Math.abs(av.x) <= getControlMaxDeltaPitch()) dx = 0;
            if (getYawInput() != 0 && Math.abs(av.y) <= getControlMaxDeltaYaw()) dy = 0;
            if (getRollInput() != 0 && Math.abs(av.z) <= getControlMaxDeltaRoll()) dz = 0;
        }
        Vec3 I = getTotalRotInertia();
        setAngularVel(new Vec3(
                getADComponent(av.x, dx, I.x),
                getADComponent(av.y, dy, I.y),
                getADComponent(av.z, dz, I.z)));
    }

    default boolean dontUseDriveTurnPhysics() {
        return false;
    }

    private double getADComponent(double v, float d, double I) {
        // AD needs to be scaled with speed
        double av = Math.abs(v);
        double da = d/I + av * 0.01;
        if (isHardCodedRotAcc()) da = d > 0 ? getHardCodedRotDecel() : 0;
        double a = av - da;
        if (a < 0.001) return 0;
        return a * Math.signum(v);
    }

    default float getAngularDrag() {
        float d = (float) getFluidDensity() * getAngularDragScale();
        if (isOnGround()) d *= 10;
        return d;
    }

    float getAngularDragScale();
    double getFluidDensity();
    double getAirDensity();
    Vec3 getTotalRotInertia();

    void addControllingTorques(QuaternionF q);

    default void hardCodedAccPitch() {
        Vec3 av = getAngularVel();
        double acc = getHardCodedRotAcc().x * getPitchInput();
        double next = getAccComp(av.x, acc, getControlMaxDeltaPitch());
        setAngularVel(new Vec3(next, av.y, av.z));
    }

    default void hardCodedAccYaw() {
        Vec3 av = getAngularVel();
        double acc = getHardCodedRotAcc().y * getYawInput();
        double next = getAccComp(av.y, acc, getControlMaxDeltaYaw());
        setAngularVel(new Vec3(av.x, next, av.z));
    }

    default void hardCodedAccRoll() {
        Vec3 av = getAngularVel();
        double acc = getHardCodedRotAcc().z * getRollInput();
        double next = getAccComp(av.z, acc, getControlMaxDeltaRoll());
        setAngularVel(new Vec3(av.x, av.y, next));
    }

    private double getAccComp(double current, double acc, double max) {
        double c = Math.abs(current);
        if (c > max) return current;
        if (acc < 0) return Math.max(current + acc, -max);
        else return Math.min(current + acc, max);
    }

    default void calcUniversalForces(QuaternionF q) {
        addForce(getWeightForce());
        addForce(getThrustForce(q));
        addDragForce(getDragForce(q));
    }

    void calcGroundMovement(QuaternionF q);
    void calcWaterMovement(QuaternionF q);
    void calcAirMovement(QuaternionF q);

    default Vec3 getWeightForce() {
        return new Vec3(0, -getTotalMass() * getAccGravity(), 0);
    }

    Vec3 getThrustForce(QuaternionF q);

    default Vec3 getDragForce(QuaternionF q) {
        return getDeltaMovement().normalize().scale(-getDragMag());
    }

    default Vec3 calcTotalDrag(QuaternionF q) {
        Vec3 d = getDragForce(q);
        for (PhysicsComponentInstance<?> phy : getPhysicsInstances()) {
            d = d.add(phy.getDragForce());
        }
        return d;
    }

    default Vec3 calcTotalLift(QuaternionF q) {
        Vec3 l = Vec3.ZERO;
        for (PhysicsComponentInstance<?> phy : getPhysicsInstances()) {
            l = l.add(phy.getLiftForce());
        }
        return l;
    }

    default double getDragMag() {
        // Drag = (drag coefficient) * (air density) * (speed)^2 * (drag area) / 2
        double speedSqr = getDeltaMovement().lengthSqr() * 400; // m/s
        return 0.5 * getFluidDensity() * speedSqr * getDragArea() * getDragCoefficient();
    }

    double getDragArea();
    double getDragCoefficient();

    default void calcAcc() {
        Vec3 f = getForces().add(getForcesBetweenTicks());
        setDeltaMovement(getDeltaMovement().add(getAccFromForce(f)));
        setForcesBetweenTicks(Vec3.ZERO);
    }

    default Vec3 getAccFromForce(Vec3 forces) {
        if (applyHorizontalSpeedScale())
            forces = forces.multiply(getHorizontalSpeedScale(), 1, getHorizontalSpeedScale());
        if (applyVerticalAccScale()) {
            forces = forces.multiply(1, getVerticalAccScale(forces.y()), 1);
        }
        double massScale = 1/getTotalMass();
        return forces.scale(massScale).scale(getAccTimeScale());
    }

    default double getAccFromForce(double force, boolean horizontal) {
        force = force / getTotalMass() * getAccTimeScale();
        if (horizontal) return force * getHorizontalSpeedScale();
        return force * getVerticalAccScale(force);
    }

    default void motionClamp() {
        Vec3 move = getDeltaMovement();
        double goalMaxXZ = Math.min(getMaxSpeedForMotion(), Config.SERVER.universalTopSpeed.get() / 20d);
        setLerpMaxXZ(Mth.lerp(DSCPhyCons.MAX_SPEED_CHANGE_RATE, getLerpMaxXZ(), goalMaxXZ));

        Vec3 motionXZ = new Vec3(move.x, 0, move.z);
        double velXZ = motionXZ.length();
        if (velXZ > getLerpMaxXZ()) motionXZ = motionXZ.scale(getLerpMaxXZ() / velXZ);

        setDeltaMovement(motionXZ.x, clampYMove(move), motionXZ.z);
    }

    default double clampYMove(Vec3 move) {
        double my = move.y;
        if (my > getMaxClimbSpeed()) my = getMaxClimbSpeed();
        else if (my < -getMaxFallSpeed()) my = -getMaxFallSpeed();
        else if (Math.abs(my) < 0.001) my = 0;

        double decreaseSpeedPos = 100;
        double altitude = getAltitude();
        double nextY = altitude + my;
        if (nextY > getMaxAltitude()) {
            my = getMaxAltitude() - altitude;
            if (flattenIfMaxAltitude() && getXRot() < 0)
                flattenPitch(getQBySide(), 0.1f);
        } else if (altitude > getMaxAltitude() - decreaseSpeedPos) {
            double percent = (altitude - getMaxAltitude() + decreaseSpeedPos) / decreaseSpeedPos;
            double maxY = 1 - percent;
            if (my > maxY) my = maxY;
            if (flattenIfMaxAltitude() && getXRot() < -maxY * 20)
                flattenPitch(getQBySide(), (float) (0.1 * percent));
        }

        if (isOnGround() && my < 0) my = -0.01; // THIS MUST BE BELOW ZERO
        return my;
    }

    default boolean flattenIfMaxAltitude() {
        return true;
    }

    default void addForce(Vec3 force) {
        setForces(getForces().add(force));
    }

    default void addDragForce(Vec3 force) {
        Vec3 m = getDeltaMovement();
        if (UtilGeometry.isZero(m)) return;
        Vec3 acc = getAccFromForce(force);
        if (m.x != 0 && Math.signum(m.x+acc.x) != Math.signum(m.x)) {
            force = force.multiply(0, 1, 1);
            m = m.multiply(0, 1, 1);
        }
        if (m.y != 0 && Math.signum(m.y+acc.y) != Math.signum(m.y)) {
            force = force.multiply(1, 0, 1);
            m = m.multiply(1, 0, 1);
        }
        if (m.z != 0 && Math.signum(m.z+acc.z) != Math.signum(m.z)) {
            force = force.multiply(1, 1, 0);
            m = m.multiply(1, 1, 0);
        }
        setDeltaMovement(m);
        addForce(force);
    }

    default void addFrictionForce(double f) {
        Vec3 m = getDeltaMovement();
        if (m.x == 0 && m.z == 0) return;
        Vec3 mn = m.normalize();
        Vec3 force = mn.scale(-f);
        Vec3 acc = getAccFromForce(force);
        if (m.x != 0 && Math.signum(m.x+acc.x) != Math.signum(m.x)) {
            force = force.multiply(0, 1, 1);
            m = m.multiply(0, 1, 1);
        }
        if (m.z != 0 && Math.signum(m.z+acc.z) != Math.signum(m.z)) {
            force = force.multiply(1, 1, 0);
            m = m.multiply(1, 1, 0);
        }
        setDeltaMovement(m);
        setForces(getForces().add(force));
    }

    default void addControlMoment(Vec3 moment) {
        setControlMoment(getControlMoment().add(moment));
    }

    default void addForcesBetweenTicks(Vec3 forces) {
        setForcesBetweenTicks(getForcesBetweenTicks().add(forces));
    }

    default void addMomentBetweenTicks(Vec3 moment) {
        setMomentBetweenTicks(getMomentBetweenTicks().add(moment));
    }

    default void addMomentX(float moment, boolean control) {
        addMoment(Vec3.ZERO.add(moment, 0, 0), control, true);
    }

    default void addMomentY(float moment, boolean control) {
        addMoment(Vec3.ZERO.add(0, moment, 0), control, true);
    }

    default void addMomentZ(float moment, boolean control) {
        addMoment(Vec3.ZERO.add(0, 0, moment), control, true);
    }

    default void flattenPitch(QuaternionF q, float dPitch) {
        Vec3 av = getAngularVel();
        float x = (float)av.x;
        UtilAngles.EulerAngles angles = UtilAngles.toDegrees(q);
        if (dPitch != 0) {
            float goalPitch = 0, pitch;
            if (isOnGround()) goalPitch = -getGroundXTilt();
            float diff = (float)angles.pitch - goalPitch;
            if (Math.abs(diff) < dPitch) pitch = diff;
            else pitch = Math.signum(diff) * dPitch;
            x += pitch;
        }
        setAngularVel(new Vec3(x, av.y, av.z));
    }

    default void flatten(QuaternionF q, float dPitch, float dRoll, boolean forced) {
        Vec3 av = getAngularVel();
        float x = (float)av.x, z = (float)av.z;
        if (!forced) {
            if (Math.abs(av.x) <= dPitch) x = 0;
            if (Math.abs(av.z) <= dRoll) z = 0;
        } else x = z = 0;
        UtilAngles.EulerAngles angles = UtilAngles.toDegrees(q);
        float roll, pitch;
        if (dRoll != 0) {
            if (Math.abs(angles.roll) < dRoll) roll = (float) -angles.roll;
            else roll = -(float)Math.signum(angles.roll) * dRoll;
            z += roll;
        }
        if (dPitch != 0) {
            float goalPitch = 0;
            if (isOnGround()) goalPitch = -getGroundXTilt();
            float diff = (float)angles.pitch - goalPitch;
            if (Math.abs(diff) < dPitch) pitch = diff;
            else pitch = Math.signum(diff) * dPitch;
            x += pitch;
        }
        setAngularVel(new Vec3(x, av.y, z));
    }

    default double getHorizontalSpeedScaleOrOne() {
        if (applyHorizontalSpeedScale()) return getHorizontalSpeedScale();
        return 1;
    }

    /**
     * THIS METHOD MUST BE MANUALLY IMPLEMENTED IF CHILD CLASS IS ENTITY
     */
    void move(@NotNull MoverType type, @NotNull Vec3 move);

    List<PhysicsComponentInstance<?>> getPhysicsInstances();

    void calcMoveStatsPre(QuaternionF q);
    void calcMoveStatsPost(QuaternionF q);

    float getTotalMass();
    double getAccTimeScale();
    double getHorizontalSpeedScale();
    boolean applyHorizontalSpeedScale();
    double getVerticalAccScale(double verticalForce);
    boolean applyVerticalAccScale();
    double getMaxSpeedForMotion();
    double getLerpMaxXZ();
    void setLerpMaxXZ(double maxXZ);
    double getMaxClimbSpeed();
    double getMaxFallSpeed();
    double getAltitude();
    double getMaxAltitude();
    double getAccGravity();
    float getGroundXTilt();
    boolean isHardCodedRotAcc();
    Vec3 getHardCodedRotAcc();
    float getHardCodedRotDecel();

    QuaternionF getQBySide();
    void setQBySide(QuaternionF q);
    QuaternionF getPrevQ();
    void setPrevQ(QuaternionF q);

    /**
     * THIS METHOD MUST BE MANUALLY IMPLEMENTED IF CHILD CLASS IS ENTITY
     */
    float getXRot();
    /**
     * THIS METHOD MUST BE MANUALLY IMPLEMENTED IF CHILD CLASS IS ENTITY
     */
    float getYRot();
    void setXRotNoQ(float rot);
    void setYRotNoQ(float rot);
    float getZRot();
    void setZRot(float rot);
    float getPrevZRot();
    void setPrevZRot(float rot);

    /**
     * THIS METHOD MUST BE MANUALLY IMPLEMENTED IF CHILD CLASS IS ENTITY
     */
    Vec3 getDeltaMovement();
    /**
     * THIS METHOD MUST BE MANUALLY IMPLEMENTED IF CHILD CLASS IS ENTITY
     */
    void setDeltaMovement(Vec3 move);
    /**
     * THIS METHOD MUST BE MANUALLY IMPLEMENTED IF CHILD CLASS IS ENTITY
     */
    void setDeltaMovement(double x, double y, double z);
    Vec3 getPrevDeltaMove();
    void setPrevDeltaMove(Vec3 move);
    Vec3 getForces();
    void setForces(Vec3 forces);
    Vec3 getForcesBetweenTicks();
    void setForcesBetweenTicks(Vec3 forces);
    Vec3 getPrevForces();
    void setPrevForces(Vec3 forces);
    Vec3 getMoment();
    void setMoment(Vec3 moment);
    Vec3 getPrevMoment();
    void setPrevMoment(Vec3 moment);
    Vec3 getControlMoment();
    void setControlMoment(Vec3 moment);
    Vec3 getMomentBetweenTicks();
    void setMomentBetweenTicks(Vec3 moment);
    Vec3 getAngularVel();
    void setAngularVel(Vec3 av);
    /**
     * THIS METHOD MUST BE MANUALLY IMPLEMENTED IF CHILD CLASS IS ENTITY
     */
    Vec3 getLookAngle();

    /**
     * THIS METHOD MUST BE MANUALLY IMPLEMENTED IF CHILD CLASS IS ENTITY
     */
    boolean isOnGround();
    /**
     * THIS METHOD MUST BE MANUALLY IMPLEMENTED IF CHILD CLASS IS ENTITY
     */
    boolean isInWater();
    boolean isTestMode();
    boolean isArcadeMode();
    boolean canUseTurnAssist();
    boolean isUsingTurnAssist();
    boolean areAllHitboxesDead(String... hitbox);
    boolean isOperational();
    int getAge();
    boolean isClientSide();

    float getPitchInput();
    float getYawInput();
    float getRollInput();
    boolean isFlapsDown();

    void debug(String info);
}
