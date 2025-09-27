package com.onewhohears.dscombat.data.parts.client;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.client.model.obj.ObjTurretModel;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;

public class TurretClientStats extends PartClientStats<EntityTurret> {
    private final boolean rot_all_yaw;
    public TurretClientStats(ResourceLocation key, JsonObject json) {
        super(key, json);
        rot_all_yaw = UtilParse.getBooleanSafe(json, "rot_all_yaw", true);
    }
    @Override
    protected ObjPartModel<EntityTurret> createNotHardCodedModel() {
        return new ObjTurretModel<>(getModelId(), getRotAllYaw(), getCustomAnims(), getKeyframeAnimIds());
    }
    @Override
    public JsonPresetType getType() {
        return PartClientType.TURRET;
    }
    public boolean getRotAllYaw() {
        return rot_all_yaw;
    }
}
