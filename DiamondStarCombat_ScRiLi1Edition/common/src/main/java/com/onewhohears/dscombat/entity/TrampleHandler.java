package com.onewhohears.dscombat.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface TrampleHandler {
    boolean canTrample(BlockState state, BlockPos pos, float fallDistance);
}
