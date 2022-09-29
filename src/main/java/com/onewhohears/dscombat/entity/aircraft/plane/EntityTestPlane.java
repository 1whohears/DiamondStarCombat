package com.onewhohears.dscombat.entity.aircraft.plane;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.BulletData;
import com.onewhohears.dscombat.data.MissileData;
import com.onewhohears.dscombat.data.PartsManager;
import com.onewhohears.dscombat.data.RadarData;
import com.onewhohears.dscombat.data.SeatData;

import net.minecraft.client.model.EntityModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityTestPlane extends EntityAbstractPlane {
	
	public static final ResourceLocation TEXTURE = new ResourceLocation(DSCombatMod.MODID, "textures/entities/basic_plane.png");
	public static EntityModel<EntityTestPlane> MODEL = null;
	
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
				600, 100000, 1, 10, 4);
		pm.getWeapons().addWeapon(test);
		test.setCurrentAmmo(test.getMaxAmmo());
		BulletData test2 = new BulletData("bullet2", new Vec3(0, 0.5, 1),
				600, 100000, 15, 100, 4, true, true, false, 100d, 4f);
		test2.setCurrentAmmo(test2.getMaxAmmo());
		pm.getWeapons().addWeapon(test2);
		MissileData test3 = new MissileData("bullet3", new Vec3(0, 0.5, 1),
				600, 100000, 15, 1000, 1d, true, true, false, 100d, 8f,
				MissileData.TargetType.GROUND, MissileData.GuidanceType.PITBULL,
				0.1f, 0.2d, 1.0d);
		test3.setCurrentAmmo(test3.getMaxAmmo());
		pm.getWeapons().addWeapon(test3);
		RadarData radar = new RadarData("main-radar", 1000, 90, 20);
		radar.setScanAircraft(true);
		radar.setScanPlayers(true);
		radar.setScanMobs(true);
		pm.addPart(radar);
		super.setupAircraftParts();
	}

	@Override
	public ResourceLocation getTexture() {
		return TEXTURE;
	}

	@Override
	public EntityModel<?> getModel() {
		return MODEL;
	}

}
