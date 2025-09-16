package com.onewhohears.dscombat.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientVehicleChainUpdate;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityChainHook;
import com.onewhohears.dscombat.entity.parts.EntityChainHook.ChainUpdateType;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

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
        PacketHandler.sendToTrackers(new ToClientVehicleChainUpdate(vehicle, hook, player, type), entity);
	}
	
	public static void sendChainAddVehicleTo(EntityVehicle vehicle, EntityChainHook hook, ServerPlayer reciever) {
		if (vehicle == null || hook == null || reciever == null) return;
        PacketHandler.sendToTrackers(new ToClientVehicleChainUpdate(vehicle, hook, null,
                ChainUpdateType.CHAIN_ADD_VEHICLE), reciever);
	}

    public static List<ServerPlayer> getPlayersWithinRadius(MinecraftServer server, ResourceKey<Level> dimension,
                                                            Vec3 center, double radius) {
        ServerLevel level = server.getLevel(dimension);
        if (level == null) {
            return List.of();
        }
        double radiusSq = radius * radius;
        return level.getPlayers(player -> player.position().distanceToSqr(center) <= radiusSq);
    }
	
}
