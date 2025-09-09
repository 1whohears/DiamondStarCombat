package com.onewhohears.dscombat.data.parts.client;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.client.model.obj.ObjWeaponRackModel;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class WeaponRackClientStats extends PartClientStats<EntityWeaponRack> {
    private final int maxAmmoNum;
    private final Vec3[] weapon_pos;
    public WeaponRackClientStats(ResourceLocation key, JsonObject json) {
        super(key, json);
        if (json.has("weapon_pos")) {
            weapon_pos = UtilParse.readVec3Array(json, "weapon_pos");
            maxAmmoNum = weapon_pos.length;
        } else {
            maxAmmoNum = UtilParse.getIntSafe(json, "maxAmmoNum", 0);
            weapon_pos = new Vec3[maxAmmoNum];
            float dX = UtilParse.getFloatSafe(json, "dX", 0);
            float dY = UtilParse.getFloatSafe(json, "dY", 0);
            for (int i = 0; i < maxAmmoNum; ++i) {
                float x = i % 2 == 0 ? dX : -dX;
                float y = (i/2 + 1) * dY - dY*0.5f;
                if (i+1 == maxAmmoNum && maxAmmoNum%2 == 1) x = 0;
                weapon_pos[i] = new Vec3(x, y, 0);
            }
        }
    }
    @Override
    protected ObjPartModel<EntityWeaponRack> createNotHardCodedModel() {
        return new ObjWeaponRackModel<>(getModelId(), getMaxAmmoNum(), getWeaponPositions());
    }
    @Override
    public JsonPresetType getType() {
        return PartClientType.WEAPON_RACK;
    }
    public int getMaxAmmoNum() {
        return maxAmmoNum;
    }
    public Vec3[] getWeaponPositions() {
        return weapon_pos;
    }
}
