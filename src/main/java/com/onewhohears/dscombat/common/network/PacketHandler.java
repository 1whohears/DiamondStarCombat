package com.onewhohears.dscombat.common.network;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundAddPartPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundAddRadarPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundAddWeaponPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundFuelPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundMissileMovePacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundPingsPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundPlaneDataPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundPlaySoundPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundRemovePartPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundRemoveRadarPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundRemoveWeaponPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundWeaponAmmoPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundWeaponIndexPacket;
import com.onewhohears.dscombat.common.network.toserver.ServerBoundAircraftToItemPacket;
import com.onewhohears.dscombat.common.network.toserver.ServerBoundCraftWeaponPacket;
import com.onewhohears.dscombat.common.network.toserver.ServerBoundFlightControlPacket;
import com.onewhohears.dscombat.common.network.toserver.ServerBoundPingSelectPacket;
import com.onewhohears.dscombat.common.network.toserver.ServerBoundQPacket;
import com.onewhohears.dscombat.common.network.toserver.ServerBoundRequestPlaneDataPacket;
import com.onewhohears.dscombat.common.network.toserver.ServerBoundSwitchSeatPacket;

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
		net.messageBuilder(ClientBoundMissileMovePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundMissileMovePacket::encode)
			.decoder(ClientBoundMissileMovePacket::new)
			.consumerMainThread(ClientBoundMissileMovePacket::handle)
			.add();
		net.messageBuilder(ClientBoundPlaneDataPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundPlaneDataPacket::encode)
			.decoder(ClientBoundPlaneDataPacket::new)
			.consumerMainThread(ClientBoundPlaneDataPacket::handle)
			.add();
		net.messageBuilder(ClientBoundWeaponAmmoPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundWeaponAmmoPacket::encode)
			.decoder(ClientBoundWeaponAmmoPacket::new)
			.consumerMainThread(ClientBoundWeaponAmmoPacket::handle)
			.add();
		net.messageBuilder(ClientBoundWeaponIndexPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundWeaponIndexPacket::encode)
			.decoder(ClientBoundWeaponIndexPacket::new)
			.consumerMainThread(ClientBoundWeaponIndexPacket::handle)
			.add();
		net.messageBuilder(ServerBoundRequestPlaneDataPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ServerBoundRequestPlaneDataPacket::encode)
			.decoder(ServerBoundRequestPlaneDataPacket::new)
			.consumerMainThread(ServerBoundRequestPlaneDataPacket::handle)
			.add();
		net.messageBuilder(ClientBoundPlaySoundPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundPlaySoundPacket::encode)
			.decoder(ClientBoundPlaySoundPacket::new)
			.consumerMainThread(ClientBoundPlaySoundPacket::handle)
			.add();
		net.messageBuilder(ClientBoundAddWeaponPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundAddWeaponPacket::encode)
			.decoder(ClientBoundAddWeaponPacket::new)
			.consumerMainThread(ClientBoundAddWeaponPacket::handle)
			.add();
		net.messageBuilder(ClientBoundRemoveWeaponPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundRemoveWeaponPacket::encode)
			.decoder(ClientBoundRemoveWeaponPacket::new)
			.consumerMainThread(ClientBoundRemoveWeaponPacket::handle)
			.add();
		net.messageBuilder(ClientBoundAddPartPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundAddPartPacket::encode)
			.decoder(ClientBoundAddPartPacket::new)
			.consumerMainThread(ClientBoundAddPartPacket::handle)
			.add();
		net.messageBuilder(ClientBoundRemovePartPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundRemovePartPacket::encode)
			.decoder(ClientBoundRemovePartPacket::new)
			.consumerMainThread(ClientBoundRemovePartPacket::handle)
			.add();		
		net.messageBuilder(ClientBoundAddRadarPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundAddRadarPacket::encode)
			.decoder(ClientBoundAddRadarPacket::new)
			.consumerMainThread(ClientBoundAddRadarPacket::handle)
			.add();
		net.messageBuilder(ClientBoundRemoveRadarPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundRemoveRadarPacket::encode)
			.decoder(ClientBoundRemoveRadarPacket::new)
			.consumerMainThread(ClientBoundRemoveRadarPacket::handle)
			.add();
		net.messageBuilder(ServerBoundSwitchSeatPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ServerBoundSwitchSeatPacket::encode)
			.decoder(ServerBoundSwitchSeatPacket::new)
			.consumerMainThread(ServerBoundSwitchSeatPacket::handle)
			.add();
		net.messageBuilder(ClientBoundFuelPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ClientBoundFuelPacket::encode)
			.decoder(ClientBoundFuelPacket::new)
			.consumerMainThread(ClientBoundFuelPacket::handle)
			.add();
		net.messageBuilder(ServerBoundAircraftToItemPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ServerBoundAircraftToItemPacket::encode)
			.decoder(ServerBoundAircraftToItemPacket::new)
			.consumerMainThread(ServerBoundAircraftToItemPacket::handle)
			.add();	
		net.messageBuilder(ServerBoundCraftWeaponPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ServerBoundCraftWeaponPacket::encode)
			.decoder(ServerBoundCraftWeaponPacket::new)
			.consumerMainThread(ServerBoundCraftWeaponPacket::handle)
			.add();
	}
	
}
