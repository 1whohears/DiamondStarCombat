package com.onewhohears.dscombat.fabric;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.event.fabric.ClientEventHandlersFabric;
import com.onewhohears.dscombat.common.event.fabric.CommonEventHandlersFabric;
import com.onewhohears.dscombat.init.ModCMTabs;
import com.onewhohears.dscombat.item.FillableItemCategory;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

public class DSCombatModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DSCombatMod.init();
        CommonEventHandlersFabric.init();
        if (Platform.getEnvironment() == Env.CLIENT) {
            DSCombatMod.clientInit();
            ClientEventHandlersFabric.init();
        }
        itemGroups();
        ForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.CLIENT, Config.clientSpec);
        ForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.COMMON, Config.commonSpec);
        ForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.SERVER, Config.serverSpec);
    }

    public static void itemGroups() {
        ModCMTabs.CREATIVE_TAB_MAP.forEach((tab, items) -> {
            ItemGroupEvents.modifyEntriesEvent(tab).register(entries -> {
                items.forEach(item -> {
                    if (item.get() instanceof FillableItemCategory fill) {
                        List<ItemStack> stacks = new ArrayList<>();
                        fill.fillItemCategory(stacks);
                        stacks.forEach(entries::accept);
                    } else {
                        entries.accept(item.get());
                    }
                });
            });
        });
    }
}
