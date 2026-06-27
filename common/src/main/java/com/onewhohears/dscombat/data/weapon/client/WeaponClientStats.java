package com.onewhohears.dscombat.data.weapon.client;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.obj.ObjWeaponModel;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.data.jsonpreset.CustomAnimStats;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class WeaponClientStats<E extends EntityWeapon<?>> extends CustomAnimStats<ObjWeaponModel<E>, E> {

    public static final JsonPresetType STANDARD = new JsonPresetType("standard", WeaponClientStats::new) {};

    // Маппинг имён контекстов в ItemDisplayContext
    private static final Map<String, ItemDisplayContext> CONTEXT_NAMES = new java.util.HashMap<>();
    static {
        CONTEXT_NAMES.put("third_person_right_hand", ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
        CONTEXT_NAMES.put("third_person_left_hand",  ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
        CONTEXT_NAMES.put("first_person_right_hand",  ItemDisplayContext.FIRST_PERSON_RIGHT_HAND);
        CONTEXT_NAMES.put("first_person_left_hand",   ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
        CONTEXT_NAMES.put("gui",                      ItemDisplayContext.GUI);
        CONTEXT_NAMES.put("ground",                   ItemDisplayContext.GROUND);
        CONTEXT_NAMES.put("fixed",                    ItemDisplayContext.FIXED);
        CONTEXT_NAMES.put("head",                     ItemDisplayContext.HEAD);
    }

    private Map<ItemDisplayContext, ObjEntityModels.ModelOverrides> itemTransforms;

    public WeaponClientStats(ResourceLocation key, JsonObject json) {
        super(key, json);
    }

    /** Возвращает per-context трансформ из "item_transforms", или null если не задан. */
    @Nullable
    public ObjEntityModels.ModelOverrides getItemTransform(ItemDisplayContext ctx) {
        if (itemTransforms == null) {
            itemTransforms = new EnumMap<>(ItemDisplayContext.class);
            JsonObject json = getJsonData();
            if (json.has("item_transforms")) {
                JsonObject transforms = json.getAsJsonObject("item_transforms");
                for (var entry : transforms.entrySet()) {
                    ItemDisplayContext context = CONTEXT_NAMES.get(entry.getKey());
                    if (context != null && entry.getValue().isJsonObject()) {
                        itemTransforms.put(context, new ObjEntityModels.ModelOverrides(
                                entry.getValue().getAsJsonObject()));
                    }
                }
            }
        }
        return itemTransforms.get(ctx);
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
