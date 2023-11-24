package com.onewhohears.dscombat.common.network;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.toclient.ToClientAddForceMoment;
import com.onewhohears.dscombat.common.network.toclient.ToClientAddPart;
import com.onewhohears.dscombat.common.network.toclient.ToClientAircraftControl;
import com.onewhohears.dscombat.common.network.toclient.ToClientAircraftFuel;
import com.onewhohears.dscombat.common.network.toclient.ToClientDataPackSynch;
import com.onewhohears.dscombat.common.network.toclient.ToClientRWRWarning;
import com.onewhohears.dscombat.common.network.toclient.ToClientRadarPings;
import com.onewhohears.dscombat.common.network.toclient.ToClientRemovePart;
import com.onewhohears.dscombat.common.network.toclient.ToClientVehicleTexture;
import com.onewhohears.dscombat.common.network.toclient.ToClientWeaponAmmo;
import com.onewhohears.dscombat.common.network.toserver.ToServerAircraftCollide;
import com.onewhohears.dscombat.common.network.toserver.ToServerAircraftControl;
import com.onewhohears.dscombat.common.network.toserver.ToServerAircraftMoveRot;
import com.onewhohears.dscombat.common.network.toserver.ToServerAircraftToItem;
import com.onewhohears.dscombat.common.network.toserver.ToServerCraftPlane;
import com.onewhohears.dscombat.common.network.toserver.ToServerCraftWeapon;
import com.onewhohears.dscombat.common.network.toserver.ToServerDismount;
import com.onewhohears.dscombat.common.network.toserver.ToServerPingSelect;
import com.onewhohears.dscombat.common.network.toserver.ToServerSeatPos;
import com.onewhohears.dscombat.common.network.toserver.ToServerShootTurret;
import com.onewhohears.dscombat.common.network.toserver.ToServerSwitchSeat;
import com.onewhohears.dscombat.common.network.toserver.ToServerVehicleTexture;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class PacketHandler {
	
	private PacketHandler() {}
	
	private static final String PROTOCOL_VERSION = "1.0";
	
	public static SimpleChannel INSTANCE;

	// FIXME: find cause of error spam thrown by L900 in ClientPacketListener
	public static void register() {
		SimpleChannel net = NetworkRegistry.ChannelBuilder
				.named(new ResourceLocation(DSCombatMod.MODID, "messages"))
				.networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(s -> s.equals(PROTOCOL_VERSION))
                .serverAcceptedVersions(s -> s.equals(PROTOCOL_VERSION))
                .simpleChannel();
		INSTANCE = net;
		int index = 0;
		net.messageBuilder(ToServerAircraftControl.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerAircraftControl::encode)
			.decoder(ToServerAircraftControl::new)
			.consumerMainThread(ToServerAircraftControl::handle)
			.add();
		net.messageBuilder(ToClientAircraftControl.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientAircraftControl::encode)
			.decoder(ToClientAircraftControl::new)
			.consumerMainThread(ToClientAircraftControl::handle)
			.add();
		net.messageBuilder(ToClientRadarPings.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientRadarPings::encode)
			.decoder(ToClientRadarPings::new)
			.consumerMainThread(ToClientRadarPings::handle)
			.add();
		net.messageBuilder(ToServerPingSelect.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerPingSelect::encode)
			.decoder(ToServerPingSelect::new)
			.consumerMainThread(ToServerPingSelect::handle)
			.add();
		net.messageBuilder(ToClientWeaponAmmo.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientWeaponAmmo::encode)
			.decoder(ToClientWeaponAmmo::new)
			.consumerMainThread(ToClientWeaponAmmo::handle)
			.add();
		net.messageBuilder(ToClientRemovePart.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientRemovePart::encode)
			.decoder(ToClientRemovePart::new)
			.consumerMainThread(ToClientRemovePart::handle)
			.add();	
		net.messageBuilder(ToClientAddPart.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientAddPart::encode)
			.decoder(ToClientAddPart::new)
			.consumerMainThread(ToClientAddPart::handle)
			.add();
		net.messageBuilder(ToServerSwitchSeat.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerSwitchSeat::encode)
			.decoder(ToServerSwitchSeat::new)
			.consumerMainThread(ToServerSwitchSeat::handle)
			.add();
		net.messageBuilder(ToClientAircraftFuel.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientAircraftFuel::encode)
			.decoder(ToClientAircraftFuel::new)
			.consumerMainThread(ToClientAircraftFuel::handle)
			.add();
		net.messageBuilder(ToServerAircraftToItem.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerAircraftToItem::encode)
			.decoder(ToServerAircraftToItem::new)
			.consumerMainThread(ToServerAircraftToItem::handle)
			.add();	
		net.messageBuilder(ToServerCraftWeapon.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerCraftWeapon::encode)
			.decoder(ToServerCraftWeapon::new)
			.consumerMainThread(ToServerCraftWeapon::handle)
			.add();
		net.messageBuilder(ToServerCraftPlane.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerCraftPlane::encode)
			.decoder(ToServerCraftPlane::new)
			.consumerMainThread(ToServerCraftPlane::handle)
			.add();
		net.messageBuilder(ToServerShootTurret.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerShootTurret::encode)
			.decoder(ToServerShootTurret::new)
			.consumerMainThread(ToServerShootTurret::handle)
			.add();
		net.messageBuilder(ToClientRWRWarning.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientRWRWarning::encode)
			.decoder(ToClientRWRWarning::new)
			.consumerMainThread(ToClientRWRWarning::handle)
			.add();
		net.messageBuilder(ToClientAddForceMoment.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientAddForceMoment::encode)
			.decoder(ToClientAddForceMoment::new)
			.consumerMainThread(ToClientAddForceMoment::handle)
			.add();
		net.messageBuilder(ToClientDataPackSynch.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientDataPackSynch::encode)
			.decoder(ToClientDataPackSynch::new)
			.consumerMainThread(ToClientDataPackSynch::handle)
			.add();
		net.messageBuilder(ToServerDismount.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerDismount::encode)
			.decoder(ToServerDismount::new)
			.consumerMainThread(ToServerDismount::handle)
			.add();
		net.messageBuilder(ToServerSeatPos.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerSeatPos::encode)
			.decoder(ToServerSeatPos::new)
			.consumerMainThread(ToServerSeatPos::handle)
			.add();
		net.messageBuilder(ToServerAircraftCollide.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerAircraftCollide::encode)
			.decoder(ToServerAircraftCollide::new)
			.consumerMainThread(ToServerAircraftCollide::handle)
			.add();
		net.messageBuilder(ToServerAircraftMoveRot.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerAircraftMoveRot::encode)
			.decoder(ToServerAircraftMoveRot::new)
			.consumerMainThread(ToServerAircraftMoveRot::handle)
			.add();
		net.messageBuilder(ToServerVehicleTexture.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerVehicleTexture::encode)
			.decoder(ToServerVehicleTexture::new)
			.consumerMainThread(ToServerVehicleTexture::handle)
			.add();
		net.messageBuilder(ToClientVehicleTexture.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientVehicleTexture::encode)
			.decoder(ToClientVehicleTexture::new)
			.consumerMainThread(ToClientVehicleTexture::handle)
			.add();
	}
	
}
