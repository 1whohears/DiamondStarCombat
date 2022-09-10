package com.onewhohears.dscombat.entity;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.init.DataSerializers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntitySeat extends Entity {
	
	public static final EntityDataAccessor<Vec3> POS = SynchedEntityData.defineId(EntityAbstractAircraft.class, DataSerializers.VEC3);
	
	public EntitySeat(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(POS, Vec3.ZERO);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		double x = compound.getDouble("relposx");
		double y = compound.getDouble("relposy");
		double z = compound.getDouble("relposz");
		setRelativeSeatPos(new Vec3(x, y, z));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		Vec3 pos = getRelativeSeatPos();
		compound.putDouble("relposx", pos.x);
		compound.putDouble("relposy", pos.y);
		compound.putDouble("relposz", pos.z);
	}
	
	@Override
	public void tick() {
		super.tick();
		//System.out.println("SEAT POS "+this.position());
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		} else if (!this.level.isClientSide) {
			return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
		} else {
			return InteractionResult.SUCCESS;
		}
	}
	
	public Vec3 getRelativeSeatPos() {
		return entityData.get(POS);
	}
	
	public void setRelativeSeatPos(Vec3 pos) {
		entityData.set(POS, pos);
	}
	
	@Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        System.out.println("SEAT ADDED PASSENGER "+passenger);
	}
	
	@Override
    public void positionRider(Entity passenger) {
		super.positionRider(passenger);
	}
	
	@Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
		// TODO get dismount location for root plane
		return super.getDismountLocationForPassenger(livingEntity);
	}
	
	@Override
    protected boolean canAddPassenger(Entity passenger) {
		return getPassengers().size() < 1;
	}
	
	@Nullable
	@Override
    public Entity getControllingPassenger() {
        List<Entity> list = getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }
	
	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}
	
	@Override
    protected boolean canRide(Entity entityIn) {
        return getPassengers().size() < 1;
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return true;
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
    public boolean hurt(DamageSource source, float amount) {
		return false;
	}
	
	@Override
	public String toString() {
		return "seat "+this.getRelativeSeatPos();
	}

}
