package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.TrackMissileData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class TrackEntityMissile extends EntityMissile {
	
	/**
	 * only set on server side
	 */
	protected boolean active;
	
	public TrackEntityMissile(EntityType<? extends TrackEntityMissile> type, Level level) {
		super(type, level);
	}
	
	public TrackEntityMissile(Level level, Entity owner, TrackMissileData data) {
		super(level, owner, data);
		active = data.isActiveTrack();
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		active = compound.getBoolean("active_track");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("active_track", active);
	}

	@Override
	public void tickGuide() {
		if (!active && !level.isClientSide) notActiveCheckTarget();
		guideToTarget();
		if (!level.isClientSide && tickCount % 10 == 0 && target instanceof EntityVehicle plane) {
			plane.trackedByMissile(position());
		}
	}
	
	public void notActiveCheckTarget() {
		if (target == null || tickCount % 10 != 0) return;
		Entity owner = getOwner();
		if (owner == null) {
			target = null;
			return;
		}
		if (!(owner.getRootVehicle() instanceof EntityVehicle plane)) {
			target = null;
			return;
		}
		if (!plane.radarSystem.hasTarget(target.getId())) {
			target = null;
			return;
		}
	}

}
