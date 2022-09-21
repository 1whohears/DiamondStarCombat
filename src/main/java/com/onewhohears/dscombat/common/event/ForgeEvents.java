package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.DSCombatMod;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.DEDICATED_SERVER)
public class ForgeEvents {
	
	@SubscribeEvent
	public void playerTick(PlayerTickEvent event) {
		// TODO change player bounding box so camera entity can't attack player
		//event.player.setBoundingBox(new AABB());
	}
	
}
