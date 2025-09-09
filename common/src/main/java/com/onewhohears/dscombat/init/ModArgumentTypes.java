package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.command.argument.VehiclePresetArgument;
import com.onewhohears.dscombat.command.argument.WeaponArgument;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModArgumentTypes {
	
	public static final DeferredRegister<ArgumentTypeInfo<?,?>> ARGUMENT_TYPES = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, DSCombatMod.MODID);
	
	public static void register(IEventBus eventBus) {
		ARGUMENT_TYPES.register(eventBus);
	}
    
	public static final RegistryObject<ArgumentTypeInfo<?,?>> WEAPON_PRESET = ARGUMENT_TYPES.register("weapon_preset", 
		() -> ArgumentTypeInfos.registerByClass(WeaponArgument.class, 
				SingletonArgumentInfo.contextFree(WeaponArgument::weapon)));
	
	public static final RegistryObject<ArgumentTypeInfo<?,?>> VEHICLE_PRESET = ARGUMENT_TYPES.register("vehicle_preset", 
		() -> ArgumentTypeInfos.registerByClass(VehiclePresetArgument.class, 
				SingletonArgumentInfo.contextFree(VehiclePresetArgument::vehiclePreset)));
	
}
