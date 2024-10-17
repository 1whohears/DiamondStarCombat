package com.onewhohears.dscombat.entity.weapon;

import java.util.Iterator;

import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.stats.BunkerBusterStats;

import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EntityBunkerBuster<T extends BunkerBusterStats> extends EntityBomb<T> {
	
	public static final EntityDataAccessor<Integer> BLOCK_STRENGTH = SynchedEntityData.defineId(EntityBunkerBuster.class, EntityDataSerializers.INT);

	public EntityBunkerBuster(EntityType<? extends EntityBunkerBuster<?>> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
		if (getWeaponStats() != null) setBlockStrength(getWeaponStats().getBlockStrength());
	}
	
	@Override
	public WeaponType getWeaponType() {
		return WeaponType.BUNKER_BUSTER;
	}
	
	@Override
	protected BlockHitResult checkBlockCollide() {
		Iterator<VoxelShape> it = level().getBlockCollisions(this, getBoundingBox().expandTowards(getDeltaMovement())).iterator();
		Entity owner = getOwner();
		while (it.hasNext()) {
			VoxelShape voxel = it.next();
			BlockPos pos = new BlockPos(voxel.bounds().getCenter());
			BlockState state = level().getBlockState(pos);
			int hit_block_strength = getBlockStrength(pos, state);
			if (getBlockStrength() >= hit_block_strength &&
					UtilVehicleEntity.hasPermissionToBreakBlock(pos, state, level(), owner)) {
				level().destroyBlock(pos, true, this);
				reduceBlockStrength(hit_block_strength);
			} else {
				return new BlockHitResult(voxel.bounds().getCenter(), getDirection(), pos, false);
			}
		}
		Vec3 p = position().add(getDeltaMovement());
		return BlockHitResult.miss(p, getDirection(), new BlockPos(p));
	}
	
	protected int getBlockStrength(BlockPos pos, BlockState state) {
		if (state.is(Blocks.BEDROCK)) return Integer.MAX_VALUE;
		if (state.is(ModTags.Blocks.ABSORBENT)) return 50;
		return (int) state.getExplosionResistance(level(), pos, null);
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(BLOCK_STRENGTH, 0);
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		setBlockStrength(compound.getInt("blockStrength"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("blockStrength", getBlockStrength());
	}
	
	public int getBlockStrength() {
		return entityData.get(BLOCK_STRENGTH);
	}
	
	public void setBlockStrength(int block_strength) {
		entityData.set(BLOCK_STRENGTH, block_strength);
	}
	
	public void reduceBlockStrength(int num) {
		setBlockStrength(Math.max(getBlockStrength()-num, 0));
	}

}
