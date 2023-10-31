package com.onewhohears.dscombat.entity.ai.goal;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.weapon.BulletData;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.weapon.EntityBullet;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.phys.Vec3;

public class TurretShootGoal extends Goal {
	
	public static final Random RANDOM = new Random();
	
	/**
	 * the mob will never shoot from the turret
	 */
	public static final ShootFunction CANT_SHOOT = (mob, turret, target, prevTargetPos) -> {
		return prevTargetPos;
	};
	
	/**
	 * the mob shoots in a %100 random direction every 1.5 seconds
	 */
	public static final ShootFunction RANDOM_SHOOT = (mob, turret, target, prevTargetPos) -> {
		if (prevTargetPos == null || mob.tickCount % 30 == 0) {
			prevTargetPos = mob.position()
				.add((RANDOM.nextDouble()-0.5)*100, (RANDOM.nextDouble()-0.25)*30, (RANDOM.nextDouble()-0.5)*100);
		}
		UtilEntity.mobLookAtPos(mob, prevTargetPos, mob.getHeadRotSpeed());
		turret.shoot(mob);
		return prevTargetPos;
	};
	
	/**
	 * the mob shoots up to 25 degrees off the target location and updates once a second
	 */
	public static final ShootFunction DUMBASS_SHOOT = (mob, turret, target, prevTargetPos) -> {
		prevTargetPos = inaccurateShootPos(mob, target, prevTargetPos, 20, 25);
		UtilEntity.mobLookAtPos(mob, prevTargetPos, mob.getHeadRotSpeed());
		turret.shoot(mob);
		return prevTargetPos;
	};
	
	/**
	 * the mob shoots up to 10 degrees off the target location and updates twice a second
	 */
	public static final ShootFunction STUPID_SHOOT = (mob, turret, target, prevTargetPos) -> {
		prevTargetPos = inaccurateShootPos(mob, target, prevTargetPos, 10, 10);
		UtilEntity.mobLookAtPos(mob, prevTargetPos, mob.getHeadRotSpeed());
		turret.shoot(mob);
		return prevTargetPos;
	};
	
	/**
	 * the mob shoots up to 5 degrees off the target location and updates twice a second
	 */
	public static final ShootFunction NORMAL_SHOOT = (mob, turret, target, prevTargetPos) -> {
		prevTargetPos = inaccurateShootPos(mob, target, prevTargetPos, 10, 5);
		UtilEntity.mobLookAtPos(mob, prevTargetPos, mob.getHeadRotSpeed());
		turret.shoot(mob);
		return prevTargetPos;
	};
	
	/**
	 * the mob accounts for long distance shots by aiming up a little. 
	 * the mob shoots up to 3 degrees off the target location and updates twice a second
	 */
	public static final ShootFunction SMART_SHOOT = (mob, turret, target, prevTargetPos) -> {
		if (prevTargetPos == null || mob.tickCount % 10 == 0) {
			Vec3 targetPos = target.getEyePosition();
			WeaponData wd = turret.getWeaponData();
			if (wd != null && wd.getType().isBullet()) {
				double speed = ((BulletData)wd).getSpeed();
				if (speed <= 0) speed = 0.01;
				double dist = mob.getEyePosition().distanceTo(targetPos);
				double time = dist / speed;
				double adjust = 0.5*Config.SERVER.accGravity.get()*EntityBullet.BULLET_GRAVITY_SCALE*time*time;
				targetPos = targetPos.add(0, adjust, 0);
			}
			prevTargetPos = UtilGeometry.inaccurateTargetPos(
					mob.getEyePosition(), targetPos, 3);
		}
		UtilEntity.mobLookAtPos(mob, prevTargetPos, mob.getHeadRotSpeed());
		turret.shoot(mob);
		return prevTargetPos;
	};
	
	public static Vec3 inaccurateShootPos(Mob mob, LivingEntity target, Vec3 prevTargetPos, int updateRate, float inaccuracy) {
		if (prevTargetPos == null || mob.tickCount % updateRate == 0) {
			prevTargetPos = UtilGeometry.inaccurateTargetPos(mob.getEyePosition(), 
					target.getEyePosition(), inaccuracy);
		}
		return prevTargetPos;
	}
	
	public static ShootFunction getShootFunctionByMob(Mob mob) {
		if (mob instanceof RangedAttackMob) return SMART_SHOOT;
		else if (mob instanceof Enemy) {
			if (mob instanceof Creeper) return RANDOM_SHOOT;
			else if (mob instanceof Zombie) return STUPID_SHOOT;
			else if (mob instanceof EnderMan) return SMART_SHOOT;
			else return DUMBASS_SHOOT;
		} else if (mob instanceof AbstractGolem) {
			return NORMAL_SHOOT;
		}
		// TODO 6.1 more levels of mobs turret shooting at targets AI types
		return CANT_SHOOT;
	}
	
	public interface ShootFunction {
		/**
		 * called every tick if the entity has a target.
		 * feed in the previous target position and output the new target position.
		 * one can return the same prevTargetPos if they wish to only update it sometimes.
		 * @return the updated target position
		 */
		@Nonnull Vec3 shoot(Mob mob, EntityTurret turret, @Nonnull LivingEntity target, @Nullable Vec3 prevTargetPos);
	}
	
	private final EntityTurret turret;
	private final Mob mob;
	private final ShootFunction shootFunction;
	private Vec3 prevTargetPos;
	
	public TurretShootGoal(Mob mob, EntityTurret turret) {
		this(mob, turret, getShootFunctionByMob(mob));
	}
	
	public TurretShootGoal(Mob mob, EntityTurret turret, ShootFunction shootFunction) {
		this.mob = mob;
		this.turret = turret;
		this.shootFunction = shootFunction;
	}
	
	@Override
	public void tick() {
		super.tick();
		LivingEntity target = mob.getTarget();
		if (target == null) {
			prevTargetPos = null;
			return;
		}
		prevTargetPos = shootFunction.shoot(mob, turret, target, prevTargetPos);
	}
	
	@Override
	public boolean canUse() {
		if (!mob.level.getGameRules().getBoolean(DSCGameRules.MOBS_USE_TURRETS)) return false;
		if (mob.getVehicle() == null || !mob.getVehicle().equals(turret)) return false;
		Entity target_entity = mob.getTarget();
		return target_entity != null && target_entity.isAlive();
	}
	
	@Override
	public boolean canContinueToUse() {
		return canUse();
	}
	
	@Override
	public void stop() {
		super.stop();
		prevTargetPos = null;
	}
	
	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

}
