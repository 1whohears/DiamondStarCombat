package com.onewhohears.dscombat.integration.distant_players;

import com.onewhohears.distant_players.common.core.DPServerManager;
import com.onewhohears.distant_players.common.core.ExtraInfoManager;
import com.onewhohears.dscombat.init.ModEntities;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class DSCDistantPlayers {

    public static void register() {
        ExtraInfoManager.register(ModEntities.BOAT.get(), DSCVehicleRenderInfo::new);
        ExtraInfoManager.register(ModEntities.CAR.get(), DSCVehicleRenderInfo::new);
        ExtraInfoManager.register(ModEntities.PLANE.get(), DSCVehicleRenderInfo::new);
        ExtraInfoManager.register(ModEntities.HELICOPTER.get(), DSCVehicleRenderInfo::new);
        ExtraInfoManager.register(ModEntities.SUBMARINE.get(), DSCVehicleRenderInfo::new);
        ExtraInfoManager.register(ModEntities.STATIONARY.get(), DSCVehicleRenderInfo::new);
        ExtraInfoManager.register(ModEntities.ANTI_RADAR_MISSILE.get(), DSCMissileRenderInfo::new);
        ExtraInfoManager.register(ModEntities.IR_MISSILE.get(), DSCMissileRenderInfo::new);
        ExtraInfoManager.register(ModEntities.DUMB_TORPEDO_MISSILE.get(), DSCMissileRenderInfo::new);
        ExtraInfoManager.register(ModEntities.TORPEDO_MISSILE.get(), DSCMissileRenderInfo::new);
        ExtraInfoManager.register(ModEntities.TRACK_MISSILE.get(), DSCMissileRenderInfo::new);
        ExtraInfoManager.register(ModEntities.POS_MISSILE.get(), DSCMissileRenderInfo::new);
    }

    public static void addExtraEntity(@NotNull MinecraftServer server, @NotNull Entity entity,
                                      @NotNull ServerPlayer... visibleTo) {
        DPServerManager.get().addExtraTrackableEntity(server, entity, visibleTo);
    }

}
