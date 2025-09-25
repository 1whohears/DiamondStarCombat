package com.onewhohears.dscombat.init.forge;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ModItemsImpl {

    public static CreativeModeTab createTab(String name, Supplier<RegistrySupplier<? extends Item>> displayItem) {
        return new CreativeModeTab(-1, name) {
            @Override
            public @NotNull ItemStack makeIcon() {
                return new ItemStack(displayItem.get().get());
            }
        };
    }

}
