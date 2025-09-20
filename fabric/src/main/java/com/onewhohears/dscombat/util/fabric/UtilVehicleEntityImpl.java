package com.onewhohears.dscombat.util.fabric;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class UtilVehicleEntityImpl {

    public static ServerPlayer createFakePlayer(ServerLevel level, GameProfile profile) {
        return new FakePlayer(level, profile);
    }

}
