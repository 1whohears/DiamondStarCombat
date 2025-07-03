package com.onewhohears.dscombat.data.vehicle.physics;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.entity.PhysicsBody;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class LiftSurfaceInstance extends PhysicsComponentInstance<LiftSurfaceData> {

    private float aoa = 0;
    private Vec3 dragForce = Vec3.ZERO, liftForce = Vec3.ZERO;

    public LiftSurfaceInstance(LiftSurfaceData data) {
        super(data);
    }

    @Override
    protected void calcPhysics(PhysicsBody body) {
        Quaternion vehicleQ;
        if (getData().isIgnoreRoll()) {
            vehicleQ = Quaternion.ONE.copy();
            vehicleQ.mul(Vector3f.XP.rotationDegrees(body.getXRot()));
            vehicleQ.mul(Vector3f.YP.rotationDegrees(body.getYRot()));
        } else {
            vehicleQ = body.getQBySide().copy();
        }
        Quaternion surfaceQ = vehicleQ.copy();
        Vector3f rotation = getData().getRotation();
        if (rotation.x() != 0) surfaceQ.mul(Vector3f.XN.rotationDegrees(rotation.x()));
        if (rotation.y() != 0) surfaceQ.mul(Vector3f.YP.rotationDegrees(rotation.y()));
        if (rotation.z() != 0) surfaceQ.mul(Vector3f.ZP.rotationDegrees(rotation.z()));
        float rotate = getData().getInputType().getRotationFromInput(getData(), body);
        if (rotate != 0) surfaceQ.mul(Vector3f.XN.rotationDegrees(rotate));
        Vec3 u = body.getDeltaMovement();
        Vec3 pitchAxis = UtilAngles.getPitchAxis(surfaceQ);
        Vec3 liftDir = u.cross(pitchAxis).normalize();
        Vec3 airFoilAxes = UtilAngles.getRollAxis(surfaceQ);
        float airFoilSpeedSqr = (float) UtilGeometry.vecCompByNormAxis(u, airFoilAxes).lengthSqr() * 400; // m/s
        float goalAOA;
        Vec3 wingNormal = UtilAngles.getYawAxis(surfaceQ).scale(-1);
        if (UtilGeometry.isZero(u)) goalAOA = 0;
        else goalAOA = calcAOA(u, wingNormal);
        // change in AOA shouldn't be instant
        aoa = Mth.lerp(getAOAChangeRate(body), aoa, goalAOA);
        // find liftK
        float speedScaleSqr = (float) (1 / body.getHorizontalSpeedScale() / body.getHorizontalSpeedScale());
        float liftK = getLiftK(aoa) * speedScaleSqr;
        double P = getAirDensity(body);
        // Lift = (angle of attack coefficient) * (air density) * (speed)^2 * (wing surface area) / 2
        double wingLiftMag = 0.5 * liftK * P * airFoilSpeedSqr * getData().getArea();
        liftForce = liftDir.scale(wingLiftMag);
        body.addForce(liftForce);
        Quaternion vehicleQI = vehicleQ.copy();
        vehicleQI.conj();
        Vec3 liftMoment = getData().getPos()
                .cross(UtilAngles.rotateVector(liftForce, vehicleQI))
                .multiply(-1, 1, 1);
        body.addMoment(liftMoment, true, true);
        // Drag = (drag coefficient) * (air density) * (speed)^2 * (drag area) / 2
        Vec3 windDir = u.normalize();
        float dragK = getDragK(aoa) * speedScaleSqr;
        double dragMag = 0.5 * dragK * P * airFoilSpeedSqr * getData().getArea() * getData().getZeroLiftDrag();
        dragForce = windDir.scale(-dragMag);
        body.addDragForce(dragForce);
        Vec3 dragMoment = getData().getPos()
                .cross(UtilAngles.rotateVector(dragForce, vehicleQI))
                .multiply(-1, 1, 1);
        body.addMoment(dragMoment, false, true);
    }

    @Override
    protected boolean canCalcPhysics(PhysicsBody body) {
        if (!super.canCalcPhysics(body)) return false;
        return !body.isArcadeMode();
    }

    @Override
    public Vec3 getDragForce() {
        return dragForce;
    }

    @Override
    public Vec3 getLiftForce() {
        return liftForce;
    }

    @Override
    public float getAOA() {
        return aoa;
    }

    protected float getLiftK(float aoa) {
        if (getWindTunnel() != null) {
            CompoundTag value = getWindTunnel().getOverrideValue("lift_coefficient");
            if (value != null) {
                double AOA = value.getDouble("aoa");
                double liftC = value.getDouble("liftC");
                if (Math.abs(aoa - AOA) < 0.001) return (float) liftC;
            }
        }
        return getData().getLiftKGraph().getLerpFloat(aoa);
    }

    protected float getDragK(float aoa) {
        if (getWindTunnel() != null) {
            CompoundTag value = getWindTunnel().getOverrideValue("drag_coefficient");
            if (value != null) {
                double AOA = value.getDouble("aoa");
                double dragC = value.getDouble("dragC");
                if (aoa == AOA) return (float) dragC;
            }
        }
        return getData().getDragGraph().getLerpFloat(aoa);
    }

    protected double getAirDensity(PhysicsBody body) {
        return body.getAirDensity();
    }

    public static float calcAOA(Vec3 u, Vec3 wingNormal) {
        return (float) UtilGeometry.angleBetweenVecPlaneDegrees(u, wingNormal);
    }

    public static float getAOAChangeRate(PhysicsBody body) {
        if (body.isTestMode()) return 1;
        return DSCPhyCons.AOA_CHANGE_RATE;
    }
}
