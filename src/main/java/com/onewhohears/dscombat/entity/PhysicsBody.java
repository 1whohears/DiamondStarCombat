package com.onewhohears.dscombat.entity;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
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
        setQBySide(UtilAngles.toQuaternion(getYRot(), getXRot(), getZRot()));
    }
    /**
     * if an entity implements PhysicsBody, override setXRot and after calling super.setXRot(xRot)
     * call PhysicsBody.super.setXRot(xRot)
     */
    default void setXRot(float xRot) {
        setQBySide(UtilAngles.toQuaternion(getYRot(), getXRot(), getZRot()));
    }
    /**
     * call this somewhere in {@link net.minecraft.world.entity.Entity#tick()} after super.tick()
     */
    default void tickPhysics() {
        Quaternion q = getQBySide();
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
        // CALC NEW FORCE MOMENTS
        calcMoveStatsPre(q);
        calcForceMoment(q);
        getPhysicsInstances().forEach(instance -> instance.tick(this));
        // APPLY NEW FORCES
        calcAcc();
        motionClamp();
        if (!isTestMode()) move(MoverType.SELF, getDeltaMovement());
        calcMoveStatsPost(q);
        // APPLY NEW MOMENT
        calcRotAcc(q);
        setQBySide(q);
        UtilAngles.EulerAngles angles = UtilAngles.toDegrees(q);
        setXRotNoQ((float)angles.pitch);
        setYRotNoQ((float)angles.yaw);
        setZRot((float)angles.roll);
    }

    default void calcRotAcc(Quaternion q) {
        clampControlMoment();
        addMoment(getControlMoment(), false, true);
        Vec3 m = getMoment().add(getMomentBetweenTicks()).scale(getAccTimeScale());
        Vec3 av = getAngularVel();
        if (!UtilGeometry.isZero(m)) {
            Vec3 I = getTotalRotInertia();
            av = av.add(m.x/I.x, m.y/I.y, m.z/I.z);
            setAngularVel(av);
        }
        q.mul(Vector3f.XN.rotationDegrees((float)av.x));
        q.mul(Vector3f.YN.rotationDegrees((float)av.y));
        q.mul(Vector3f.ZP.rotationDegrees((float)av.z));
        setMomentBetweenTicks(Vec3.ZERO);
    }

    default void addMoment(Vec3 moment, boolean control, boolean relative) {
        //if (!relative) {
        // FIXME not all moments are applies relative to the vehicle's current axis of rotation

        //}
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

    default void calcForceMoment(Quaternion q) {
        calcUniversalForces(q);
        calcUniversalMoments(q);
        if (isOnGround() && isInWater()) {
            calcGroundMovement(q);
            calcWaterMovement(q);
        } else if (isOnGround()) calcGroundMovement(q);
        else if (isInWater()) calcWaterMovement(q);
        else calcAirMovement(q);
    }

    default void calcUniversalMoments(Quaternion q) {
        applyAngularDrag();
        addControllingTorques(q);
    }

    default void applyAngularDrag() {
        Vec3 av = getAngularVel();
        float d = getAngularDrag();
        float dx = d, dy = d, dz = d;
        if (!isOnGround()) {
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

    private double getADComponent(double v, float d, double I) {
        // AD needs to be scaled with speed
        double av = Math.abs(v);
        double a = av - (d/I + av * 0.01);
        if (a < 0) return 0;
        return a * Math.signum(v);
    }

    default float getAngularDrag() {
        float d = (float) getFluidDensity() * getAngularDragScale();
        if (isOnGround()) d *= 10;
        return d;
    }

    float getAngularDragScale();
    double getFluidDensity();
    Vec3 getTotalRotInertia();

    void addControllingTorques(Quaternion q);

    default void calcUniversalForces(Quaternion q) {
        addForce(getWeightForce());
        addForce(getThrustForce(q));
        addForce(getDragForce(q));
    }

    void calcGroundMovement(Quaternion q);
    void calcWaterMovement(Quaternion q);
    void calcAirMovement(Quaternion q);

    default Vec3 getWeightForce() {
        return new Vec3(0, -getTotalMass() * getAccGravity(), 0);
    }

    Vec3 getThrustForce(Quaternion q);

    default Vec3 getDragForce(Quaternion q) {
        return getDeltaMovement().normalize().scale(-getDragMag());
    }

    double getDragMag();

    default void calcAcc() {
        Vec3 f = getForces().add(getForcesBetweenTicks());
        if (applyHorizontalSpeedScale())
            f = f.multiply(getHorizontalSpeedScale(), 1, getHorizontalSpeedScale());
        double massScale = 1/getTotalMass();
        setDeltaMovement(getDeltaMovement().add(f.scale(massScale).scale(getAccTimeScale())));
        setForcesBetweenTicks(Vec3.ZERO);
    }

    default void motionClamp() {
        Vec3 move = getDeltaMovement();
        double goalMaxXZ = getMaxSpeedForMotion();
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
        if (nextY > getMaxAltitude()) my = getMaxAltitude() - altitude;
        else if (altitude > getMaxAltitude() - decreaseSpeedPos) {
            double maxY = 1 - (altitude - getMaxAltitude() + decreaseSpeedPos) / decreaseSpeedPos;
            if (my > maxY) my = maxY;
        }

        if (isOnGround() && my < 0) my = -0.01; // THIS MUST BE BELOW ZERO
        return my;
    }

    default void addForce(Vec3 force) {
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

    void move(@NotNull MoverType type, @NotNull Vec3 move);

    List<PhysicsComponentInstance<?>> getPhysicsInstances();

    void calcMoveStatsPre(Quaternion q);
    void calcMoveStatsPost(Quaternion q);

    float getTotalMass();
    double getAccTimeScale();
    double getHorizontalSpeedScale();
    boolean applyHorizontalSpeedScale();
    double getMaxSpeedForMotion();
    double getLerpMaxXZ();
    void setLerpMaxXZ(double maxXZ);
    double getMaxClimbSpeed();
    double getMaxFallSpeed();
    double getAltitude();
    double getMaxAltitude();
    double getAccGravity();

    Quaternion getQBySide();
    void setQBySide(Quaternion q);
    Quaternion getPrevQ();
    void setPrevQ(Quaternion q);

    float getXRot();
    float getYRot();
    void setXRotNoQ(float rot);
    void setYRotNoQ(float rot);
    float getZRot();
    void setZRot(float rot);
    float getPrevZRot();
    void setPrevZRot(float rot);

    Vec3 getDeltaMovement();
    void setDeltaMovement(Vec3 move);
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

    boolean isOnGround();
    boolean isInWater();
    boolean isTestMode();
    boolean canUseTurnAssist();
    boolean isUsingTurnAssist();
    boolean areAllHitboxesDead(String... hitbox);

    float getPitchInput();
    float getYawInput();
    float getRollInput();
    boolean isFlapsDown();

}
