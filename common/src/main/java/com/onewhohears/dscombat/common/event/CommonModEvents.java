package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.DependencySafety;
import com.onewhohears.dscombat.common.network.VehicleSyncAction;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.data.vehicle.physics.PhysicsComponentData;
import com.onewhohears.dscombat.data.weapon.RadarTargetTypes;

import com.onewhohears.dscombat.init.ModVillagers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.MOD)
public final class CommonModEvents {
	
	@SubscribeEvent
	public static void loadModConfigEvent(ModConfigEvent.Loading event) {
		if (event.getConfig().getType() == Type.COMMON) {
			RadarTargetTypes.get().readConfig();
		}
	}
	
	@SubscribeEvent
	public static void reloadModConfigEvent(ModConfigEvent.Reloading event) {
		if (event.getConfig().getType() == Type.COMMON) {
			RadarTargetTypes.get().readConfig();
		}
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		PhysicsComponentData.register();
		VehicleSyncAction.register();
		PacketHandler.register();
		DSCGameRules.registerAll();
		DependencySafety.fmlCommonSetup();
		event.enqueueWork(ModVillagers::registerPOIs);
	}
	
}
