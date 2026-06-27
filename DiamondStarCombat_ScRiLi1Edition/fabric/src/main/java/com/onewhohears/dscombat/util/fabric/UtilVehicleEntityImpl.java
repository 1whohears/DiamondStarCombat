package com.onewhohears.dscombat.util.fabric;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class UtilVehicleEntityImpl {

    public static ServerPlayer createFakePlayer(ServerLevel level, GameProfile profile) {
        return new FakePlayer(level, profile);
    }

    public static void revive(Entity entity) {
        entity.unsetRemoved(); // FIXME IDK if this revive will work on fabric
    }

}
