package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.command.*;
import com.onewhohears.dscombat.data.villager.DSCVillagerTrades;
import com.onewhohears.dscombat.init.ModVillagers;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID)
public final class ModEvents {
	
	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		new MissileCommand(event.getDispatcher());
		new VehicleCommand(event.getDispatcher());
		new DSCParticleDebugCommand(event.getDispatcher());
		new DebugSlotPosCommand(event.getDispatcher());
		new DebugHitboxPosCommand(event.getDispatcher());
		new TargetModeCommand(event.getDispatcher());
		ConfigCommand.register(event.getDispatcher());
		// IDEA 3.2 set position guided missile position with command
	}
	
	@SubscribeEvent
	public static void customVillagerTrades(VillagerTradesEvent event) {
		if (event.getType() == ModVillagers.WEAPONS_ENGINEER.get()) DSCVillagerTrades.putWeaponEngineerTrades(event.getTrades());
		else if (event.getType() == ModVillagers.AIRCRAFT_ENGINEER.get()) DSCVillagerTrades.putAircraftEngineerTrades(event.getTrades());
	}
	
}
