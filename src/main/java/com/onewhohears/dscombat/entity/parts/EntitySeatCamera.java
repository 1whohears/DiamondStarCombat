package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class EntitySeatCamera extends Entity {

	public EntitySeatCamera(EntityType<? extends EntitySeatCamera> type, Level level) {
		super(type, level);
		this.setInvulnerable(true);
		this.noPhysics = true;
	}
	
	public EntitySeatCamera(Level level) {
		super(ModEntities.CAMERA.get(), level);
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
		if (!this.level.isClientSide && this.getVehicle() == null) this.discard();
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
		return false;
	}
	
	@Override
	public boolean isAttackable() {
		return false;
	}
	
	@Override
	public double getMyRidingOffset() {
		return 1.62 - 0.35;
	}

}
