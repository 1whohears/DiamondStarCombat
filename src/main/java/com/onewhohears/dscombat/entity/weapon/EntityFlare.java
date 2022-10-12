package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class EntityFlare extends Entity {

	public EntityFlare(EntityType<?extends EntityFlare> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	public EntityFlare(Level level) {
		this(ModEntities.FLARE.get(), level);
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
	
	public float getHeat() {
		return 1.5f;
	}
	
	@Override
	public void tick() {
		super.tick();
	}

}
