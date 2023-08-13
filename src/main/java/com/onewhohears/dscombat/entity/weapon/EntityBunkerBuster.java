package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.BunkerBusterData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class EntityBunkerBuster extends EntityBomb {
	
	public static final EntityDataAccessor<Integer> BLOCK_STRENGTH = SynchedEntityData.defineId(EntityBullet.class, EntityDataSerializers.INT);
	
	public EntityBunkerBuster(EntityType<? extends EntityBunkerBuster> type, Level level) {
		super(type, level);
	}
	
	public EntityBunkerBuster(Level level, Entity owner, BunkerBusterData data) {
		super(level, owner, data);
		this.setBlockStrength(data.getBlockStrength());
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	protected void tickCheckCollide() {
		super.tickCheckCollide();
	}
	
	@Override
	protected void tickSetMove() {
		super.tickSetMove();
	}
	
	@Override
	protected BlockHitResult checkBlockCollide() {
		BlockHitResult hit = super.checkBlockCollide();
		while (hit.getType() != HitResult.Type.MISS) {
			int hit_block_strength = BunkerBusterData.getBlockStrength(level.getBlockState(hit.getBlockPos()));
			if (getBlockStrength() >= hit_block_strength) {
				level.destroyBlock(hit.getBlockPos(), true, this);
				reduceBlockStrength(hit_block_strength);
			} else return hit;
			hit = super.checkBlockCollide();
		}
		return hit;
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(BLOCK_STRENGTH, 0);
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setBlockStrength(compound.getInt("blockStrength"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("blockStrength", this.getBlockStrength());
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
