package com.onewhohears.dscombat.common;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.ClientboundUpdateBoostPacket;
import com.onewhohears.dscombat.common.network.ServerBoundFlightControlPacket;
import com.onewhohears.dscombat.common.network.ServerBoundQPacket;
import com.onewhohears.dscombat.common.network.ServerboundBoostUpdatePacket;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class PacketHandler {
	
	private PacketHandler() {}
	
	private static final String PROTOCOL_VERSION = "1";
	
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(DSCombatMod.MODID), 
			() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	
	public static void init() {
		int index = 0;
		INSTANCE.messageBuilder(ServerBoundFlightControlPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ServerBoundFlightControlPacket::encode)
			.decoder(ServerBoundFlightControlPacket::new)
			.consumer(ServerBoundFlightControlPacket::handle)
			.add();
		INSTANCE.messageBuilder(ServerBoundQPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ServerBoundQPacket::encode)
			.decoder(ServerBoundQPacket::new)
			.consumer(ServerBoundQPacket::handle)
			.add();
	}
	
}
