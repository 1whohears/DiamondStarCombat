package com.onewhohears.dscombat.entity.aircraft.plane;

import com.onewhohears.dscombat.data.BulletData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityTestPlane extends EntityAbstractPlane {

	public EntityTestPlane(EntityType<? extends EntityAbstractPlane> entity, Level level) {
		super(entity, level);
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
	}
	
	@Override
	protected void setupAircraftParts() {
		super.setupAircraftParts();
		this.addSeat(new Vec3(0, 0, 0));
		this.getWeaponSystem().addWeapon(new BulletData(
				"test_bullet", new Vec3(0, 0.5, -1), 600, 1, 1));
	}

}
