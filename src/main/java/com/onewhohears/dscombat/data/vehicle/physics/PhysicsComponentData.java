package com.onewhohears.dscombat.data.vehicle.physics;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class PhysicsComponentData {

    private static Map<String, Function<JsonObject, PhysicsComponentData>> components = new HashMap<>();

    public static void register() {
        components.put("lift_surface", LiftSurfaceData::new);
    }

    @Nullable
    public static PhysicsComponentData getData(JsonObject json) {
        String id = UtilParse.getStringSafe(json, "id", "");
        if (!components.containsKey(id)) return null;
        return components.get(id).apply(json);
    }

    private final String hitbox;
    private final Vec3 pos;

    public PhysicsComponentData(JsonObject json) {
        hitbox = UtilParse.getStringSafe(json, "hitbox", " ");
        pos = UtilParse.readVec3(json, "pos");
    }

    public String getHitbox() {
        return hitbox;
    }

    public Vec3 getPos() {
        return pos;
    }

    public abstract PhysicsComponentInstance<?> createInstance();
}
