package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.entity.aircraft.parts.EntitySeat;

import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonForgeEvents {
	
	public CommonForgeEvents() {}
	
	@SubscribeEvent
	public void playerTickEvent(TickEvent.PlayerTickEvent event) {
		// TODO change player bounding box so camera entity can't attack player not working
		//if(event.phase != TickEvent.Phase.END) return;
		final var player = event.player;
		System.out.println("server side player "+player);
		if (player == null) return;
		System.out.println("server side vehicle "+player.getVehicle());
		if (!(player.getVehicle() instanceof EntitySeat seat)) return;
		//EntityDimensions dim = new EntityDimensions(player.getBbWidth(), 0.5f, true);
		//player.setBoundingBox(dim.makeBoundingBox(player.position()));
		System.out.println("server side player hitbox");
		double x = player.getX();
		double y = player.getY();
		double z = player.getZ();
		double w = player.getBbWidth()/2;
		player.setBoundingBox(new AABB(x+w, y, z+w, x-w, y+0.5, z-w));
	}
	
}
