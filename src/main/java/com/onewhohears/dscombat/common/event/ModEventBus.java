package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.DSCombatMod;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.MOD)
public class ModEventBus {
	
	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		//event.put(ModEntities.BASIC_PLANE, EntityBasicPlane);
	}
	
}
