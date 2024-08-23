package com.onewhohears.dscombat.entity.weapon;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.stats.AntiRadarMissileStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class AntiRadarMissile<T extends AntiRadarMissileStats> extends EntityMissile<T> {
	
	public AntiRadarMissile(EntityType<? extends AntiRadarMissile<?>> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
	}
	
	@Override
	public WeaponType getWeaponType() {
		return WeaponType.ANTI_RADAR_MISSILE;
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
		List<EntityVehicle> vehicles = level.getEntitiesOfClass(
				EntityVehicle.class, getARBoundingBox());
		for (int i = 0; i < vehicles.size(); ++i) {
			EntityVehicle vehicle = vehicles.get(i);
			if (!vehicle.radarSystem.hasRadar()) continue;
			if (vehicle.getRadarMode().isOff()) continue;
			if (!vehicle.radarSystem.canServerTick()) continue;
			if (!basicCheck(vehicle)) continue;
			float distSqr = (float)distanceToSqr(vehicle);
			targets.add(new ARTarget(vehicle, 
				(float)vehicle.radarSystem.getMaxAirRange() / distSqr));
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
		if (!checkTargetRange(ping, getWeaponStats().getScanRange())) {
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
		double w = getWeaponStats().getScanRange();
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

}
