package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.data.weapon.NonTickingMissileManager;
import com.onewhohears.dscombat.entity.parts.EntitySeat;

import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class CommonForgeEvents {
	
	public CommonForgeEvents() {}
	
	@SubscribeEvent
	public void playerTickEvent(TickEvent.PlayerTickEvent event) {
		if (event.phase != Phase.START) return;
		if (event.getPhase() != EventPriority.NORMAL) return;
		if (event.side != LogicalSide.SERVER) return;
		final var player = event.player;
		//System.out.println("server side player "+player);
		if (player == null) return;
		//System.out.println("server side vehicle "+player.getVehicle());
		if (!(player.getVehicle() instanceof EntitySeat seat)) return;
		//System.out.println("server side player hitbox");
		double x = player.getX();
		double y = player.getY();
		double z = player.getZ();
		double w = player.getBbWidth()/2;
		player.setBoundingBox(new AABB(x+w, y+0.5d, z+w, x-w, y, z-w)); 
	}
	
	@SubscribeEvent
	public void serverTickEvent(TickEvent.ServerTickEvent event) {
		if (event.phase != Phase.END) return;
		if (event.getPhase() != EventPriority.NORMAL) return;
		//ChunkManager.serverTick(event.getServer());
		NonTickingMissileManager.serverTick(event.getServer());
	}
	
}
