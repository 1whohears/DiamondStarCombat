package com.onewhohears.dscombat.entity.weapon;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.damagesource.WeaponDamageSource;
import com.onewhohears.dscombat.data.weapon.IRMissileData;
import com.onewhohears.dscombat.data.weapon.RadarTargetTypes;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.UtilEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class IRMissile extends EntityMissile {
	
	/**
	 * only set on server side
	 */
	protected final float flareResistance;
	
	public IRMissile(EntityType<? extends IRMissile> type, Level level) {
		super(type, level);
		flareResistance = 0;
	}
	
	public IRMissile(Level level, Entity owner, IRMissileData data) {
		super(level, owner, data);
		flareResistance = data.getFlareResistance();
	}
	
	@Override
	public void tickGuide() {
		if (tickCount % 10 == 0) findIrTarget();
		if (target != null) guideToTarget();
	}
	
	protected List<IrTarget> targets = new ArrayList<IrTarget>();
	
	public static void updateIRTargetsList(Entity weapon, List<IrTarget> targets, float flareResistance, float fov) {
		targets.clear();
		// planes
		List<EntityAircraft> planes = weapon.level.getEntitiesOfClass(
				EntityAircraft.class, getIrBoundingBox(weapon));
		for (int i = 0; i < planes.size(); ++i) {
			if (!basicCheck(weapon, planes.get(i), true, fov)) continue;
			float distSqr = (float)weapon.distanceToSqr(planes.get(i));
			targets.add(new IrTarget(planes.get(i), planes.get(i).getHeat() / distSqr));
		}	
		// flares
		List<EntityFlare> flares = weapon.level.getEntitiesOfClass(
				EntityFlare.class, getIrBoundingBox(weapon));
		for (int i = 0; i < flares.size(); ++i) {
			if (!basicCheck(weapon, flares.get(i), false, fov)) continue;
			float distSqr = (float)weapon.distanceToSqr(flares.get(i));
			targets.add(new IrTarget(flares.get(i), 
					flares.get(i).getHeat() / distSqr * flareResistance));
		}
		// other
		for (int j = 0; j < RadarTargetTypes.get().getIrEntityClasses().size(); ++j) {
			Class<? extends Entity> clazz = RadarTargetTypes.get().getIrEntityClasses().get(j);
			float heat = RadarTargetTypes.get().getIrEntityHeats().get(j);
			List<? extends Entity> entities = weapon.level.getEntitiesOfClass(
					clazz, getIrBoundingBox(weapon));
			for (int i = 0; i < entities.size(); ++i) {
				if (entities.get(i).isPassenger()) continue;
				if (!basicCheck(weapon, entities.get(i), true, fov)) continue;
				float distSqr = (float)weapon.distanceToSqr(entities.get(i));
				targets.add(new IrTarget(entities.get(i), 
						getSpecificEntityHeat(entities.get(i), heat) / distSqr));
			}
		}
	}
	
	protected void findIrTarget() {
		updateIRTargetsList(this, targets, flareResistance, fov);
		// pick target
		if (targets.size() == 0) {
			this.target = null;
			this.targetPos = null;
			//System.out.println("NO TARGET");
			return;
		}
		IrTarget max = targets.get(0);
		for (int i = 1; i < targets.size(); ++i) {
			if (targets.get(i).heat > max.heat) max = targets.get(i);
		}
		this.target = max.entity;
		this.targetPos = max.entity.position();
		//System.out.println("TARGET FOUND "+missile.target);
	}
	
	protected static boolean basicCheck(Entity weapon, Entity ping, boolean checkGround, float fov) {
		//System.out.println("target? "+ping);
		if (weapon.equals(ping)) {
			//System.out.println("same");
			return false;
		}
		if (checkGround && ping.isOnGround()) {
			//System.out.println("on ground");
			return false;
		}
		if (ping.isInWater()) {
			return false;
		}
		if (weapon.isAlliedTo(ping)) {
			//System.out.println("is allied");
			return false;
		}
		if (!checkTargetRange(weapon, ping, fov, IR_RANGE)) {
			//System.out.println("not in cone");
			return false;
		}
		if (!UtilEntity.canEntitySeeEntity(weapon, ping, 
				Config.COMMON.maxBlockCheckDepth.get(), 0, 0)) {
			//System.out.println("can't see");
			return false;
		}
		//System.out.println("POSSIBLE");
		return true;
	}
	
	protected static float getSpecificEntityHeat(Entity e, float instead) {
		return RadarTargetTypes.get().getEntityHeat(EntityType.getKey(e.getType()).toString(), instead);
	}
	
	public static final double IR_RANGE = 300d;
	
	public static AABB getIrBoundingBox(Entity e) {
		double x = e.getX();
		double y = e.getY();
		double z = e.getZ();
		double w = IR_RANGE;
		return new AABB(x+w, y+w, z+w, x-w, y-w, z-w);
	}
	
	public static class IrTarget {
		
		public final Entity entity;
		public final float heat;
		
		public IrTarget(Entity entity, float heat) {
			this.entity = entity;
			this.heat = heat;
		}
		
	}
	
	@Override
	protected WeaponDamageSource getImpactDamageSource() {
		return WeaponDamageSource.WeaponDamageType.MISSILE_CONTACT.getSource(getOwner(), this);
	}

	@Override
	protected WeaponDamageSource getExplosionDamageSource() {
		return WeaponDamageSource.WeaponDamageType.IR_MISSILE.getSource(getOwner(), this);
	}

}
