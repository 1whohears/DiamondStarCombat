package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.weapon.NonTickingMissileManager;
import com.onewhohears.dscombat.entity.parts.EntitySeat;

import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE)
public final class CommonForgeEvents {
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		if (!(event.player.getVehicle() instanceof EntitySeat seat)) return;
		double x = seat.getX(), y = seat.getY(), z = seat.getZ();
		double w = event.player.getBbWidth()/2;
		event.player.setBoundingBox(new AABB(x-w, y-w, z-w, x+w, y+w, z+w));
		if (event.phase == Phase.START) {
			if (event.player.isShiftKeyDown()) {
				
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void serverTickEvent(TickEvent.ServerTickEvent event) {
		if (event.phase != Phase.END) return;
		NonTickingMissileManager.serverTick(event.getServer());
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void entityDismountEvent(EntityMountEvent event) {
		if (!event.isDismounting()) return;
		System.out.println("DISMOUNT "
				+event.getEntityMounting()+" "
				+event.getEntityBeingMounted());
		System.out.println("isClientSide = "+event.getEntityMounting().level.isClientSide);
		System.out.println("isShiftKeyDown = "+event.getEntityMounting().isShiftKeyDown());
		// TODO prevent player from dismounting aircraft with shift to free that key
	}
	
}
