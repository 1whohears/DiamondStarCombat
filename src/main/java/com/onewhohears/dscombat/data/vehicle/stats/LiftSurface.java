package com.onewhohears.dscombat.data.vehicle.stats;

import com.google.gson.JsonObject;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.data.graph.AoaLiftKGraph;
import com.onewhohears.dscombat.data.graph.StatGraphs;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilParse;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiFunction;
import java.util.function.Function;

public class LiftSurface extends PhysicsComponent {

    private final boolean static_rotation;
    private final float aoa_input_bias;
    private final double area;
    private final Vec3 pos;
    private final Vector3f rotation;
    private final AoaInputType input_type;
    private final String lift_k_graph_key;
    private AoaLiftKGraph lift_k_graph;

    public LiftSurface(JsonObject json) {
        super(json);
        static_rotation = UtilParse.getBooleanSafe(json, "static_rotation", false);
        aoa_input_bias = UtilParse.getFloatSafe(json, "aoa_input_bias", 10);
        area = UtilParse.getFloatSafe(json, "area", 10);
        pos = UtilParse.readVec3(json, "pos");
        rotation = UtilParse.readVec3f(json, "rotation");
        input_type = UtilParse.getEnumSafe(json, "input_type", AoaInputType.class);
        lift_k_graph_key = UtilParse.getStringSafe(json, "lift_k_graph", "fuselage");
    }

    @Override
    protected void calcPhysics(EntityVehicle vehicle) {
        Quaternion q;
        if (isStaticRotation()) q = new Quaternion(0, 0, 0, 1);
        else q = vehicle.getQBySide().copy();
        if (rotation.x() != 0) q.mul(Vector3f.XP.rotationDegrees(rotation.x()));
        if (rotation.y() != 0) q.mul(Vector3f.YP.rotationDegrees(rotation.y()));
        if (rotation.z() != 0) q.mul(Vector3f.ZP.rotationDegrees(rotation.z()));
        Vec3 u = vehicle.getDeltaMovement();
        Vec3 pitchAxis = UtilAngles.getPitchAxis(q);
        Vec3 liftDir = u.cross(pitchAxis).normalize();
        Vec3 airFoilAxes = UtilAngles.getRollAxis(q);
        float airFoilSpeedSqr = (float) UtilGeometry.vecCompByNormAxis(u, airFoilAxes).lengthSqr();
        float goalAOA;
        if (vehicle.isOnGround() || UtilGeometry.isZero(u)) {
            goalAOA = 0;
        } else {
            Vec3 wingNormal = UtilAngles.getYawAxis(q).scale(-1);
            goalAOA = (float) UtilGeometry.angleBetweenVecPlaneDegrees(u, wingNormal);
        }
        // change aoa from inputs

        // change in AOA shouldn't be instant
        //float aoa = Mth.lerp(DSCPhyCons.AOA_CHANGE_RATE, aoa, goalAOA);
        float aoa = goalAOA;
        // find liftK
        float speedScaleSqr = (float) (1 / vehicle.getHorizontalSpeedScale() / vehicle.getHorizontalSpeedScale() * 400);
        float liftK = getLiftKGraph().getLerpFloat(aoa) * speedScaleSqr;
        // Lift = (angle of attack coefficient) * (air density) * (speed)^2 * (wing surface area) / 2
        double wingLiftMag = 0.5 * liftK * vehicle.getFluidDensity() * airFoilSpeedSqr * getArea();
        Vec3 liftForce = liftDir.scale(wingLiftMag);
        vehicle.addForce(liftForce);
    }

    public AoaLiftKGraph getLiftKGraph() {
        if (lift_k_graph == null)
            lift_k_graph = StatGraphs.get().getAoaLiftKGraph(lift_k_graph_key);
        return lift_k_graph;
    }

    public double getArea() {
        return area;
    }

    public Vec3 getPos() {
        return pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public boolean isStaticRotation() {
        return static_rotation;
    }

    public float getAoaInputBias() {
        return aoa_input_bias;
    }

    public enum AoaInputType {
        NONE((surface,vehicle) -> 0f),
        LEFT_FLAP((surface,vehicle) -> {
            if (vehicle.isFlapsDown()) return surface.aoa_input_bias;
            return vehicle.inputs.roll * surface.aoa_input_bias;
        }),
        RIGHT_FLAP((surface,vehicle) -> {
            if (vehicle.isFlapsDown()) return surface.aoa_input_bias;
            return -vehicle.inputs.roll * surface.aoa_input_bias;
        }),
        ELEVATOR((surface,vehicle) -> vehicle.inputs.pitch * surface.aoa_input_bias),
        STABILIZER((surface,vehicle) -> vehicle.inputs.yaw * surface.aoa_input_bias);
        private final BiFunction<LiftSurface, EntityVehicle, Float> input;
        AoaInputType(BiFunction<LiftSurface, EntityVehicle, Float> input) {
            this.input = input;
        }
        public float getAoaChangeFromInput(LiftSurface liftSurface, EntityVehicle vehicle) {
            return input.apply(liftSurface, vehicle);
        }
    }
}
