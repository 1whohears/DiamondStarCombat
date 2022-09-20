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
		BulletData test = new BulletData("bullet1", new Vec3(0, 0.5, 1), 
				600, 100000, 1000, 1);
		pm.getWeapons().addWeapon(test);
		test.setCurrentAmmo(test.getMaxAmmo());
		BulletData test2 = new BulletData("bullet2", new Vec3(0, 0.5, 1),
				600, 100000, 1000, 1, true, true, false, 1000d, 5f);
		test2.setCurrentAmmo(test2.getMaxAmmo());
		pm.getWeapons().addWeapon(test2);
		super.setupAircraftParts();
	}

}
