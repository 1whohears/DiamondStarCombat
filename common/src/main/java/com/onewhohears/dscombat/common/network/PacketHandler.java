package com.onewhohears.dscombat.common.network;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.toclient.*;
import com.onewhohears.dscombat.common.network.toserver.*;
import com.onewhohears.onewholibs.util.UtilEntity;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;

public final class PacketHandler {
	
	private PacketHandler() {}

    public static final SimpleNetworkManager INSTANCE = SimpleNetworkManager.create(DSCombatMod.MODID);

    public static final MessageType C2S_VEHICLE_CONTROL = INSTANCE.registerC2S(
            "c2s_vehicle_control", ToServerVehicleControl::new);

	public static void register() {
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
		net.messageBuilder(ToServerCraftWeaponPart.class, index++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(ToServerCraftWeaponPart::encode)
				.decoder(ToServerCraftWeaponPart::new)
				.consumerMainThread(ToServerCraftWeaponPart::handle)
				.add();
	}

    public static LevelChunk getEntityChunk(@NotNull Entity entity) {
        return UtilEntity.getLevel(entity).getChunkAt(entity.blockPosition());
    }

    public static void sendToTrackers(@NotNull BaseS2CMessage message, @NotNull Entity entity) {
        message.sendToChunkListeners(getEntityChunk(entity));
    }

    public static void register() {}
	
}
