package com.onewhohears.dscombat.data.vehicle.physics;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilParse;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class LiftSurfaceInstance extends PhysicsComponentInstance<LiftSurfaceData> {

    private float aoa = 0;

    public LiftSurfaceInstance(LiftSurfaceData data) {
        super(data);
    }

    @Override
    protected void calcPhysics(EntityVehicle vehicle) {
        Quaternion vehicleQ;
        if (getData().isIgnoreRoll()) {
            vehicleQ = new Quaternion(0, 0, 0, 1);
            vehicleQ.mul(Vector3f.XP.rotationDegrees(vehicle.getXRot()));
            vehicleQ.mul(Vector3f.YP.rotationDegrees(vehicle.getYRot()));
        } else {
            vehicleQ = vehicle.getQBySide().copy();
        }
        Quaternion surfaceQ = vehicleQ.copy();
        Vector3f rotation = getData().getRotation();
        if (rotation.x() != 0) surfaceQ.mul(Vector3f.XN.rotationDegrees(rotation.x()));
        if (rotation.y() != 0) surfaceQ.mul(Vector3f.YP.rotationDegrees(rotation.y()));
        if (rotation.z() != 0) surfaceQ.mul(Vector3f.ZP.rotationDegrees(rotation.z()));
        float rotate = getData().getInputType().getRotationFromInput(getData(), vehicle);
        if (rotate != 0) surfaceQ.mul(Vector3f.XN.rotationDegrees(rotate));
        Vec3 u = vehicle.getDeltaMovement();
        Vec3 pitchAxis = UtilAngles.getPitchAxis(surfaceQ);
        Vec3 liftDir = u.cross(pitchAxis).normalize();
        Vec3 airFoilAxes = UtilAngles.getRollAxis(surfaceQ);
        float airFoilSpeedSqr = (float) UtilGeometry.vecCompByNormAxis(u, airFoilAxes).lengthSqr() * 400; // m/s
        float goalAOA;
        Vec3 wingNormal = UtilAngles.getYawAxis(surfaceQ).scale(-1);
        if (/*vehicle.isOnGround() || */UtilGeometry.isZero(u)) {
            goalAOA = 0;
        } else {
            goalAOA = calcAOA(u, wingNormal, pitchAxis);
        }
        // change in AOA shouldn't be instant
        aoa = Mth.lerp(DSCPhyCons.AOA_CHANGE_RATE, aoa, goalAOA);
        // find liftK
        float speedScaleSqr = (float) (1 / vehicle.getHorizontalSpeedScale() / vehicle.getHorizontalSpeedScale());
        float liftK = getData().getLiftKGraph().getLerpFloat(aoa) * speedScaleSqr;
        double P = vehicle.getFluidDensity();
        // Lift = (angle of attack coefficient) * (air density) * (speed)^2 * (wing surface area) / 2
        double wingLiftMag = 0.5 * liftK * P * airFoilSpeedSqr * getData().getArea();
        Vec3 liftForce = liftDir.scale(wingLiftMag);
        vehicle.addForce(liftForce);
        Quaternion vehicleQI = vehicleQ.copy();
        vehicleQI.conj();
        Vec3 liftMoment = getData().getPos()
                .cross(UtilAngles.rotateVector(liftForce, vehicleQI))
                .multiply(-1, 1, 1);
        vehicle.addMoment(liftMoment, false, true);
        // Drag = (drag coefficient) * (air density) * (speed)^2 * (drag area) / 2
        Vec3 windDir = u.normalize();
        float dragK = getData().getDragGraph().getLerpFloat(aoa) * speedScaleSqr;
        double dragMag = 0.5 * dragK * P * airFoilSpeedSqr * getData().getArea() * getData().getZeroLiftDrag();
        Vec3 dragForce = windDir.scale(-dragMag);
        vehicle.addForce(dragForce);
        Vec3 dragMoment = getData().getPos()
                .cross(UtilAngles.rotateVector(dragForce, vehicleQI))
                .multiply(-1, 1, 1);
        vehicle.addMoment(dragMoment, false, true);

        vehicle.debug("LIFT SURFACE = "+getData().getHitbox());
        //vehicle.debug("wingNormal = "+UtilParse.prettyVec3(wingNormal, 2));
        //vehicle.debug("airFoilAxes = "+UtilParse.prettyVec3(airFoilAxes, 2));
        vehicle.debug("aoa "+aoa+" liftK "+liftK+" rotate "+rotate);
        vehicle.debug("liftForce = "+UtilParse.prettyVec3(liftForce, 2));
        vehicle.debug("liftMoment = "+UtilParse.prettyVec3(liftMoment, 2));
        vehicle.debug("dragForce = "+UtilParse.prettyVec3(dragForce, 2));
        vehicle.debug("dragMoment = "+UtilParse.prettyVec3(dragMoment, 2));
    }

    public static float calcAOA(Vec3 u, Vec3 wingNormal, Vec3 pitchAxis) {
        return (float) UtilGeometry.angleBetweenVecPlaneDegrees(u, wingNormal);
        //float dot = (float) u.dot(airFoilAxes);
        //float dot = (float) u.dot(wingNormal);
        //return Mth.abs((float) UtilGeometry.angleBetweenVecPlaneDegrees(u, wingNormal)) * Mth.sign(dot);
        //return (float) UtilGeometry.angleBetweenVecPlaneDegrees(u, wingNormal) * Mth.sign(dot);
        //float dot = (float) wingNormal.cross(u).dot(pitchAxis);
        //return (float) (-UtilGeometry.angleBetweenVecPlaneDegrees(u, wingNormal) * Mth.sign(dot));
    }
}
