package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientDataPackSynch;
import com.onewhohears.dscombat.data.aircraft.AircraftPresets;
import com.onewhohears.dscombat.data.weapon.NonTickingMissileManager;
import com.onewhohears.dscombat.entity.parts.EntitySeat;

import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE)
public final class CommonForgeEvents {
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		if (!(event.player.getVehicle() instanceof EntitySeat seat)) return;
		double x = seat.getX(), y = seat.getY(), z = seat.getZ();
		double w = event.player.getBbWidth()/2;
		event.player.setBoundingBox(new AABB(x-w, y-w, z-w, x+w, y+w, z+w));
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void serverTickEvent(TickEvent.ServerTickEvent event) {
		if (event.phase != Phase.END) return;
		NonTickingMissileManager.serverTick(event.getServer());
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onDatapackSync(OnDatapackSyncEvent event) {
		System.out.println("DATAPACKSYNCH "+event.getPlayer());
		PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ToClientDataPackSynch());
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void serverStartedEvent(ServerStartedEvent event) {
		AircraftPresets.resetCachedPresets(event.getServer().getResourceManager(), false);
	}
	
}
