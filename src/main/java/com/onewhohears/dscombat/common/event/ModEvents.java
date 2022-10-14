package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.command.TestWeaponCommand;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.MOD)
public class ModEvents {
	
	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		//event.put(ModEntities.BASIC_PLANE, EntityBasicPlane);
	}
	
	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		new TestWeaponCommand(event.getDispatcher());
		ConfigCommand.register(event.getDispatcher());
	}
	
}
