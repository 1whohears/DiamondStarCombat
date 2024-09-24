package com.onewhohears.dscombat.util;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
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
            if (rv.isOnGround() || UtilEntity.isHeadAboveWater(rv)) return true;
        }
        if (entity instanceof Player p && p.isFallFlying()) return false;
        if (!entity.isInWater() && entity.isSprinting() && entity.fallDistance < 1.15) return true;
        if (entity.isOnGround() || UtilEntity.isHeadAboveWater(entity)) return true;
        return false;
    }

    public static double getAirPressure(Entity entity) {
        DimensionType dt = entity.level.dimensionType();
        double space;
        double surface;
        if (dt.natural()) {
            space = 2500.0;
            surface = 64.0;
        } else {
            space = 2000.0;
            surface = 0.0;
        }
        double scale = 1.0;
        double exp = 2.0;
        double posY = entity.getY();
        if (posY <= surface) {
            return scale;
        } else if (posY > space) {
            return 0.0;
        } else {
            posY -= surface;
            return Math.pow(Math.abs(posY - space), exp) * Math.pow(space, -exp);
        }
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
