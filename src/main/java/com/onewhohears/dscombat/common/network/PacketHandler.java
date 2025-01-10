package com.onewhohears.dscombat.common.network;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.toclient.*;
import com.onewhohears.dscombat.common.network.toserver.*;

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
		net.messageBuilder(ToServerVehicleControl.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerVehicleControl::encode)
			.decoder(ToServerVehicleControl::new)
			.consumerMainThread(ToServerVehicleControl::handle)
			.add();
		net.messageBuilder(ToClientVehicleControl.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientVehicleControl::encode)
			.decoder(ToClientVehicleControl::new)
			.consumerMainThread(ToClientVehicleControl::handle)
			.add();
		net.messageBuilder(ToClientRadarPings.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientRadarPings::encode)
			.decoder(ToClientRadarPings::new)
			.consumerMainThread(ToClientRadarPings::handle)
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
		net.messageBuilder(ToServerSeatPos.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerSeatPos::encode)
			.decoder(ToServerSeatPos::new)
			.consumerMainThread(ToServerSeatPos::handle)
			.add();
		net.messageBuilder(ToServerVehicleCollide.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerVehicleCollide::encode)
			.decoder(ToServerVehicleCollide::new)
			.consumerMainThread(ToServerVehicleCollide::handle)
			.add();
		net.messageBuilder(ToServerVehicleMoveRot.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerVehicleMoveRot::encode)
			.decoder(ToServerVehicleMoveRot::new)
			.consumerMainThread(ToServerVehicleMoveRot::handle)
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
		net.messageBuilder(ToClientVehicleExplode.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientVehicleExplode::encode)
			.decoder(ToClientVehicleExplode::new)
			.consumerMainThread(ToClientVehicleExplode::handle)
			.add();
		net.messageBuilder(ToClientWeaponImpact.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientWeaponImpact::encode)
			.decoder(ToClientWeaponImpact::new)
			.consumerMainThread(ToClientWeaponImpact::handle)
			.add();
		net.messageBuilder(ToClientDelayedSound.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientDelayedSound::encode)
			.decoder(ToClientDelayedSound::new)
			.consumerMainThread(ToClientDelayedSound::handle)
			.add();
		net.messageBuilder(ToClientVehicleChainUpdate.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientVehicleChainUpdate::encode)
			.decoder(ToClientVehicleChainUpdate::new)
			.consumerMainThread(ToClientVehicleChainUpdate::handle)
			.add();
		net.messageBuilder(ToServerGetHookChains.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerGetHookChains::encode)
			.decoder(ToServerGetHookChains::new)
			.consumerMainThread(ToServerGetHookChains::handle)
			.add();
		net.messageBuilder(ToClientSyncPart.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientSyncPart::encode)
			.decoder(ToClientSyncPart::new)
			.consumerMainThread(ToClientSyncPart::handle)
			.add();
		net.messageBuilder(ToServerSyncRotBoxPassengerPos.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerSyncRotBoxPassengerPos::encode)
			.decoder(ToServerSyncRotBoxPassengerPos::new)
			.consumerMainThread(ToServerSyncRotBoxPassengerPos::handle)
			.add();
		net.messageBuilder(ToClientDebugHitboxPos.class, index++, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(ToClientDebugHitboxPos::encode)
			.decoder(ToClientDebugHitboxPos::new)
			.consumerMainThread(ToClientDebugHitboxPos::handle)
			.add();
		net.messageBuilder(ToServerFixHitboxes.class, index++, NetworkDirection.PLAY_TO_SERVER)
			.encoder(ToServerFixHitboxes::encode)
			.decoder(ToServerFixHitboxes::new)
			.consumerMainThread(ToServerFixHitboxes::handle)
			.add();
		net.messageBuilder(ToServerVehicleSyncAction.class, index++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(ToServerVehicleSyncAction::encode)
				.decoder(ToServerVehicleSyncAction::new)
				.consumerMainThread(ToServerVehicleSyncAction::handle)
				.add();
		net.messageBuilder(ToClientOnShoot.class, index++, NetworkDirection.PLAY_TO_CLIENT)
				.encoder(ToClientOnShoot::encode)
				.decoder(ToClientOnShoot::new)
				.consumerMainThread(ToClientOnShoot::handle)
				.add();
	}
	
}
