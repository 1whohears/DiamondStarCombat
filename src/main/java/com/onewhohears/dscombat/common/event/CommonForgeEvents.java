package com.onewhohears.dscombat.common.event;

import java.util.List;

import com.onewhohears.dscombat.data.ChunkManager;
import com.onewhohears.dscombat.data.weapon.NonTickingMissileManager;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.weapon.EntityAbstractWeapon;
import com.onewhohears.dscombat.entity.weapon.EntityFlare;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class CommonForgeEvents {
	
	public CommonForgeEvents() {}
	
	@SubscribeEvent
	public void playerTickEvent(TickEvent.PlayerTickEvent event) {
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
	public void chunkUnload(ChunkEvent.Unload event) {
		if (event.getPhase() != EventPriority.NORMAL) return;
		ChunkAccess chunk = event.getChunk();
		LevelAccessor level = event.getLevel();
		AABB chunkArea = getChunkBox(chunk.getPos());
		List<EntityAbstractWeapon> list = level.getEntitiesOfClass(
				EntityAbstractWeapon.class, chunkArea);
		for (int i = 0; i < list.size(); ++i) list.get(i).discard();
		List<EntityFlare> list2 = level.getEntitiesOfClass(
				EntityFlare.class, chunkArea);
		for (int i = 0; i < list2.size(); ++i) list2.get(i).discard();
	}
	
	private static AABB getChunkBox(ChunkPos chunk) {
		return new AABB(chunk.getMaxBlockX(), -64, chunk.getMaxBlockZ(),
				chunk.getMinBlockX(), 1000, chunk.getMaxBlockZ());
	}
	
	@SubscribeEvent
	public void serverTickEvent(TickEvent.ServerTickEvent event) {
		if (event.getPhase() != EventPriority.NORMAL) return;
		//ChunkManager.serverTick(event.getServer());
		NonTickingMissileManager.serverTick(event.getServer());
	}
	
}
