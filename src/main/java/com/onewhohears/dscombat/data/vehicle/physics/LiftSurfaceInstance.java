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
        Quaternion q;
        if (getData().isIgnoreRoll()) q = new Quaternion(0, 0, 0, 1);
        else q = vehicle.getQBySide().copy();
        Vector3f rotation = getData().getRotation();
        if (rotation.x() != 0) q.mul(Vector3f.XN.rotationDegrees(rotation.x()));
        if (rotation.y() != 0) q.mul(Vector3f.YP.rotationDegrees(rotation.y()));
        if (rotation.z() != 0) q.mul(Vector3f.ZP.rotationDegrees(rotation.z()));
        float rotate = getData().getInputType().getRotationFromInput(getData(), vehicle);
        if (rotate != 0) q.mul(Vector3f.XN.rotationDegrees(rotate));
        Vec3 u = vehicle.getDeltaMovement();
        Vec3 pitchAxis = UtilAngles.getPitchAxis(q);
        Vec3 liftDir = u.cross(pitchAxis).normalize();
        Vec3 airFoilAxes = UtilAngles.getRollAxis(q);
        System.out.println("airFoilAxes = "+UtilParse.prettyVec3(airFoilAxes, 2));
        float airFoilSpeedSqr = (float) UtilGeometry.vecCompByNormAxis(u, airFoilAxes).lengthSqr();
        float goalAOA;
        if (/*vehicle.isOnGround() || */UtilGeometry.isZero(u)) {
            goalAOA = 0;
        } else {
            Vec3 wingNormal = UtilAngles.getYawAxis(q).scale(-1);
            goalAOA = (float) UtilGeometry.angleBetweenVecPlaneDegrees(u, wingNormal);
        }
        // change in AOA shouldn't be instant
        aoa = Mth.lerp(DSCPhyCons.AOA_CHANGE_RATE, aoa, goalAOA);
        // find liftK
        float speedScaleSqr = (float) (1 / vehicle.getHorizontalSpeedScale() / vehicle.getHorizontalSpeedScale() * 400);
        float liftK = getData().getLiftKGraph().getLerpFloat(aoa) * speedScaleSqr;
        // Lift = (angle of attack coefficient) * (air density) * (speed)^2 * (wing surface area) / 2
        double wingLiftMag = 0.5 * liftK * vehicle.getFluidDensity() * airFoilSpeedSqr * getData().getArea();
        Vec3 liftForce = liftDir.scale(wingLiftMag);
        vehicle.addForce(liftForce);
        Vec3 liftMoment = getData().getPos().cross(liftForce);
        vehicle.addMoment(liftMoment, false);
        System.out.println("lift surface "+getData().getHitbox()+" aoa "+aoa+" liftK "+liftK+" rotate "+rotate);
        System.out.println("liftForce = "+UtilParse.prettyVec3(liftForce, 2));
        System.out.println("liftMoment = "+UtilParse.prettyVec3(liftMoment, 2));
    }
}
