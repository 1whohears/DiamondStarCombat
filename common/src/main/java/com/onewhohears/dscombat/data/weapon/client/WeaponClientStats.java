package com.onewhohears.dscombat.data.weapon.client;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.obj.ObjWeaponModel;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.onewholibs.data.jsonpreset.CustomAnimStats;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class WeaponClientStats<E extends EntityWeapon<?>> extends CustomAnimStats<ObjWeaponModel<E>, E> {

    public static final JsonPresetType STANDARD = new JsonPresetType("standard", WeaponClientStats::new) {};

    public WeaponClientStats(ResourceLocation key, JsonObject json) {
        super(key, json);
    }

    @Override
    protected ObjWeaponModel<E> createModel() {
        return new ObjWeaponModel<>(getModelId(), getCustomAnims(), getKeyframeAnimIds());
    }

    @Override
    public JsonPresetType getType() {
        return STANDARD;
    }

    @Override
    public @Nullable JsonPresetInstance<?> createPresetInstance() {
        return null;
    }

    public static class Builder extends CustomAnimStatsBuilder<WeaponClientStats.Builder> {
        public static Builder createStandard(String name) {
            return new Builder(name, STANDARD);
        }
        protected Builder(String name, JsonPresetType type) {
            this(DSCombatMod.MODID, name, type);
        }
        protected Builder(String namespace, String name, JsonPresetType type) {
            super(namespace, name, type);
        }
    }
}
