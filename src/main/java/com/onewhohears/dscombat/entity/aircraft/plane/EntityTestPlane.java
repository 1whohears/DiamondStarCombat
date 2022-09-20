package com.onewhohears.dscombat.entity.aircraft.plane;

import com.onewhohears.dscombat.data.BulletData;
import com.onewhohears.dscombat.data.PartsManager;
import com.onewhohears.dscombat.data.SeatData;

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
		PartsManager pm = this.getPartsManager();
		pm.addPart(new SeatData("pilot_seat", Vec3.ZERO));
		BulletData test = new BulletData("test_bullet", new Vec3(0, 0.5, 1), 600, 100, 100000, 1);
		test.setCurrentAmmo(test.getMaxAmmo());
		pm.getWeapons().addWeapon(test);
		pm.getWeapons().get("test_bullet").setMaxAmmo(10000);
		pm.getWeapons().get("test_bullet").setCurrentAmmo(10000);
		super.setupAircraftParts();
	}

}
