package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.command.argument.VehiclePresetArgument;
import com.onewhohears.dscombat.command.argument.WeaponArgument;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;

public class ModArgumentTypes {
	
	public static final DeferredRegister<ArgumentTypeInfo<?,?>> ARGUMENT_TYPES = DeferredRegister.create(
            DSCombatMod.MODID, Registry.COMMAND_ARGUMENT_TYPE_REGISTRY);

    public static final RegistrySupplier<ArgumentTypeInfo<?,?>> WEAPON_PRESET = ARGUMENT_TYPES.register("weapon_preset",
            () -> SingletonArgumentInfo.contextFree(WeaponArgument::weapon));
	
	public static final RegistrySupplier<ArgumentTypeInfo<?,?>> VEHICLE_PRESET = ARGUMENT_TYPES.register("vehicle_preset",
		() -> SingletonArgumentInfo.contextFree(VehiclePresetArgument::vehiclePreset));

    public static void register() {
        ARGUMENT_TYPES.register();
    }
}
