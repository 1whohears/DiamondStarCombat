package com.onewhohears.dscombat.common.event;

import java.util.HashSet;
import java.util.Set;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.onewholibs.common.event.GetJsonPresetListenersEvent;
import com.onewhohears.dscombat.data.graph.StatGraphs;
import com.onewhohears.dscombat.data.parts.PartPresets;
import com.onewhohears.dscombat.data.radar.RadarPresets;
import com.onewhohears.dscombat.data.vehicle.VehiclePresets;
import com.onewhohears.dscombat.data.weapon.NonTickingMissileManager;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.vehicle.CustomExplosion;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.RotableHitboxes;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE)
public final class CommonForgeEvents {
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void livingHurtEvent(LivingHurtEvent event) {
		if (event.getSource().isMagic()) return;
		if (!event.getEntity().isPassenger() || !(event.getEntity().getRootVehicle() instanceof EntityVehicle plane)) return;
		event.setAmount(Math.max(0, plane.calcDamageToInside(event.getSource(), event.getAmount())));
	}
	
	private static Set<Integer> explodeRepeatCheck = new HashSet<>();
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void explosionEvent(ExplosionEvent.Detonate event) {
		explodeRepeatCheck.clear();
		for (int i = 0; i < event.getAffectedEntities().size(); ++i) {
			Entity e = event.getAffectedEntities().get(i);
			if (!e.ignoreExplosion()) continue;
			if (explodeRepeatCheck.contains(e.getId())) continue;
			if (!(e instanceof CustomExplosion entity)) continue;
			entity.customExplosionHandler(event.getExplosion());
			explodeRepeatCheck.add(e.getId());
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		// CANCEL ELYTRA
		if (event.player.isFallFlying() && event.player.level.getGameRules().getBoolean(DSCGameRules.DISABLE_ELYTRA_FLYING)) {
			event.player.stopFallFlying();
		}
		// CHANGE HITBOX
		/*if (!(event.player.getVehicle() instanceof EntitySeat seat)) return;
		double x = seat.getX(), y = seat.getY(), z = seat.getZ();
		double w = 0.1; //event.player.getBbWidth()/2;
		event.player.setBoundingBox(new AABB(x-w, y-w, z-w, x+w, y+w, z+w));*/
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void serverTickEvent(TickEvent.ServerTickEvent event) {
		if (event.phase != Phase.END) return;
		NonTickingMissileManager.serverTick(event.getServer());
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void serverStoppingEvent(ServerStoppingEvent event) {
		VehiclePresets.close();
		WeaponPresets.close();
		RadarPresets.close();
		PartPresets.close();
		StatGraphs.close();
		RotableHitboxes.onServerStop();
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void addJsonPresetListener(GetJsonPresetListenersEvent event) {
		event.addListener(StatGraphs.get());
		event.addListener(VehiclePresets.get());
		event.addListener(WeaponPresets.get());
		event.addListener(RadarPresets.get());
		event.addListener(PartPresets.get());
	}
	
	/*@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void entityJoinLevelEvent(EntityJoinLevelEvent event) {
		Level level = event.getLevel();
		if (level.isClientSide) return;
		
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void entityLeaveLevelEvent(EntityLeaveLevelEvent event) {
		Level level = event.getLevel();
		if (level.isClientSide) return;
		if (!event.getEntity().getRemovalReason().shouldSave()) return;
		if (!(event.getEntity() instanceof EntityVehicle vehicle)) return;
		if (vehicle.getHitboxes().length == 0) return;
		
	}*/
	
	/*@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void chunkWatchEvent(ChunkWatchEvent.Watch event) {
		System.out.println("CHUNK WATCH EVENT");
		System.out.println("event chunk  = "+event.getPos());
		System.out.println("player chunk = "+event.getPlayer().chunkPosition());
		if (event.getPlayer().getVehicle() != null) 
		System.out.println("seat chunk   = "+event.getPlayer().getVehicle().chunkPosition());
		System.out.println("level tick   = "+event.getLevel().getGameTime());
		
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		for (int i = 0; i < stack.length; ++i) {
			System.out.println(stack[i].toString());
			if (stack[i].toString().contains("net.minecraft.client.main.Main")) break;
		}
	}*/
	
}
