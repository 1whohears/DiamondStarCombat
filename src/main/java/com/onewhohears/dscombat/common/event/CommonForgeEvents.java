package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientDataPackSynch;
import com.onewhohears.dscombat.data.aircraft.AircraftPresets;
import com.onewhohears.dscombat.data.radar.RadarPresets;
import com.onewhohears.dscombat.data.weapon.NonTickingMissileManager;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.parts.EntitySeat;

import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.PacketTarget;

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
		PacketTarget target;
		if (event.getPlayer() == null) {
			target = PacketDistributor.ALL.noArg();
		} else {
			target = PacketDistributor.PLAYER.with(() -> event.getPlayer());
		}
		PacketHandler.INSTANCE.send(target, new ToClientDataPackSynch());
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void serverStoppingEvent(ServerStoppingEvent event) {
		AircraftPresets.close();
		WeaponPresets.close();
		RadarPresets.close();
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void addReloadListener(AddReloadListenerEvent event) {
		event.addListener(AircraftPresets.get());
		event.addListener(WeaponPresets.get());
		event.addListener(RadarPresets.get());
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void stopTrackingEvent(PlayerEvent.StopTracking event) {
		//if (!event.getTarget().isVehicle()) return;
		if (!(event.getTarget() instanceof EntitySeat seat)) return;
		if (!event.getTarget().equals(event.getEntity().getVehicle())) return;
		//if (!plane.isVehicleOf(event.getEntity())) return;
		System.out.println("STOP TRACKING EVENT "+event.getEntity().level.getGameTime()+" "+(int)(System.currentTimeMillis()*0.001));
		System.out.println("seat   = "+event.getTarget()+" "+event.getTarget().tickCount);
		System.out.println("plane  = "+event.getEntity().getRootVehicle()+" "+event.getEntity().getRootVehicle().tickCount);
		System.out.println("player = "+event.getEntity()+" "+event.getEntity().tickCount);
		System.out.println("distance = "+event.getEntity().distanceTo(event.getTarget()));
		//System.out.println("is same = "+event.getTarget().equals(event.getEntity().getVehicle()));
		/*System.out.println("stack trace = ");
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		for (int i = 0; i < stack.length; ++i) {
			System.out.println(stack[i].toString());
			if (stack[i].toString().contains("net.minecraft.client.main.Main")) break;
		}*/
	}
	
}
