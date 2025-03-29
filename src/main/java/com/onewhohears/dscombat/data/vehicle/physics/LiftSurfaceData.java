package com.onewhohears.dscombat.data.vehicle.physics;

import com.google.gson.JsonObject;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.data.graph.AoaLiftKGraph;
import com.onewhohears.dscombat.data.graph.FloatFloatGraph;
import com.onewhohears.dscombat.data.graph.StatGraphs;
import com.onewhohears.dscombat.entity.PhysicsBody;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiFunction;

public class LiftSurfaceData extends PhysicsComponentData {

    public static JsonObject createJsonData(String hitbox, boolean ignore_roll, float input_rotation_max, double area,
                                            Vec3 pos, float xRot, float yRot, float zRot, InputType input_type,
                                            String lift_k_graph, String drag_graph, double zero_lift_drag) {
        JsonObject json = new JsonObject();
        json.addProperty("id", "lift_surface");
        json.addProperty("hitbox", hitbox);
        json.addProperty("ignore_roll", ignore_roll);
        json.addProperty("input_rotation_max", input_rotation_max);
        json.addProperty("area", area);
        UtilParse.writeVec3(json, "pos", pos);
        UtilParse.writeVec3f(json, "rotation", new Vector3f(xRot, yRot, zRot));
        UtilParse.writeEnum(json, "input_type", input_type);
        json.addProperty("lift_k_graph", lift_k_graph);
        json.addProperty("drag_graph", drag_graph);
        json.addProperty("zero_lift_drag", zero_lift_drag);
        return json;
    }

    private final boolean ignore_roll;
    private final float input_rotation_max;
    private final double area, zero_lift_drag;
    private final Vec3 pos;
    private final Vector3f rotation;
    private final InputType input_type;
    private final String lift_k_graph_key;
    private final String drag_graph_key;
    private AoaLiftKGraph lift_k_graph;
    private FloatFloatGraph drag_graph;

    public LiftSurfaceData(JsonObject json) {
        super(json);
        ignore_roll = UtilParse.getBooleanSafe(json, "ignore_roll", false);
        input_rotation_max = UtilParse.getFloatSafe(json, "input_rotation_max", 4);
        area = UtilParse.getFloatSafe(json, "area", 10);
        pos = UtilParse.readVec3(json, "pos");
        rotation = UtilParse.readVec3f(json, "rotation");
        input_type = UtilParse.getEnumSafe(json, "input_type", InputType.class);
        lift_k_graph_key = UtilParse.getStringSafe(json, "lift_k_graph", "fuselage");
        zero_lift_drag = UtilParse.getFloatSafe(json, "zero_lift_drag", 0.5f);
        drag_graph_key = UtilParse.getStringSafe(json, "drag_graph", "default_drag_aoa");
    }

    @Override
    public PhysicsComponentInstance<?> createInstance() {
        return new LiftSurfaceInstance(this);
    }

    public AoaLiftKGraph getLiftKGraph() {
        if (lift_k_graph == null)
            lift_k_graph = StatGraphs.get().getAoaLiftKGraph(lift_k_graph_key);
        return lift_k_graph;
    }

    public FloatFloatGraph getDragGraph() {
        if (drag_graph == null)
            drag_graph = StatGraphs.get().getFloatFloatGraph(drag_graph_key);
        return drag_graph;
    }

    public double getArea() {
        return area;
    }

    public double getZeroLiftDrag() {
        return zero_lift_drag;
    }

    public Vec3 getPos() {
        return pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public boolean isIgnoreRoll() {
        return ignore_roll;
    }

    public float getInputRotationMax() {
        return input_rotation_max;
    }
    
    public InputType getInputType() {
        return input_type;
    }

    public enum InputType {
        NONE((surface,body) -> 0f),
        LEFT_FLAP((surface,body) -> {
            if (body.isFlapsDown()) return surface.getInputRotationMax();
            return body.getRollInput() * surface.getInputRotationMax() * 0.5f;
        }),
        RIGHT_FLAP((surface,body) -> {
            if (body.isFlapsDown()) return surface.getInputRotationMax();
            return -body.getRollInput() * surface.getInputRotationMax() * 0.5f;
        }),
        ELEVATOR((surface,body) -> -body.getPitchInput() * surface.getInputRotationMax()),
        STABILIZER((surface,body) -> body.getYawInput() * surface.getInputRotationMax());
        private final BiFunction<LiftSurfaceData, PhysicsBody, Float> input;
        InputType(BiFunction<LiftSurfaceData, PhysicsBody, Float> input) {
            this.input = input;
        }
        public float getRotationFromInput(LiftSurfaceData liftSurface, PhysicsBody body) {
            return input.apply(liftSurface, body);
        }
    }
}
