package com.onewhohears.dscombat.entity.parts.hitbox;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.world.phys.Vec3;

/**
 * Data class for turret hitboxes, analogous to RotableHitboxData for vehicles.
 * Defined in turret preset JSON under the "hitboxes" array.
 */
public class TurretHitboxData {

    private final String name;
    private final Vec3 size, rel_pos;
    private final float max_health, max_armor;
    private final boolean remove_on_destroy, damage_root;
    private final int index;

    public TurretHitboxData(JsonObject json, int index) {
        name = json.get("name").getAsString();
        size = UtilParse.readVec3(json, "size");
        rel_pos = UtilParse.readVec3(json, "rel_pos");
        max_health = UtilParse.getFloatSafe(json, "max_health", 0);
        max_armor = UtilParse.getFloatSafe(json, "max_armor", 0);
        remove_on_destroy = UtilParse.getBooleanSafe(json, "remove_on_destroy", false);
        damage_root = UtilParse.getBooleanSafe(json, "damage_root", false);
        this.index = index;
    }

    public String getName() { return name; }
    public Vec3 getSize() { return size; }
    public Vec3 getRelPos() { return rel_pos; }
    public float getMaxHealth() { return max_health; }
    public float getMaxArmor() { return max_armor; }
    public boolean isRemoveOnDestroy() { return remove_on_destroy; }
    public boolean isDamageRoot() { return damage_root; }
    public int getIndex() { return index; }
}
