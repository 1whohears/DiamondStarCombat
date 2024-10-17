package com.onewhohears.dscombat.util;

import com.onewhohears.dscombat.data.vehicle.SeaLevels;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;

import javax.annotation.Nullable;

public class UtilVehicleEntity {

    public static double getCrossSectionalArea(Entity entity) {
        if (entity instanceof EntityVehicle plane) return plane.getCrossSectionArea();
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

    public static double getAirPressure(Entity entity) {
        return SeaLevels.getAirPressure(entity.level().dimension(), entity.getY());
    }

    public static boolean hasPermissionToBreakBlock(BlockPos pos, BlockState state, Level level, @Nullable Entity entity) {
        if (entity instanceof Player player) {
            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, state, player);
            MinecraftForge.EVENT_BUS.post(event);
            return !event.isCanceled();
        }
        return level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
    }

}
