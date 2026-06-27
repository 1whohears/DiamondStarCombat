package com.onewhohears.dscombat.integration.distant_players;

import com.onewhohears.distant_players.client.core.DPClientManager;
import com.onewhohears.distant_players.common.core.DPServerManager;
import com.onewhohears.distant_players.common.core.ExtraInfoManager;
import com.onewhohears.distant_players.common.core.RenderTargetInfo;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        ExtraInfoManager.register(ModEntities.ALL_MISSILE.get(), DSCMissileRenderInfo::new);
    }

    public static void addExtraEntity(@NotNull MinecraftServer server, @NotNull Entity entity,
                                      @NotNull ServerPlayer... visibleTo) {
        DPServerManager.get().addExtraTrackableEntity(server, entity, visibleTo);
    }

    public static int getClientDistantLookingAtEntityId(@NotNull Entity looker) {
        double maxDistance = 10000;
        return DPClientManager.get().getRenderTargetInfoStream(info -> UtilGeometry.isPointInsideCone(
                info.getPos(), looker.getEyePosition(), looker.getLookAngle(), 1, maxDistance)
                ).min((info1, info2) -> {
                    double dist1 = looker.distanceToSqr(info1.getPos());
                    double dist2 = looker.distanceToSqr(info2.getPos());
                    return Double.compare(dist1, dist2);
                }).map(RenderTargetInfo::getId).orElse(-1);
    }

    public static @Nullable Vec3 getClientDistantEntityPos(int id) {
        if (id == -1) return null;
        RenderTargetInfo info = DPClientManager.get().getRenderTargetInfo(id);
        if (info == null) return null;
        return info.getPos();
    }

}
