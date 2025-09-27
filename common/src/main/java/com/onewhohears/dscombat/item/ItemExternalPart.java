package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.client.PartAssets;
import com.onewhohears.dscombat.data.parts.client.PartClientStats;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.item.ObjModelItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ItemExternalPart extends ItemPart implements ObjModelItem {

    public ItemExternalPart(int stackSize, @NotNull String defaultPresetId) {
        super(stackSize, defaultPresetId);
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        ObjModelItem.super.initializeClient(consumer);
    }

    @Override
    public @NotNull String getObjModelId(@NotNull String preset) {
        PartClientStats<?> pcs = PartAssets.get().get(preset);
        if (pcs == null) return "";
        return pcs.getModelId();
    }

    @Override
    public ObjEntityModels.@NotNull ModelOverrides getItemModelOverrides(@NotNull String preset) {
        PartClientStats<?> pcs = PartAssets.get().get(preset);
        if (pcs == null) return ObjEntityModels.NO_OVERRIDES;
        return pcs.getItemModelOverrides();
    }
}
