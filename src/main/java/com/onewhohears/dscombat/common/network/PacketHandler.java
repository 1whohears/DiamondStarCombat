package com.onewhohears.dscombat.common.network;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.toclient.ToClientAddRadar;
import com.onewhohears.dscombat.common.network.toclient.ToClientAddWeapon;
import com.onewhohears.dscombat.common.network.toclient.ToClientAircraftFuel;
import com.onewhohears.dscombat.common.network.toclient.ToClientMissileMove;
import com.onewhohears.dscombat.common.network.toclient.ToClientRadarPings;
import com.onewhohears.dscombat.common.network.toclient.ToClientRecievePlaneData;
import com.onewhohears.dscombat.common.network.toclient.ToClientRemoveRadar;
import com.onewhohears.dscombat.common.network.toclient.ToClientRemoveWeapon;
import com.onewhohears.dscombat.common.network.toclient.ToClientWeaponAmmo;
import com.onewhohears.dscombat.common.network.toclient.ToClientWeaponIndex;
import com.onewhohears.dscombat.common.network.toserver.ToServerAircraftToItem;
import com.onewhohears.dscombat.common.network.toserver.ToServerCraftPlane;
import com.onewhohears.dscombat.common.network.toserver.ToServerCraftWeapon;
import com.onewhohears.dscombat.common.network.toserver.ToServerFlightControl;
import com.onewhohears.dscombat.common.network.toserver.ToServerPingSelect;
import com.onewhohears.dscombat.common.network.toserver.ToServerAircraftQ;
import com.onewhohears.dscombat.common.network.toserver.ToServerRequestPlaneData;
import com.onewhohears.dscombat.common.network.toserver.ToServerShootTurret;
import com.onewhohears.dscombat.common.network.toserver.ToServerSwitchSeat;

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
		net.messageBuilder(ToServerFlightControl.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerFlightControl::encode)
			.decoder(ToServerFlightControl::new)
			.consumerMainThread(ToServerFlightControl::handle)
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
		net.messageBuilder(ToClientMissileMove.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientMissileMove::encode)
			.decoder(ToClientMissileMove::new)
			.consumerMainThread(ToClientMissileMove::handle)
			.add();
		net.messageBuilder(ToClientRecievePlaneData.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientRecievePlaneData::encode)
			.decoder(ToClientRecievePlaneData::new)
			.consumerMainThread(ToClientRecievePlaneData::handle)
			.add();
		net.messageBuilder(ToClientWeaponAmmo.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientWeaponAmmo::encode)
			.decoder(ToClientWeaponAmmo::new)
			.consumerMainThread(ToClientWeaponAmmo::handle)
			.add();
		net.messageBuilder(ToClientWeaponIndex.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientWeaponIndex::encode)
			.decoder(ToClientWeaponIndex::new)
			.consumerMainThread(ToClientWeaponIndex::handle)
			.add();
		net.messageBuilder(ToServerRequestPlaneData.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerRequestPlaneData::encode)
			.decoder(ToServerRequestPlaneData::new)
			.consumerMainThread(ToServerRequestPlaneData::handle)
			.add(); 
		net.messageBuilder(ToClientAddWeapon.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientAddWeapon::encode)
			.decoder(ToClientAddWeapon::new)
			.consumerMainThread(ToClientAddWeapon::handle)
			.add();
		net.messageBuilder(ToClientRemoveWeapon.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientRemoveWeapon::encode)
			.decoder(ToClientRemoveWeapon::new)
			.consumerMainThread(ToClientRemoveWeapon::handle)
			.add();		
		net.messageBuilder(ToClientAddRadar.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientAddRadar::encode)
			.decoder(ToClientAddRadar::new)
			.consumerMainThread(ToClientAddRadar::handle)
			.add();
		net.messageBuilder(ToClientRemoveRadar.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientRemoveRadar::encode)
			.decoder(ToClientRemoveRadar::new)
			.consumerMainThread(ToClientRemoveRadar::handle)
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
		net.messageBuilder(ToServerAircraftQ.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerAircraftQ::encode)
			.decoder(ToServerAircraftQ::new)
			.consumerMainThread(ToServerAircraftQ::handle)
			.add();
		net.messageBuilder(ToServerShootTurret.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerShootTurret::encode)
			.decoder(ToServerShootTurret::new)
			.consumerMainThread(ToServerShootTurret::handle)
			.add();
	}
	
}
