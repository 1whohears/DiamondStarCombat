package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.BulletData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityBullet extends EntityAbstractWeapon {
	
	public EntityBullet(EntityType<? extends EntityBullet> type, Level level) {
		super(type, level);
	}
	
	public EntityBullet(Level level, Entity owner, BulletData data) {
		super(level, owner, data);
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setWeaponData(new BulletData(compound.getCompound("weapondata")));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.put("weapondata", this.getWeaponData().write());
	}

}
