package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.onewholibs.util.UtilMCText;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModCMTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(
            DSCombatMod.MODID, Registries.CREATIVE_MODE_TAB);

    public static final Map<ResourceKey<CreativeModeTab>, List<Supplier<? extends Item>>> CREATIVE_TAB_MAP = new HashMap<>();

    public static RegistrySupplier<CreativeModeTab> createTab(String name, Supplier<RegistrySupplier<? extends Item>> displayItem) {
        CreativeModeTab tab = CreativeTabRegistry.create(
                UtilMCText.translatable("itemGroup.dscombat."+name),
                () -> new ItemStack(displayItem.get().get()));
        return CREATIVE_TABS.register(name, () -> tab);
    }

    public static final RegistrySupplier<CreativeModeTab> DSC_ITEMS = createTab("items", () -> ModItems.WRENCH);
    public static final RegistrySupplier<CreativeModeTab> PARTS = createTab("parts", () -> ModItems.C12_ENGINE);
    public static final RegistrySupplier<CreativeModeTab> WEAPONS = createTab("weapons", () -> ModItems.AIM9X);
    public static final RegistrySupplier<CreativeModeTab> WEAPON_PARTS = createTab("weapon_parts", () -> ModItems.CIWS);
    public static final RegistrySupplier<CreativeModeTab> VEHICLES = createTab("vehicle", () -> ModItems.JAVI_PLANE);

    public static void register() {
        CREATIVE_TABS.register();
    }

}
