package com.onewhohears.dscombat.entity.aircraft.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class EntityAbstractPart extends Entity {

	public EntityAbstractPart(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		
	}

	@Override
	protected void defineSynchedData() {
		
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound) {
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound) {
		
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	public void init() {
		
	}
	
	@Override
	public void tick() {
		if (this.firstTick) init();
		super.tick();
		if (!this.level.isClientSide && this.getVehicle() == null) this.kill();
	}

}
