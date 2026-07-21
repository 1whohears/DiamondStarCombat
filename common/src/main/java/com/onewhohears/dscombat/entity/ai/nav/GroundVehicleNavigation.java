package com.onewhohears.dscombat.entity.ai.nav;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.Vec3;

public class GroundVehicleNavigation extends VehicleNavigation {

    public GroundVehicleNavigation(EntityVehicle vehicle, Level level) {
        super(vehicle, level);
    }

    protected VehiclePathFinder createPathFinder(int i) {
        this.nodeEvaluator = new DriveNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new VehiclePathFinder(this.nodeEvaluator, i);
    }

    protected boolean canUpdatePath() {
        return this.vehicle.onGround() || this.isInLiquid() || this.vehicle.isPassenger();
    }

    protected Vec3 getTempMobPos() {
        return new Vec3(this.vehicle.getX(), this.getSurfaceY(), this.vehicle.getZ());
    }

    public Path createPath(BlockPos blockPos, int i) {
        if (this.level.getBlockState(blockPos).isAir()) {
            BlockPos blockPos2;
            for(blockPos2 = blockPos.below(); blockPos2.getY() > this.level.getMinBuildHeight() && this.level.getBlockState(blockPos2).isAir(); blockPos2 = blockPos2.below()) {
            }

            if (blockPos2.getY() > this.level.getMinBuildHeight()) {
                return super.createPath(blockPos2.above(), i);
            }

            while(blockPos2.getY() < this.level.getMaxBuildHeight() && this.level.getBlockState(blockPos2).isAir()) {
                blockPos2 = blockPos2.above();
            }

            blockPos = blockPos2;
        }

        if (!this.level.getBlockState(blockPos).isSolid()) {
            return super.createPath(blockPos, i);
        } else {
            BlockPos blockPos2;
            for(blockPos2 = blockPos.above(); blockPos2.getY() < this.level.getMaxBuildHeight() && this.level.getBlockState(blockPos2).isSolid(); blockPos2 = blockPos2.above()) {
            }

            return super.createPath(blockPos2, i);
        }
    }

    public Path createPath(Entity entity, int i) {
        return this.createPath(entity.blockPosition(), i);
    }

    private int getSurfaceY() {
        if (this.vehicle.isInWater() && this.canFloat()) {
            int i = this.vehicle.getBlockY();
            BlockState blockState = this.level.getBlockState(BlockPos.containing(this.vehicle.getX(), (double)i, this.vehicle.getZ()));
            int j = 0;

            while(blockState.is(Blocks.WATER)) {
                ++i;
                blockState = this.level.getBlockState(BlockPos.containing(this.vehicle.getX(), (double)i, this.vehicle.getZ()));
                ++j;
                if (j > 16) {
                    return this.vehicle.getBlockY();
                }
            }

            return i;
        } else {
            return Mth.floor(this.vehicle.getY() + (double)0.5F);
        }
    }

    protected boolean hasValidPathType(BlockPathTypes blockPathTypes) {
        if (blockPathTypes == BlockPathTypes.WATER) {
            return false;
        } else if (blockPathTypes == BlockPathTypes.LAVA) {
            return false;
        } else {
            return blockPathTypes != BlockPathTypes.OPEN;
        }
    }

    public void setCanOpenDoors(boolean bl) {
        this.nodeEvaluator.setCanOpenDoors(bl);
    }

    public boolean canPassDoors() {
        return this.nodeEvaluator.canPassDoors();
    }

    public void setCanPassDoors(boolean bl) {
        this.nodeEvaluator.setCanPassDoors(bl);
    }

    public boolean canOpenDoors() {
        return this.nodeEvaluator.canPassDoors();
    }

    public void setCanWalkOverFences(boolean bl) {
        this.nodeEvaluator.setCanWalkOverFences(bl);
    }
}
