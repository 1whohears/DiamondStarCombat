package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.weapon.NonTickingMissileManager;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE)
public final class CommonForgeEvents {
	
	/*@SubscribeEvent
	public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
		//if (event.side != LogicalSide.SERVER) return;
		if (event.getPhase() != EventPriority.NORMAL) return;
		if (event.phase != Phase.END) return;
		final var player = event.player;
		if (player == null) return;
		if (!(player.getVehicle() instanceof EntitySeat seat)) return;
		System.out.println(player);
		double x = player.getX();
		double y = player.getY();
		double z = player.getZ();
		double w = player.getBbWidth()/2;
		player.setBoundingBox(new AABB(x+w, y+0.5d, z+w, x-w, y, z-w)); 
	}*/
	
	@SubscribeEvent
	public static void serverTickEvent(TickEvent.ServerTickEvent event) {
		if (event.phase != Phase.END) return;
		if (event.getPhase() != EventPriority.NORMAL) return;
		NonTickingMissileManager.serverTick(event.getServer());
	}
	
}
