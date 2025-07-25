package com.onewhohears.dscombat.entity.ai.goal;

import java.util.function.Predicate;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import com.onewhohears.onewholibs.util.UtilEntity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import static com.onewhohears.dscombat.entity.ai.goal.TurretShootGoal.LOGGER;
import static com.onewhohears.dscombat.entity.ai.goal.TurretShootGoal.debugTurretAI;

public class TurretTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
	
	public static TurretTargetGoal<Player> targetPlayers(Mob mob, EntityTurret turret) {
		double range = turret.getAIHorizontalRange();
		return new TurretTargetGoal<>(mob, turret, Player.class, range, 
				checkCanTarget(mob, turret, false));
	}
	
	public static TurretTargetGoal<LivingEntity> targetEnemy(Mob mob, EntityTurret turret) {
		double range = turret.getAIHorizontalRange();
		return new TurretTargetGoal<>(mob, turret, LivingEntity.class, range, 
				checkCanTarget(mob, turret, true));
	}
	
	public static Predicate<LivingEntity> checkCanTarget(Mob mob, EntityTurret turret, boolean enemyCheck) {
		return entity -> {
			if (debugTurretAI()) LOGGER.info("turret {} checking {}", turret, entity);
			WeaponInstance<?> wd = turret.getWeaponData();
			if (wd == null) {
				if (debugTurretAI()) LOGGER.info("FAIL weapon instance null");
				return false;
			}
			if (entity == null) {
				if (debugTurretAI()) LOGGER.info("FAIL entity null");
				return false;
			}
			if (entity.isRemoved()) {
				if (debugTurretAI()) LOGGER.info("FAIL entity removed");
				return false;
			}
			if (entity.isDeadOrDying()) {
				if (debugTurretAI()) LOGGER.info("FAIL entity dead or dying");
				return false;
			}
			if (entity.isSpectator()) {
				if (debugTurretAI()) LOGGER.info("FAIL entity is spectator");
				return false;
			}
			if (entity.position().subtract(turret.position()).horizontalDistance() > turret.getAIHorizontalRange()) {
				if (debugTurretAI()) LOGGER.info("FAIL entity outside of range {}", turret.getAIHorizontalRange());
				return false;
			}
			if (entity.isAlliedTo(mob)) {
				if (debugTurretAI()) LOGGER.info("FAIL entity is allied");
				return false;
			}
			if (mob.isPassengerOfSameVehicle(entity)) {
				if (debugTurretAI()) LOGGER.info("FAIL entity is passenger of the same vehicle of the mob");
				return false;
			}
			boolean isPlayer = false;
			if (entity instanceof Player player) {
				isPlayer = true;
				if (player.isCreative()) {
					if (debugTurretAI()) LOGGER.info("FAIL player is creative");
					return false;
				}
			}
			if (enemyCheck) {
				if (mob.getTeam() == null && isPlayer) {
					if (debugTurretAI()) LOGGER.info("FAIL team null and is player");
					return false;
				}
				if (!(entity instanceof Enemy)) {
					if (debugTurretAI()) LOGGER.info("FAIL not an enemy");
					return false;
				}
			}
			if (wd.getStats().isIRMissile()) {
				if (UtilVehicleEntity.isOnGroundOrWater(entity)) return false;
			} else if (wd.getStats().requiresRadar()) {
				EntityVehicle vehicle = turret.getParentVehicle();
				if (vehicle == null) {
					if (debugTurretAI()) LOGGER.info("FAIL vehicle null");
					return false;
				}
				if (!vehicle.radarSystem.hasTarget(entity)) {
					if (debugTurretAI()) LOGGER.info("FAIL radar doesn't have target");
					return false;
				}
			}
			if (!UtilEntity.canEntitySeeEntity(mob, entity, Config.COMMON.maxBlockCheckDepth.get())) {
				if (debugTurretAI()) LOGGER.info("FAIL cant see target");
				return false;
			}
			return true;
		};
	}

	private final Predicate<LivingEntity> check;
	private final EntityTurret turret;
	private final double range;
	
	@Override
	protected void findTarget() {
		if (debugTurretAI()) LOGGER.info("find target {}", mob);
		WeaponInstance<?> wd = turret.getWeaponData();
		EntityVehicle vehicle = turret.getParentVehicle();
		if (wd == null || vehicle == null) return;
		if (wd.getStats().requiresRadar()) {
			if (targetType != Player.class && targetType != ServerPlayer.class) 
				target = vehicle.radarSystem.getLivingTargetByWeapon(wd);
			else target = vehicle.radarSystem.getPlayerTargetByWeapon(wd);
			if (debugTurretAI()) LOGGER.info("target = {}", target);
			mob.setTarget(target);
			return;
		}
		if (targetType != Player.class && targetType != ServerPlayer.class) 
			target = mob.level.getNearestEntity(mob.level.getEntitiesOfClass(targetType, 
					getTargetSearchArea(getFollowDistance()), (entity) -> true), targetConditions, 
					mob, mob.getX(), mob.getEyeY(), mob.getZ());
		else target = mob.level.getNearestPlayer(targetConditions, 
					mob, mob.getX(), mob.getEyeY(), mob.getZ());
		mob.setTarget(target);
	}
	
	@Override
	public boolean canUse() {
		if (debugTurretAI()) LOGGER.info("canUse? {}", mob);
		if (!mob.level.getGameRules().getBoolean(DSCGameRules.MOBS_USE_TURRETS)) return false;
		if (mob.getVehicle() == null || !mob.getVehicle().equals(turret)) return false;
		return super.canUse();
	}
	
	@Override
	public boolean canContinueToUse() {
		LivingEntity living = mob.getTarget();
		if (living == null) living = target;
		if (living == null) return false;
		if (mob.isDeadOrDying()) return false;
		if (!check.test(living)) {
			if (debugTurretAI()) LOGGER.info("cant continue to use {} {}", mob, mob.getTarget());
			return false;
		}
		mob.setTarget(living);
		target = living;
		if (debugTurretAI()) LOGGER.info("canContinueToUse {} {}", mob, mob.getTarget());
        return true;
	}
	
	@Override
	public void stop() {
		mob.setTarget(null);
		target = null;
	}
	
	private TurretTargetGoal(Mob mob, EntityTurret turret,  
			Class<T> type, double range, Predicate<LivingEntity> check) {
		super(mob, type, 8, false, false, check);
		this.turret = turret;
		this.range = range;
		this.check = check;
		this.targetConditions.ignoreLineOfSight(); // vanilla line of sight has a limit of 128 blocks
	}
	
	@Override
	protected @NotNull AABB getTargetSearchArea(double targetDistance) {
		return mob.getBoundingBox().inflate(getFollowDistance(), getVerticalRange(), getFollowDistance());
	}
	
	public double getVerticalRange() {
		return turret.getAIVerticalRange();
	}
	
	@Override
	protected double getFollowDistance() {
		return range;
	}

}
