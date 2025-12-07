package com.onewhohears.dscombat.item;

import dev.architectury.impl.ItemPropertiesExtensionImpl;
import dev.architectury.registry.registries.DeferredSupplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.onewhohears.dscombat.init.ModCMTabs.CREATIVE_TAB_MAP;

public interface FillableItemCategory {
    void fillItemCategory(@NotNull List<ItemStack> items);
    /**
     * THIS METHOD MUST BE MANUALLY IMPLEMENTED IF CHILD CLASS IS ITEM
     */
    Item asItem();

    static void onInit(FillableItemCategory item, Item.Properties props) {
        List<ItemStack> list = new ArrayList<>();
        item.fillItemCategory(list);
        @Nullable DeferredSupplier<CreativeModeTab> tabSupplier = ((ItemPropertiesExtensionImpl)props).arch$getTabSupplier();
        if (tabSupplier == null) {
            return;
        }
        ResourceKey<CreativeModeTab> tab = tabSupplier.getKey();
        if (CREATIVE_TAB_MAP.containsKey(tab)) {
            CREATIVE_TAB_MAP.get(tab).add(item::asItem);
            return;
        }
        List<Supplier<? extends Item>> itemList = new ArrayList<>();
        itemList.add(item::asItem);
        CREATIVE_TAB_MAP.put(tab, itemList);
    }
}
