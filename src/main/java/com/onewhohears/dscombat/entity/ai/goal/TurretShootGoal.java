package com.onewhohears.dscombat.entity.ai.goal;

import javax.annotation.Nonnull;

import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.util.UtilEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.phys.Vec3;

public class TurretShootGoal extends Goal {
	
	public static final ShootFunction CANT_SHOOT = (mob, turret, target) -> {
	};
	
	public static final ShootFunction RANDOM_SHOOT = (mob, turret, target) -> {
		Vec3 randPos = mob.position().add(turret.getLookAngle().scale(40))
			.add((Math.random()-0.5)*10, (Math.random()-0.25)*10, (Math.random()-0.5)*10);
		UtilEntity.mobLookAtPos(mob, randPos, 360);
		turret.shoot(mob);
	};
	
	public static final ShootFunction DUMBASS_SHOOT = (mob, turret, target) -> {
		Vec3 randPos = target.getEyePosition().add((Math.random()-0.5)*10, (Math.random()-0.5)*5, (Math.random()-0.5)*10);
		UtilEntity.mobLookAtPos(mob, randPos, mob.getHeadRotSpeed());
		turret.shoot(mob);
	};
	
	public static final ShootFunction STUPID_SHOOT = (mob, turret, target) -> {
		// TODO 6.3 more accurate but still random aiming
		UtilEntity.mobLookAtPos(mob, target.getEyePosition(), mob.getHeadRotSpeed());
		turret.shoot(mob);
	};
	
	public static final ShootFunction NORMAL_SHOOT = (mob, turret, target) -> {
		UtilEntity.mobLookAtPos(mob, target.getEyePosition(), mob.getHeadRotSpeed());
		turret.shoot(mob);
	};
	
	public static final ShootFunction SMART_SHOOT = (mob, turret, target) -> {
		UtilEntity.mobLookAtPos(mob, target.getEyePosition(), mob.getHeadRotSpeed());
		// TODO 6.2 aim up to correct for distance
		turret.shoot(mob);
	};
	
	public static ShootFunction getShootFunctionByMob(Mob mob) {
		if (mob instanceof Enemy) {
			if (mob instanceof Creeper) return RANDOM_SHOOT;
			else return NORMAL_SHOOT;
		} else if (mob instanceof AbstractGolem) {
			return NORMAL_SHOOT;
		}
		// TODO 6.1 more levels of mobs turret shooting at targets AI types
		return CANT_SHOOT;
	}
	
	public interface ShootFunction {
		void shoot(Mob mob, EntityTurret turret, @Nonnull LivingEntity target);
	}
	
	private final EntityTurret turret;
	private final Mob mob;
	private final ShootFunction shootFunction;
	
	public TurretShootGoal(Mob mob, EntityTurret turret) {
		this.mob = mob;
		this.turret = turret;
		this.shootFunction = getShootFunctionByMob(mob);
	}
	
	@Override
	public void tick() {
		super.tick();
		LivingEntity target = mob.getTarget();
		if (target == null) return;
		shootFunction.shoot(mob, turret, target);;
	}
	
	@Override
	public boolean canUse() {
		if (mob.getVehicle() == null || !mob.getVehicle().equals(turret)) return false;
		Entity target_entity = mob.getTarget();
		return target_entity != null && target_entity.isAlive();
	}
	
	@Override
	public boolean canContinueToUse() {
		return canUse();
	}
	
	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

}
