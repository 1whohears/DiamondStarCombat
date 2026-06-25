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
            double currentSpeed = getXZSpeed() * getXZSpeedDir();
            double maxSpeed = getMaxSpeedForMotion();
            double targetSpeed = getCurrentThrottle() * maxSpeed;

            // Hard-clamp speed to max allowed in current direction (handles reverse speed limit)
            double speedLimit = maxSpeed;
            if (Math.abs(currentSpeed) > speedLimit) {
                currentSpeed = speedLimit * Math.signum(currentSpeed);
            }

            // --- РЕАЛИЗМ: влияние уклона на скорость ---
            // xRot > 0 = нос вниз (спуск) -> ускорение, xRot < 0 = нос вверх (подъём) -> замедление
            double slopeFactor = Math.sin(Math.toRadians(-getXRot()));
            double slopeAcc = slopeFactor * DSCPhyCons.GRAVITY * 0.05;
            // Применяем только если едем вперёд или уклон помогает
            if (getXZSpeedDir() == 1 || slopeAcc * Math.signum(currentSpeed) > 0) {
                driveAcc += slopeAcc;
            }

            // --- РЕАЛИЗМ: инерция — скорость нарастает/падает постепенно ---
            // Вместо мгновенного прыжка к targetSpeed используем ускорение как ограничитель
            double speedDiff = targetSpeed - currentSpeed;
            double maxAccThisTick = Math.abs(driveAcc);
            double appliedAcc;
            if (Math.abs(speedDiff) <= maxAccThisTick) {
                // Уже близко к цели — дотягиваемся точно
                appliedAcc = speedDiff;
            } else {
                // Ограничиваем изменение скорости за тик
                appliedAcc = maxAccThisTick * Math.signum(speedDiff);
            }

            // --- РЕАЛИЗМ: торможение двигателем при снятии газа ---
            float throttle = Math.abs(getCurrentThrottle());
            if (throttle < 0.05f && getXZSpeed() > 0.001f) {
                // Двигатель тормозит при нулевом газе (engine braking)
                double engineBraking = isNoPilotBraking() ? 0.008 : 0.0015;
                appliedAcc -= engineBraking * Math.signum(currentSpeed);
            }

            double newSpeed = currentSpeed + appliedAcc;
            // Не даём перескочить через ноль при торможении
            if (Math.signum(newSpeed) != Math.signum(currentSpeed) && Math.abs(targetSpeed) < 0.001) {
                newSpeed = 0;
            }
            setDeltaMovement(n.scale(newSpeed));
        }
        // turn physics
        if (dontUseDriveTurnPhysics()) return;
        float max_tr = getTurnRadius();
        Vec3 av = getAngularVel();
        if (getYawInput() == 0 || max_tr == 0) {
            if (!isSliding()) setAngularVel(av.multiply(1, 0, 1));
            return;
        }
        // Prevent division by very small numbers that could cause extreme values
        float yawInput = getYawInput();
        if (Math.abs(yawInput) < 0.01f) {
            if (!isSliding()) setAngularVel(av.multiply(1, 0, 1));
            return;
        }
        // --- РЕАЛИЗМ: радиус поворота растёт с увеличением скорости ---
        // При высокой скорости машина поворачивает менее охотно
        float speedRatio = Math.min(getXZSpeed() / Math.max((float) getMaxSpeedForMotion(), 0.001f), 1f);
        float dynamicTurnRadius = max_tr * (1f + speedRatio * 0.6f);
        float tr = 1 / yawInput * dynamicTurnRadius;
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
            double cen_acc = getXZSpeed() * getXZSpeed() / tr * 150; // cen_acc needed to complete turn (m/s/s)
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

    /** Returns true when there is no pilot and the vehicle should brake faster. */
    default boolean isNoPilotBraking() {
        return false;
    }

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
