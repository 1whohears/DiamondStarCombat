package com.onewhohears.dscombat.entity.weapon;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.data.weapon.IRMissileData;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
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
		if (tickCount < 10) return;
		if (tickCount % 10 == 0) findIrTarget();
		if (target != null) guideToTarget();
	}
	
	protected List<IrTarget> targets = new ArrayList<IrTarget>();
	
	protected void findIrTarget() {
		targets.clear();
		// planes
		List<EntityAircraft> planes = level.getEntitiesOfClass(
				EntityAircraft.class, getIrBoundingBox());
		for (int i = 0; i < planes.size(); ++i) {
			if (planes.get(i).isVehicleOf(getOwner())) continue;
			if (!basicCheck(planes.get(i), true)) continue;
			float distSqr = (float)distanceToSqr(planes.get(i));
			targets.add(new IrTarget(planes.get(i), planes.get(i).getHeat() / distSqr));
		}
		// players
		List<Player> players = level.getEntitiesOfClass(
				Player.class, getIrBoundingBox());
		for (int i = 0; i < players.size(); ++i) {
			if (players.get(i).getRootVehicle() instanceof EntityAircraft) continue;
			if (!basicCheck(players.get(i), true)) continue;
			float distSqr = (float)distanceToSqr(players.get(i));
			targets.add(new IrTarget(players.get(i), 1f / distSqr));
		}
		// mobs
		List<Mob> mobs = level.getEntitiesOfClass(
				Mob.class, getIrBoundingBox());
		for (int i = 0; i < mobs.size(); ++i) {
			if (mobs.get(i).getRootVehicle() instanceof EntityAircraft) continue;
			if (!basicCheck(mobs.get(i), true)) continue;
			float distSqr = (float)distanceToSqr(mobs.get(i));
			targets.add(new IrTarget(mobs.get(i), getMobHeat(mobs.get(i)) / distSqr));
		}
		// missiles
		List<EntityMissile> missiles = level.getEntitiesOfClass(
				EntityMissile.class, getIrBoundingBox());
		for (int i = 0; i < missiles.size(); ++i) {
			if (this.equals(missiles.get(i))) continue;
 			if (!basicCheck(missiles.get(i), false)) continue;
 			if (this.getOwner() != null && getOwner().equals(missiles.get(i).getOwner())) continue;
			float distSqr = (float)distanceToSqr(missiles.get(i));
			targets.add(new IrTarget(missiles.get(i), missiles.get(i).getHeat() / distSqr));
		}	
		// flares
		List<EntityFlare> flares = level.getEntitiesOfClass(
				EntityFlare.class, getIrBoundingBox());
		for (int i = 0; i < flares.size(); ++i) {
			if (!basicCheck(flares.get(i), false)) continue;
			float distSqr = (float)distanceToSqr(flares.get(i));
			targets.add(new IrTarget(flares.get(i), 
					flares.get(i).getHeat() / distSqr * flareResistance));
		}
		// fire arrows
		List<AbstractArrow> arrows = level.getEntitiesOfClass(
				AbstractArrow.class, getIrBoundingBox());
		for (int i = 0; i < arrows.size(); ++i) {
			if (!arrows.get(i).isOnFire()) continue;
			if (!basicCheck(arrows.get(i), false)) continue;
			float distSqr = (float)distanceToSqr(arrows.get(i));
			targets.add(new IrTarget(arrows.get(i), 
					2f / distSqr * flareResistance));
		}
		// fire works
		List<FireworkRocketEntity> foreworks = level.getEntitiesOfClass(
				FireworkRocketEntity.class, getIrBoundingBox());
		for (int i = 0; i < foreworks.size(); ++i) {
			if (!basicCheck(foreworks.get(i), false)) continue;
			float distSqr = (float)distanceToSqr(foreworks.get(i));
			targets.add(new IrTarget(foreworks.get(i), 
					2f / distSqr * flareResistance));
		}
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
	
	protected boolean basicCheck(Entity ping, boolean checkGround) {
		//System.out.println("target? "+ping);
		if (checkGround && ping.isOnGround()) {
			//System.out.println("on ground");
			return false;
		}
		if (ping.isInWater()) {
			return false;
		}
		if (ping.equals(getOwner())) {
			//System.out.println("is owner");
			return false;
		}
		if (!checkTargetRange(ping, IR_RANGE)) {
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
	
	protected static float getMobHeat(Mob mob) {
		if (mob.getType().equals(EntityType.BLAZE)) return 10f;
		if (mob.getType().equals(EntityType.MAGMA_CUBE)) return 8f;
		if (mob.getType().equals(EntityType.WITHER)) return 200f;
		if (mob.getType().equals(EntityType.ENDER_DRAGON)) return 100f;
		return 1f;
	}
	
	protected static final double IR_RANGE = 300d;
	
	protected AABB getIrBoundingBox() {
		double x = getX();
		double y = getY();
		double z = getZ();
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

}
