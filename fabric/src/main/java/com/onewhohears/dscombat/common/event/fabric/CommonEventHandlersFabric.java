package com.onewhohears.dscombat.common.event.fabric;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.command.argument.VehiclePresetArgument;
import com.onewhohears.dscombat.command.argument.WeaponArgument;
import com.onewhohears.dscombat.common.event.CommonEventHandlers;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.fml.event.config.ModConfigEvents;
import net.minecraftforge.fml.config.ModConfig;

public class CommonEventHandlersFabric {

    public static void init() {
        ModConfigEvents.loading(DSCombatMod.MODID).register(CommonEventHandlersFabric::onConfigLoad);
        ModConfigEvents.reloading(DSCombatMod.MODID).register(CommonEventHandlersFabric::onConfigReload);
        registerArgumentTypes();
    }

    public static void onConfigReload(ModConfig modConfig) {
        CommonEventHandlers.onReadConfig(modConfig);
    }

    public static void onConfigLoad(ModConfig modConfig) {
        CommonEventHandlers.onReadConfig(modConfig);
    }

    public static void registerArgumentTypes() {
        ArgumentTypeRegistry.registerArgumentType(ResourceLocation.tryBuild(DSCombatMod.MODID, "weapon_preset"),
                WeaponArgument.class, SingletonArgumentInfo.contextFree(WeaponArgument::weapon));
        ArgumentTypeRegistry.registerArgumentType(ResourceLocation.tryBuild(DSCombatMod.MODID, "vehicle_preset"),
                VehiclePresetArgument.class, SingletonArgumentInfo.contextFree(VehiclePresetArgument::vehiclePreset));
    }

}
