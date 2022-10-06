package com.onewhohears.dscombat.common;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.ClientBoundEntityMovePacket;
import com.onewhohears.dscombat.common.network.ClientBoundPingsPacket;
import com.onewhohears.dscombat.common.network.ClientBoundPlaneDataPacket;
import com.onewhohears.dscombat.common.network.ServerBoundFlightControlPacket;
import com.onewhohears.dscombat.common.network.ServerBoundPingSelectPacket;
import com.onewhohears.dscombat.common.network.ServerBoundQPacket;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class PacketHandler {
	
	private PacketHandler() {}
	
	private static final String PROTOCOL_VERSION = "1.0";
	
	public static SimpleChannel INSTANCE;
	
	public static void register() {
		SimpleChannel net = NetworkRegistry.ChannelBuilder
				.named(new ResourceLocation(DSCombatMod.MODID, "messages"))
				.networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
		INSTANCE = net;
		int index = 0;
		net.messageBuilder(ServerBoundFlightControlPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ServerBoundFlightControlPacket::encode)
			.decoder(ServerBoundFlightControlPacket::new)
			.consumerMainThread(ServerBoundFlightControlPacket::handle)
			.add();
		net.messageBuilder(ServerBoundQPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ServerBoundQPacket::encode)
			.decoder(ServerBoundQPacket::new)
			.consumerMainThread(ServerBoundQPacket::handle)
			.add();
		net.messageBuilder(ClientBoundPingsPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundPingsPacket::encode)
			.decoder(ClientBoundPingsPacket::new)
			.consumerMainThread(ClientBoundPingsPacket::handle)
			.add();
		net.messageBuilder(ServerBoundPingSelectPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ServerBoundPingSelectPacket::encode)
			.decoder(ServerBoundPingSelectPacket::new)
			.consumerMainThread(ServerBoundPingSelectPacket::handle)
			.add();
		net.messageBuilder(ClientBoundEntityMovePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundEntityMovePacket::encode)
			.decoder(ClientBoundEntityMovePacket::new)
			.consumerMainThread(ClientBoundEntityMovePacket::handle)
			.add();
		net.messageBuilder(ClientBoundPlaneDataPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundPlaneDataPacket::encode)
			.decoder(ClientBoundPlaneDataPacket::new)
			.consumerMainThread(ClientBoundPlaneDataPacket::handle)
			.add();
	}
	
}
