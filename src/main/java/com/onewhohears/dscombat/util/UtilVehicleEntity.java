package com.onewhohears.dscombat.util;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

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

}
