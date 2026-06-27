package com.onewhohears.dscombat.init.fabric;

import com.onewhohears.dscombat.DSCombatMod;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class ModVillagersImpl {

    public static void platformRegisterPoiType(String name, Supplier<Block> block, int workers, int range) {
        PointOfInterestHelper.register(ResourceLocation.tryBuild(DSCombatMod.MODID, name), workers, range, block.get());
    }

}
