package com.onewhohears.dscombat.data.weapon;

import com.google.gson.JsonObject;
import net.minecraft.world.phys.Vec3;
import com.onewhohears.onewholibs.util.UtilParse;

import java.util.ArrayList;
import java.util.List;

/**
 * Данные о дополнительном оружии с позицией установки
 */
public class ExtraWeaponData {
    
    public final String name;
    public final String weaponId;
    public final Vec3 pos;
    /** List of other extra weapon names that should fire simultaneously with this one */
    public final List<String> linkedWeapons;
    
    public ExtraWeaponData(String name, String weaponId, Vec3 pos) {
        this(name, weaponId, pos, new ArrayList<>());
    }
    
    public ExtraWeaponData(String name, String weaponId, Vec3 pos, List<String> linkedWeapons) {
        this.name = name;
        this.weaponId = weaponId;
        this.pos = pos;
        this.linkedWeapons = linkedWeapons;
    }
    
    public ExtraWeaponData(JsonObject json) {
        this.name = UtilParse.getStringSafe(json, "name", "");
        this.weaponId = json.get("weapon").getAsString();
        this.pos = UtilParse.readVec3(json, "pos");
        this.linkedWeapons = new ArrayList<>();
        if (json.has("linked_weapons")) {
            com.google.gson.JsonArray arr = json.getAsJsonArray("linked_weapons");
            for (int i = 0; i < arr.size(); i++) {
                this.linkedWeapons.add(arr.get(i).getAsString());
            }
        }
    }
    
    public String getName() {
        return name;
    }
    
    public String getWeaponId() {
        return weaponId;
    }
    
    public Vec3 getPos() {
        return pos;
    }
    
    public List<String> getLinkedWeapons() {
        return linkedWeapons;
    }
    
    public boolean hasLinkedWeapons() {
        return !linkedWeapons.isEmpty();
    }
}
