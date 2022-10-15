package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.command.MissileCommand;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID)
public class ModEvents {
	
	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		new MissileCommand(event.getDispatcher());
		ConfigCommand.register(event.getDispatcher());
		System.out.println("REGISTERING COMMANDS");
	}
	
}
