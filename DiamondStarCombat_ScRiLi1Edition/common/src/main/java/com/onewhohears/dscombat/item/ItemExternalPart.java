package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.client.PartAssets;
import com.onewhohears.dscombat.data.parts.client.PartClientStats;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.item.ObjModelItem;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.NotNull;

public class ItemExternalPart extends ItemPart implements ObjModelItem {

    @ExpectPlatform
    public static ItemExternalPart create(int stackSize, String defaultPresetId) {
        throw new AssertionError();
    }

    public ItemExternalPart(int stackSize, @NotNull String defaultPresetId) {
        super(stackSize, defaultPresetId);
    }

    @Override
    public @NotNull String getObjModelId(@NotNull String preset) {
        PartClientStats<?> pcs = PartAssets.get().get(preset);
        if (pcs == null) return "";
        return pcs.getModel().modelId;
    }

    @Override
    public ObjEntityModels.@NotNull ModelOverrides getItemModelOverrides(@NotNull String preset) {
        PartClientStats<?> pcs = PartAssets.get().get(preset);
        if (pcs == null) return ObjEntityModels.NO_OVERRIDES;
        return pcs.getItemModelOverrides();
    }
}
