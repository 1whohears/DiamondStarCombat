package com.onewhohears.dscombat.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public enum DSCFakePlayer {
    VEHICLE_TRAMPLE(new GameProfile(
            UUID.fromString("a2c9e86f-c72f-4a2d-9d74-9969e79fbec3"),
            "dsc_vehicle_trample")),
    WEAPON_BREAK(new GameProfile(
            UUID.fromString("4f5237ff-0b7f-464e-8c33-9e6b43572fd2"),
            "dsc_weapon_break"));
    private final GameProfile profile;
    private ServerPlayer player = null;
    DSCFakePlayer(GameProfile profile) {
        this.profile = profile;
    }
    public ServerPlayer getPlayer(ServerLevel level) {
        if (player == null) player = UtilVehicleEntity.createFakePlayer(level, profile);
        return player;
    }
}
