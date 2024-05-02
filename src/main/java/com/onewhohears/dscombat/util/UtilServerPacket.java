package com.onewhohears.dscombat.util;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientVehicleChainUpdate;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityChainHook;
import com.onewhohears.dscombat.entity.parts.EntityChainHook.ChainUpdateType;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class UtilServerPacket {
	
	public static void sendChainAddPlayer(EntityChainHook hook, Player player) {
		sendChainUpdateToClient(null, hook, player, ChainUpdateType.CHAIN_ADD_PLAYER);
	}
	
	public static void sendChainAddVehicle(EntityVehicle vehicle, EntityChainHook hook, @Nullable Player player) {
		sendChainUpdateToClient(vehicle, hook, player, ChainUpdateType.CHAIN_ADD_VEHICLE);
	}
	
	public static void sendChainDisconnectPlayer(EntityChainHook hook, Player player) {
		sendChainUpdateToClient(null, hook, player, ChainUpdateType.CHAIN_DISCONNECT_PLAYER);
	}
	
	public static void sendChainDisconnectVehicle(EntityVehicle vehicle, EntityChainHook hook) {
		sendChainUpdateToClient(vehicle, hook, null, ChainUpdateType.CHAIN_DISCONNECT_VEHICLE);
	}
	
	public static void sendVehicleAddPlayer(EntityVehicle vehicle, Player player) {
		sendChainUpdateToClient(vehicle, null, player, ChainUpdateType.VEHICLE_ADD_PLAYER);
	}
	
	private static void sendChainUpdateToClient(EntityVehicle vehicle, EntityChainHook hook, Player player, ChainUpdateType type) {
		Entity entity = (vehicle != null) ? vehicle : (hook != null) ? hook : (player != null) ? player : null;
		if (entity == null) return;
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), 
				new ToClientVehicleChainUpdate(vehicle, hook, player, type));
	}
	
	public static void sendChainAddVehicleTo(EntityVehicle vehicle, EntityChainHook hook, ServerPlayer reciever) {
		if (vehicle == null || hook == null || reciever == null) return;
		PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> reciever), 
				new ToClientVehicleChainUpdate(vehicle, hook, null, ChainUpdateType.CHAIN_ADD_VEHICLE));
	}
	
}
