package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.weapon.WeaponSystem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityTurrent extends EntitySeat {
	
	public final String weaponId;
	
	public final WeaponSystem weaponSystem = new WeaponSystem();
	
	public EntityTurrent(EntityType<?> type, Level level, String weaponId) {
		super(type, level);
		this.weaponId = weaponId;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
	}
	
	public void init() {
		super.init();
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public boolean shouldRender() {
		return true;
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 25600;
	}

}
