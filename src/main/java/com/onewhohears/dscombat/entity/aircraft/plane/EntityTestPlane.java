package com.onewhohears.dscombat.entity.aircraft.plane;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.BulletData;
import com.onewhohears.dscombat.data.MissileData;
import com.onewhohears.dscombat.data.MissileData.GuidanceType;
import com.onewhohears.dscombat.data.MissileData.TargetType;
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
	
	/*@Override
	protected void setupAircraftParts() {
		super.setupAircraftParts();
	}*/

	@Override
	public ResourceLocation getTexture() {
		return TEXTURE;
	}

	@Override
	public EntityModel<?> getModel() {
		return MODEL;
	}

}
