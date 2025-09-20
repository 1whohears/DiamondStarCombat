package com.onewhohears.dscombat.util.forge;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.FakePlayer;

public class UtilVehicleEntityImpl {

    public static ServerPlayer createFakePlayer(ServerLevel level, GameProfile profile) {
        return new FakePlayer(level, profile);
    }

}
