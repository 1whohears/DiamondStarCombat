package com.onewhohears.dscombat.entity;

import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public interface DrivingBody extends PhysicsBody {

    default void calcGroundMovement(QuaternionF q) {
        if (canFlattenOnGround()) flatten(q, 4f, 4f, true);
        if (canDriveOnGround()) calcDriveMovement(q);
        else calcOtherGroundMovement(q);
        if (canGroundBrake() && isGroundBraking()) applyGroundBreaks();
    }

    default void applyGroundBreaks() {
        driveSlowDown(getGroundBreaksDeAcceleration());
    }

    default void calcDriveMovement(QuaternionF q) {
        // drive physics
        Vec3 n = UtilAngles.rotationToVector(getYRot(), 0);
        double driveAcc = getDriveAcc();
        double minDriveAcc = getMinDriveAcc(); // FIXME any weird behavior without fuel/engines?
        if (Math.abs(driveAcc) < minDriveAcc) driveAcc = minDriveAcc * (driveAcc < 0 ? -1 : 1);
        if (isSliding() || willSlideFromTurn()) {
            setDeltaMovement(getDeltaMovement().add(n.scale(driveAcc * 0.5)));
            addFrictionForce(getKineticFriction());
        } else {
            setDeltaMovement(n.scale(getXZSpeed()*getXZSpeedDir() + driveAcc));
            if (getCurrentThrottle() == 0 && getXZSpeed() != 0) driveSlowDown(0.0002);
        }
        // turn physics
        if (dontUseDriveTurnPhysics()) return;
        float max_tr = getTurnRadius();
        Vec3 av = getAngularVel();
        if (getYawInput() == 0 || max_tr == 0) {
            if (!isSliding()) setAngularVel(av.multiply(1, 0, 1));
            return;
        }
        float tr = 1 / getYawInput() * max_tr;
        float turn = getXZSpeed() / tr * getXZSpeedDir();
        float turnDeg = turn * Mth.RAD_TO_DEG;
        if (!isSliding()) av = av
                .multiply(1, 0, 1)
                .add(0, turnDeg, 0);
        else addMomentY(turnDeg*getSlideAngleCos()*DSCPhyCons.DRIFT_FACTOR, false);
        setAngularVel(av);
    }

    default boolean willSlideFromTurn() {
        double max_tr = getTurnRadius();
        if (getYawInput() != 0 && max_tr != 0) { // IF TURNING
            double tr = max_tr * 1 / Math.abs(getYawInput()); // inputed turn radius
            double cen_acc = getXZSpeed() * getXZSpeed() / tr * 400; // cen_acc needed to complete turn (m/s/s)
            double cen_force = cen_acc * getTotalMass(); // friction force needed to not slide
            //debug(cen_force+" >? "+staticFric);
            return cen_force >= getStaticFriction(); // if cen_force >= static-friction-threshold slide
        }
        return false;
    }

    default boolean isSlideAngleNearZero() {
        if (getXZSpeedDir() == -1) return Mth.abs(Mth.abs(getSlideAngle())-180) < 2;
        return Mth.abs(getSlideAngle()) < 2;
    }

    default boolean isSliding() {
        return !canDriveOnGround() || !isSlideAngleNearZero();
    }

    default void calcOtherGroundMovement(QuaternionF q) {
        addFrictionForce(getKineticFriction());
    }

    default void driveSlowDown(double amount) {
        Vec3 m = getDeltaMovement().multiply(1, 0, 1);
        if (UtilGeometry.isZero(m)) return;
        double speed = m.length();
        double newSpeed = speed - amount;
        if (Math.signum(newSpeed) != Math.signum(speed)) newSpeed = 0;
        m = m.scale(newSpeed / speed);
        setDeltaMovement(new Vec3(m.x, getDeltaMovement().y, m.z));
    }

    boolean canDriveOnGround();
    boolean canGroundBrake();
    boolean isGroundBraking();
    double getKineticFriction();
    double getStaticFriction();
    boolean canAirBrake();
    boolean isAirBreaking();
    double getGroundBreaksDeAcceleration();
    double getAirBreaksDeAcceleration();
    double getDriveAcc();
    float getCurrentThrottle();
    float getTurnRadius();
    boolean canFlattenOnGround();
    double getMinDriveAcc();

    default void calcAirMovement(QuaternionF q) {
        if (canAirBrake() && isAirBreaking()) applyAirBreaks();
    }

    default void applyAirBreaks() {
        airSlowDown(getAirBreaksDeAcceleration());
    }

    default void airSlowDown(double amount) {
        Vec3 m = getDeltaMovement();
        if (UtilGeometry.isZero(m)) return;
        double speed = m.length();
        double newSpeed = speed - amount;
        if (Math.signum(newSpeed) != Math.signum(speed)) newSpeed = 0;
        m = m.scale(newSpeed / speed);
        setDeltaMovement(m);
    }

    @Override
    default void calcMoveStatsPost(QuaternionF q) {
        Vec3 m = getDeltaMovement();
        float y = getYRot();
        setXZSpeed((float) Math.sqrt(m.x*m.x + m.z*m.z));
        if (getXZSpeed() == 0) {
            setXZYaw(y);
            setSlideAngle(0);
        } else {
            setXZYaw(UtilAngles.getYaw(m));
            setSlideAngle(Mth.degreesDifference(getXZYaw(), y));
            setSlideAngleCos((float) Math.abs(Math.cos(Math.toRadians(getSlideAngle()))));
        }
        setXZSpeedDir(1);
        if (Math.abs(getSlideAngle()) > 90) setXZSpeedDir(-1);
    }

    float getXZSpeed();
    void setXZSpeed(float speed);
    int getXZSpeedDir();
    void setXZSpeedDir(int direction);
    float getXZYaw();
    void setXZYaw(float angle);
    float getSlideAngle();
    void setSlideAngle(float angle);
    float getSlideAngleCos();
    void setSlideAngleCos(float angle);

}
