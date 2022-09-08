package com.onewhohears.dscombat.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntitySeat extends Entity {

	public EntitySeat(EntityType<?> p_19870_, Level p_19871_) {
		super(p_19870_, p_19871_);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void defineSynchedData() {
		
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag p_20052_) {
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag p_20139_) {
		
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		// TODO Auto-generated method stub
		return null;
	}

}
