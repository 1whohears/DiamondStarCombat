package com.onewhohears.dscombat.util;

import com.onewhohears.dscombat.data.vehicle.physics.SeaLevels;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.wind_tunnel.EntityWindTunnel;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class UtilVehicleEntity {

    public static double getRadarCrossSectionalArea(Entity entity, Vec3 radarPos) {
        if (entity instanceof EntityVehicle plane) return plane.getRadarArea(radarPos);
        double area = entity.getBbHeight()*entity.getBbWidth();
        if (entity.getType().is(ModTags.EntityTypes.VEHICLE)) return Math.max(area, 1);
        return area;
    }

    public static boolean isOnGroundOrWater(Entity entity) {
        if (entity.getType().is(ModTags.EntityTypes.ALWAYS_GROUNDED)) return true;
        if (entity.isPassenger()) {
            Entity rv = entity.getRootVehicle();
            if (rv.getType().is(ModTags.EntityTypes.ALWAYS_GROUNDED)) return true;
            if (rv.isOnGround() || UtilEntity.isHeadAboveWater(rv)) return true;
        }
        if (entity instanceof Player p && p.isFallFlying()) return false;
        if (!entity.isInWater() && entity.isSprinting() && entity.fallDistance < 1.15) return true;
        if (entity.isOnGround() || UtilEntity.isHeadAboveWater(entity)) return true;
        return false;
    }

    public static double getAirDensity(Entity entity) {
        return SeaLevels.getAirPressure(entity.getLevel().dimension(), entity.getY());
    }

    public static boolean hasPermissionToBreakBlock(BlockPos pos, BlockState state, Level level,
                                                    @Nullable Entity entity, DSCFakePlayer type) {
        if (entity instanceof Player player) {
            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, state, player);
            MinecraftForge.EVENT_BUS.post(event);
            return !event.isCanceled();
        } else if (entity instanceof Enemy) {
            return level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        } else if (!level.isClientSide()) {
            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, state,
                    type.getPlayer((ServerLevel) level));
            MinecraftForge.EVENT_BUS.post(event);
            return !event.isCanceled();
        }
        return false;
    }

    public static boolean vehicleHasPermissionToTrample(BlockPos pos, BlockState state, Level level,
                                                        @Nullable Entity entity) {
        return hasPermissionToBreakBlock(pos, state, level, entity, DSCFakePlayer.VEHICLE_TRAMPLE);
    }

    public static boolean weaponHasPermissionToBreak(BlockPos pos, BlockState state, Level level,
                                                        @Nullable Entity entity) {
        return hasPermissionToBreakBlock(pos, state, level, entity, DSCFakePlayer.WEAPON_BREAK);
    }

    public static final int WIND_TUNNEL_SEARCH_RANGE = 16;

    public static Optional<EntityWindTunnel> findWindTunnel(Vec3 pos, Level level) {
        double r = WIND_TUNNEL_SEARCH_RANGE;
        List<EntityWindTunnel> tunnels = level.getEntitiesOfClass(EntityWindTunnel.class,
                new AABB(pos.x-r, pos.y-r, pos.z-r, pos.x+r, pos.y+r, pos.z+r));
        if (tunnels.isEmpty()) return Optional.empty();
        return tunnels.stream().min((tunnel1, tunnel2) -> (int) (tunnel2.distanceToSqr(pos) - tunnel1.distanceToSqr(pos)));
    }

}
