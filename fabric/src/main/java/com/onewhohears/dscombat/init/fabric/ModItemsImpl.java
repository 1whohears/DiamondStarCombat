package com.onewhohears.dscombat.init.fabric;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ModItemsImpl {

    public static CreativeModeTab createTab(String name, Supplier<RegistrySupplier<? extends Item>> displayItem) {
        /*return new CreativeModeTab(0, "items") {
            @Override
            public @NotNull ItemStack makeIcon() {
                return new ItemStack(displayItem.get().get());
            }
        };*/
        return null;
    }

}
