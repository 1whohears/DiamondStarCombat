package com.onewhohears.dscombat.util;

import com.mojang.authlib.GameProfile;
import com.onewhohears.dscombat.data.vehicle.physics.SeaLevels;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.wind_tunnel.EntityWindTunnel;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.onewholibs.util.UtilEntity;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.utils.value.IntValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            if (rv.onGround() || UtilEntity.isHeadAboveWater(rv)) return true;
        }
        if (entity instanceof Player p && p.isFallFlying()) return false;
        if (!entity.isInWater() && entity.isSprinting() && entity.fallDistance < 1.15) return true;
        if (entity.onGround() || UtilEntity.isHeadAboveWater(entity)) return true;
        return false;
    }

    public static double getAirDensity(Entity entity) {
        return SeaLevels.getAirPressure(UtilEntity.getLevel(entity).dimension(), entity.getY());
    }

    private static final IntValue XP = new IntValue() {
        @Override public void accept(int value) {}
        @Override public int getAsInt() {return 0;}
    };

    public static boolean hasPermissionToBreakBlock(BlockPos pos, BlockState state, Level level,
                                                    @Nullable Entity entity, DSCFakePlayer type) {
        if (entity instanceof ServerPlayer player) {
            EventResult result = BlockEvent.BREAK.invoker().breakBlock(level, pos, state, player, XP);
            return !result.isPresent() || result.isTrue(); // FIXME is this correct? test with FTB
        } else if (entity instanceof Enemy) {
            return level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        } else if (!level.isClientSide()) {
            EventResult result = BlockEvent.BREAK.invoker().breakBlock(level, pos, state,
                    type.getPlayer((ServerLevel) level), XP);
            return !result.isPresent() || result.isTrue();
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

    public static int getDistFromGround(Entity entity, int limit, boolean ignoreWater) {
        Level l = UtilEntity.getLevel(entity);
        int[] pos = new int[]{entity.getBlockX(), entity.getBlockY(), entity.getBlockZ()};

        int dist;
        for(dist = 0; pos[1] >= l.getMinBuildHeight() && dist <= limit; ++dist) {
            BlockState block = l.getBlockState(new BlockPos(pos[0], pos[1], pos[2]));
            if (block != null && !block.isAir() && (!ignoreWater || UtilEntity.blocksMotion(block))) {
                break;
            }
            pos[1]--;
        }

        return dist;
    }

    @ExpectPlatform
    public static ServerPlayer createFakePlayer(ServerLevel level, GameProfile profile) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void revive(Entity entity) {
        throw new AssertionError();
    }

    public static boolean isExplosion(@NotNull DamageSource source) {
        return source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION);
    }

    public static boolean isFire(@NotNull DamageSource source) {
        return source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE);
    }

    public static boolean isBypassArmor(@NotNull DamageSource source) {

    }

}
