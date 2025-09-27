package com.onewhohears.dscombat.init.fabric;

import com.onewhohears.dscombat.DSCombatMod;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ModItemsImpl {

    public static CreativeModeTab createTab(String name, Supplier<RegistrySupplier<? extends Item>> displayItem) {
        return FabricItemGroupBuilder
                .create(ResourceLocation.tryBuild(DSCombatMod.MODID, name))
                .icon(() -> new ItemStack(displayItem.get().get()))
                .build();
    }

}
