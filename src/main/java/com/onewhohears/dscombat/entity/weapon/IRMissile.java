package com.onewhohears.dscombat.entity.weapon;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.stats.IRMissileStats;
import com.onewhohears.dscombat.entity.IREmitter;
import com.onewhohears.dscombat.entity.damagesource.WeaponDamageSource;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.onewholibs.util.UtilEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class IRMissile<T extends IRMissileStats> extends EntityMissile<T> {
	
	public IRMissile(EntityType<? extends IRMissile<?>> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
	}
	
	@Override
	public WeaponType getWeaponType() {
		return WeaponType.IR_MISSILE;
	}
	
	@Override
	public void tickGuide() {
		if (tickCount % 10 == 0) findIrTarget();
		if (target != null) guideToTarget();
	}
	
	protected List<IrTarget> targets = new ArrayList<IrTarget>();
	
	public static void updateIRTargetsList(Entity weapon, List<IrTarget> targets, float flareResistance, float fov) {
		targets.clear();
		List<Entity> irEmitters = weapon.level().getEntities(weapon, getIrBoundingBox(weapon),
				(entity) -> entity.getType().is(ModTags.EntityTypes.IR_EMITTER));
		for (int i = 0; i < irEmitters.size(); ++i) {
			Entity emitter = irEmitters.get(i);
			if (emitter.isPassenger()) continue;
			if (!basicCheck(weapon, emitter, true, fov)) continue;
			float distSqr = (float)weapon.distanceToSqr(emitter);
			float heat = getEntityHeat(emitter, flareResistance);
			targets.add(new IrTarget(emitter, heat / distSqr));
		}
	}
	
	public static float getEntityHeat(Entity entity, float flareResistance) {
		float heat = 0;
		if (entity instanceof IREmitter ir) heat = ir.getIRHeat();
		else if (entity.getType().is(ModTags.EntityTypes.IR_EMITTER_EXTREME)) heat = 100;
		else if (entity.getType().is(ModTags.EntityTypes.IR_EMITTER_HIGH)) heat = 20;
		else if (entity.getType().is(ModTags.EntityTypes.IR_EMITTER_MED)) heat = 5;
		else if (entity.getType().is(ModTags.EntityTypes.IR_EMITTER_LOW)) heat = 1;
		if (entity.getType().is(ModTags.EntityTypes.FLARE)) heat *= flareResistance;
		return heat;
	}
	
	protected void findIrTarget() {
		updateIRTargetsList(this, targets, getWeaponStats().getFlareResistance(), getWeaponStats().getFov());
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
		if (checkGround && ping.onGround()) {
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
