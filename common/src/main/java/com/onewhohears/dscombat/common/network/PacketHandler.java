package com.onewhohears.dscombat.common.network;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.toclient.*;
import com.onewhohears.dscombat.common.network.toserver.*;
import com.onewhohears.dscombat.common.network.toclient.ToClientVehicleDecal;
import com.onewhohears.dscombat.common.network.toserver.ToServerVehicleDecal;
import com.onewhohears.onewholibs.util.UtilEntity;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketHandler {
	
	private PacketHandler() {}

    public static final SimpleNetworkManager INSTANCE = SimpleNetworkManager.create(DSCombatMod.MODID);

    public static final MessageType C2S_VEHICLE_CONTROL = INSTANCE.registerC2S(
            "c2s_vehicle_control", ToServerVehicleControl::new);
    public static final MessageType C2S_CRAFT_WEAPON = INSTANCE.registerC2S(
            "c2s_craft_weapon", ToServerCraftWeapon::new);
    public static final MessageType C2S_CRAFT_PLANE = INSTANCE.registerC2S(
            "c2s_craft_plane", ToServerCraftPlane::new);
    public static final MessageType C2S_SEAT_POS = INSTANCE.registerC2S(
            "c2s_seat_pos", ToServerSeatPos::new);
    public static final MessageType C2S_VEHICLE_COLLIDE = INSTANCE.registerC2S(
            "c2s_vehicle_collide", ToServerVehicleCollide::new);
    public static final MessageType C2S_VEHICLE_MOVE_ROT = INSTANCE.registerC2S(
            "c2s_vehicle_move_rot", ToServerVehicleMoveRot::new);
    public static final MessageType C2S_VEHICLE_TEXTURE = INSTANCE.registerC2S(
            "c2s_vehicle_texture", ToServerVehicleTexture::new);
    public static final MessageType C2S_GET_HOOK_CHAINS = INSTANCE.registerC2S(
            "c2s_get_hook_chains", ToServerGetHookChains::new);
    public static final MessageType C2S_SYNC_ROTBOX_PASSENGER_POS = INSTANCE.registerC2S(
            "c2s_sync_rotbox_passenger_pos", ToServerSyncRotBoxPassengerPos::new);
    public static final MessageType C2S_FIX_HITBOXES = INSTANCE.registerC2S(
            "c2s_fix_hitboxes", ToServerFixHitboxes::new);
    public static final MessageType C2S_VEHICLE_SYNC_ACTION = INSTANCE.registerC2S(
            "c2s_vehicle_sync_action", ToServerVehicleSyncAction::new);
    public static final MessageType C2S_CRAFT_WEAPON_PART = INSTANCE.registerC2S(
            "c2s_craft_weapon_part", ToServerCraftWeaponPart::new);
    public static final MessageType C2S_MISSILE_STATION_LAUNCH = INSTANCE.registerC2S(
            "c2s_missile_station_launch", ToServerMissileStationLaunch::new);
    public static final MessageType C2S_MISSILE_STATION_TOGGLE_ARMED = INSTANCE.registerC2S(
            "c2s_missile_station_toggle_armed", ToServerMissileStationToggleArmed::new);
    public static final MessageType C2S_MISSILE_STATION_SET_TARGET = INSTANCE.registerC2S(
            "c2s_missile_station_set_target", ToServerMissileStationSetTarget::new);
    public static final MessageType C2S_VEHICLE_DECAL = INSTANCE.registerC2S(
            "c2s_vehicle_decal", ToServerVehicleDecal::new);

    public static final MessageType S2C_VEHICLE_CONTROL = INSTANCE.registerS2C(
            "s2c_vehicle_control", ToClientVehicleControl::new);
    public static final MessageType S2C_RADAR_PINGS = INSTANCE.registerS2C(
            "s2c_radar_pings", ToClientRadarPings::new);
    public static final MessageType S2C_WEAPON_AMMO = INSTANCE.registerS2C(
            "s2c_weapon_ammo", ToClientWeaponAmmo::new);
    public static final MessageType S2C_REMOVE_PART = INSTANCE.registerS2C(
            "s2c_remove_part", ToClientRemovePart::new);
    public static final MessageType S2C_ADD_PART = INSTANCE.registerS2C(
            "s2c_add_part", ToClientAddPart::new);
    public static final MessageType S2C_RWR_WARNING = INSTANCE.registerS2C(
            "s2c_rwr_warning", ToClientRWRWarning::new);
    public static final MessageType S2C_ADD_FORCE_MOMENT = INSTANCE.registerS2C(
            "s2c_add_force_moment", ToClientAddForceMoment::new);
    public static final MessageType S2C_VEHICLE_TEXTURE = INSTANCE.registerS2C(
            "s2c_vehicle_texture", ToClientVehicleTexture::new);
    public static final MessageType S2C_VEHICLE_EXPLODE = INSTANCE.registerS2C(
            "s2c_vehicle_explode", ToClientVehicleExplode::new);
    public static final MessageType S2C_REMOVE_FIRE_COLUMN = INSTANCE.registerS2C(
            "s2c_remove_fire_column", ToClientRemoveFireColumn::new);
    public static final MessageType S2C_MINE_EXPLODE = INSTANCE.registerS2C(
            "s2c_mine_explode", ToClientMineExplode::new);
    public static final MessageType S2C_WEAPON_IMPACT = INSTANCE.registerS2C(
            "s2c_weapon_impact", ToClientWeaponImpact::new);
    public static final MessageType S2C_DELAYED_SOUND = INSTANCE.registerS2C(
            "s2c_delayed_sound", ToClientDelayedSound::new);
    public static final MessageType S2C_DISTANT_GUNFIRE = INSTANCE.registerS2C(
            "s2c_distant_gunfire", ToClientDistantGunfire::new);
    public static final MessageType S2C_VEHICLE_CHAIN_UPDATE = INSTANCE.registerS2C(
            "s2c_vehicle_chain_update", ToClientVehicleChainUpdate::new);
    public static final MessageType S2C_SYNC_PART = INSTANCE.registerS2C(
            "s2c_sync_part", ToClientSyncPart::new);
    public static final MessageType S2C_DEBUG_HITBOX_POS = INSTANCE.registerS2C(
            "s2c_debug_hitbox_pos", ToClientDebugHitboxPos::new);
    public static final MessageType S2C_ON_SHOOT = INSTANCE.registerS2C(
            "s2c_on_shoot", ToClientOnShoot::new);
    public static final MessageType S2C_SET_TARGET_POS = INSTANCE.registerS2C(
            "s2c_set_target_pos", ToClientSetTargetPos::new);
    public static final MessageType S2C_BALLISTIC_EXPLOSION = INSTANCE.registerS2C(
            "s2c_ballistic_explosion", ToClientBallisticExplosion::new);
    public static final MessageType S2C_ECM_JAM = INSTANCE.registerS2C(
            "s2c_ecm_jam", ToClientEcmJam::new);
    public static final MessageType S2C_VEHICLE_DECAL = INSTANCE.registerS2C(
            "s2c_vehicle_decal", ToClientVehicleDecal::new);

    public static LevelChunk getEntityChunk(@NotNull Entity entity) {
        return UtilEntity.getLevel(entity).getChunkAt(entity.blockPosition());
    }

    public static void sendToTrackers(@NotNull BaseS2CMessage message, @Nullable Entity entity) {
        if (entity == null) return;
        message.sendToChunkListeners(getEntityChunk(entity));
    }

    public static void register() {}
	
}
