package com.onewhohears.dscombat.common;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.ClientBoundMissileMovePacket;
import com.onewhohears.dscombat.common.network.ClientBoundPingsPacket;
import com.onewhohears.dscombat.common.network.ServerBoundFlightControlPacket;
import com.onewhohears.dscombat.common.network.ServerBoundPingSelectPacket;
import com.onewhohears.dscombat.common.network.ServerBoundQPacket;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class PacketHandler {
	
	private PacketHandler() {}
	
	private static final String PROTOCOL_VERSION = "1";
	
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(DSCombatMod.MODID), 
			() -> PROTOCOL_VERSION, 
			PROTOCOL_VERSION::equals, 
			PROTOCOL_VERSION::equals);
	
	public static void init() {
		int index = 0;
		INSTANCE.messageBuilder(ServerBoundFlightControlPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ServerBoundFlightControlPacket::encode)
			.decoder(ServerBoundFlightControlPacket::new)
			.consumerMainThread(ServerBoundFlightControlPacket::handle)
			.add();
		INSTANCE.messageBuilder(ServerBoundQPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ServerBoundQPacket::encode)
			.decoder(ServerBoundQPacket::new)
			.consumerMainThread(ServerBoundQPacket::handle)
			.add();
		INSTANCE.messageBuilder(ClientBoundPingsPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundPingsPacket::encode)
			.decoder(ClientBoundPingsPacket::new)
			.consumerMainThread(ClientBoundPingsPacket::handle)
			.add();
		INSTANCE.messageBuilder(ServerBoundPingSelectPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ServerBoundPingSelectPacket::encode)
			.decoder(ServerBoundPingSelectPacket::new)
			.consumerMainThread(ServerBoundPingSelectPacket::handle)
			.add();
		INSTANCE.messageBuilder(ClientBoundMissileMovePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundMissileMovePacket::encode)
			.decoder(ClientBoundMissileMovePacket::new)
			.consumerMainThread(ClientBoundMissileMovePacket::handle)
			.add();
	}
	
}
