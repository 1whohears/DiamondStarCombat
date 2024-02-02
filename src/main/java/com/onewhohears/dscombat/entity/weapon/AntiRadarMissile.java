package com.onewhohears.dscombat.entity.weapon;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.data.weapon.AntiRadarMissileData;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class AntiRadarMissile extends EntityMissile {
	
	protected AntiRadarMissileData antiRadMisData;
	
	public AntiRadarMissile(EntityType<? extends AntiRadarMissile> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
	}
	
	@Override
	protected void castWeaponData() {
		super.castWeaponData();
		antiRadMisData = (AntiRadarMissileData)weaponData;
	}
	
	@Override
	public void tickGuide() {
		if (tickCount % 10 == 0) findARTarget();
		if (target != null) guideToTarget();
	}
	
	@Override
	public boolean dieIfNoTargetOutsideTickRange() {
		return false;
	}
	
	protected List<ARTarget> targets = new ArrayList<ARTarget>();
	
	protected void findARTarget() {
		// IDEA 7 make anti radar missile target entity type configurable so entities other mod entities can be targeted
		targets.clear();
		// planes
		List<EntityVehicle> planes = level.getEntitiesOfClass(
				EntityVehicle.class, getARBoundingBox());
		for (int i = 0; i < planes.size(); ++i) {
			if (!planes.get(i).radarSystem.hasRadar()) continue;
			if (!planes.get(i).radarSystem.canServerTick()) continue;
			if (!basicCheck(planes.get(i))) continue;
			float distSqr = (float)distanceToSqr(planes.get(i));
			targets.add(new ARTarget(planes.get(i), 
				(float)planes.get(i).radarSystem.getMaxAirRange() / distSqr));
		}
		// pick target
		if (targets.size() == 0) {
			this.target = null;
			this.targetPos = null;
			//System.out.println("NO TARGET");
			return;
		}
		ARTarget max = targets.get(0);
		for (int i = 1; i < targets.size(); ++i) 
			if (targets.get(i).radiation > max.radiation) 
				max = targets.get(i);
		this.target = max.entity;
		this.targetPos = max.entity.position();
	}
	
	protected boolean basicCheck(Entity ping) {
		//System.out.println("target? "+ping);
		if (!ping.isOnGround()) {
			return false;
		}
		if (isAlliedTo(ping)) {
			//System.out.println("is allied");
			return false;
		}
		if (!checkTargetRange(ping, antiRadMisData.getScanRange())) {
			//System.out.println("not in cone");
			return false;
		}
		if (!checkCanSee(ping)) {
			//System.out.println("can't see");
			return false;
		}
		//System.out.println("POSSIBLE");
		return true;
	}
	
	protected AABB getARBoundingBox() {
		double x = getX();
		double y = getY();
		double z = getZ();
		double w = antiRadMisData.getScanRange();
		return new AABB(x+w, y+w, z+w, x-w, y-w, z-w);
	}
	
	public static class ARTarget {
		
		public final Entity entity;
		public final float radiation;
		
		public ARTarget(Entity entity, float radiation) {
			this.entity = entity;
			this.radiation = radiation;
		}
		
	}
	
	@Override
	public WeaponData.WeaponType getWeaponType() {
		return WeaponData.WeaponType.ANTIRADAR_MISSILE;
	}

}
