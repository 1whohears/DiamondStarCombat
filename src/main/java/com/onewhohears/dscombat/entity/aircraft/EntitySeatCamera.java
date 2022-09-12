package com.onewhohears.dscombat.entity.aircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class EntitySeatCamera extends Entity {

	public EntitySeatCamera(EntityType<? extends EntitySeatCamera> p_19870_, Level p_19871_) {
		super(p_19870_, p_19871_);
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
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (this.getVehicle() == null) this.kill();
	}

}
