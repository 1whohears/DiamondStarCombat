package com.onewhohears.dscombat.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class EntityParachute extends Entity {
	
	public static final float FALL_SPEED = -0.10f;
	
	public EntityParachute(EntityType<?> type, Level level) {
		super(type, level);
		this.blocksBuilding = true;
	}
	
	@Override
	public void tick() {
		super.tick();
		tickMovement();
		move(MoverType.SELF, getDeltaMovement());
		tickCollide();
		tickLerp();
	}
	
	public void tickMovement() {
		Vec3 move = getDeltaMovement();
		float mx = Mth.approach((float)move.x, 0, 0.005f);
		float my = Mth.approach((float)move.y, FALL_SPEED, 0.006f);
		float mz = Mth.approach((float)move.z, 0, 0.005f);
		setDeltaMovement(new Vec3(mx, my, mz));
	}
	
	public void tickCollide() {
		if (!level.isClientSide && verticalCollisionBelow) {
			kill();
		}
	}
	
	private void tickLerp() {
		if (isControlledByLocalInstance()) {
			syncPacketPositionCodec(getX(), getY(), getZ());
			return;
		}
	}
	
	@Override
    public double getPassengersRidingOffset() {
        return 0;
    }
	
	@Override
    public boolean canBeCollidedWith() {
        return true;
    }
	
	@Override
    public boolean isPushedByFluid() {
    	return true;
    }
	
	@Override
    public boolean canTrample(BlockState state, BlockPos pos, float fallDistance) {
    	return true;
    }

	@Override
	protected void defineSynchedData() {
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag nbt) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
