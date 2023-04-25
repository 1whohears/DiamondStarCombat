package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.command.MissileCommand;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID)
public final class ModEvents {
	
	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		new MissileCommand(event.getDispatcher());
		ConfigCommand.register(event.getDispatcher());
		// IDEA 3 set position guided missile position with command
	}
	
}
