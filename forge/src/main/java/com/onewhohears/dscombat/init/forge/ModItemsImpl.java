package com.onewhohears.dscombat.init.forge;

import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.onewholibs.util.UtilMCText;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ModItemsImpl {

    public static ResourceKey<CreativeModeTab> createTab(String name, Supplier<RegistrySupplier<? extends Item>> displayItem) {
        CreativeModeTab tab = CreativeModeTab.builder()
                .title(UtilMCText.translatable("itemGroup.dscombat."+name))
                .icon(() -> new ItemStack(displayItem.get().get()))
                .build();
        RegistrySupplier<CreativeModeTab> tabSup = ModItems.CREATIVE_TABS.register(name, () -> tab);
        return tabSup.getKey();
    }

}
