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

public class TurretTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
	
	public static TurretTargetGoal<Player> targetPlayers(Mob mob, EntityTurret turret) {
		double range = turret.getAIHorizontalRange();
		return new TurretTargetGoal<>(mob, turret, Player.class, range, 
				checkCanTarget(mob, turret, false, range));
	}
	
	public static TurretTargetGoal<LivingEntity> targetEnemy(Mob mob, EntityTurret turret) {
		double range = turret.getAIHorizontalRange();
		return new TurretTargetGoal<>(mob, turret, LivingEntity.class, range, 
				checkCanTarget(mob, turret, true, range));
	}
	
	public static Predicate<LivingEntity> checkCanTarget(Mob mob, EntityTurret turret, boolean enemyCheck, double range) {
		return (entity) -> {
			WeaponInstance<?> wd = turret.getWeaponData();
			if (wd == null) return false;
			if (entity == null) return false;
			if (entity.isRemoved()) return false;
			if (entity.isDeadOrDying()) return false;
			if (!entity.isAttackable()) return false;
			if (entity.isSpectator()) return false;
			if (entity.position().subtract(turret.position()).horizontalDistance() > range) return false;
			if (entity.isAlliedTo(mob)) return false;
			if (mob.isPassengerOfSameVehicle(entity)) return false;
			boolean isPlayer = false;
			if (entity instanceof Player player) {
				isPlayer = true;
				if (player.isCreative()) return false;
			}
			if (enemyCheck) {
				if (mob.getTeam() == null && isPlayer) return false;
				if (!(entity instanceof Enemy)) return false;
			}
			if (wd.getStats().isIRMissile()) {
				if (UtilVehicleEntity.isOnGroundOrWater(entity)) return false;
			} else if (wd.getStats().requiresRadar()) {
				EntityVehicle vehicle = turret.getParentVehicle();
				if (vehicle == null) return false;
				if (!vehicle.radarSystem.hasTarget(entity)) return false;
			}
			if (!UtilEntity.canEntitySeeEntity(mob, entity, Config.COMMON.maxBlockCheckDepth.get())) return false;
			return true;
		};
	}
	
	private final EntityTurret turret;
	private final double range;
	
	@Override
	protected void findTarget() {
		//System.out.println("find target");
		WeaponInstance<?> wd = turret.getWeaponData();
		EntityVehicle vehicle = turret.getParentVehicle();
		if (wd == null || vehicle == null) return;
		if (wd.getStats().requiresRadar()) {
			if (targetType != Player.class && targetType != ServerPlayer.class) 
				target = vehicle.radarSystem.getLivingTargetByWeapon(wd);
			else target = vehicle.radarSystem.getPlayerTargetByWeapon(wd);
			//System.out.println("target = "+target);
			return;
		}
		if (targetType != Player.class && targetType != ServerPlayer.class) 
			target = mob.level().getNearestEntity(mob.level().getEntitiesOfClass(targetType,
					getTargetSearchArea(getFollowDistance()), (entity) -> true), targetConditions, 
					mob, mob.getX(), mob.getEyeY(), mob.getZ());
		else target = mob.level().getNearestPlayer(targetConditions,
					mob, mob.getX(), mob.getEyeY(), mob.getZ());
	}
	
	@Override
	public boolean canUse() {
		//System.out.println("canUse?");
		if (!mob.level().getGameRules().getBoolean(DSCGameRules.MOBS_USE_TURRETS)) return false;
		if (mob.getVehicle() == null || !mob.getVehicle().equals(turret)) return false;
		return super.canUse();
	}
	
	@Override
	public boolean canContinueToUse() {
		LivingEntity living = mob.getTarget();
		if (living == null) living = target;
		if (living == null) return false;
		if (mob.isDeadOrDying()) return false;
		if (!targetConditions.test(mob, living)) return false;
		mob.setTarget(living);
		//System.out.println("canContinueToUse "+living);
        return true;
	}
	
	@Override
	public void stop() {
		mob.setTarget((LivingEntity)null);
		target = null;
	}
	
	private TurretTargetGoal(Mob mob, EntityTurret turret,  
			Class<T> type, double range, Predicate<LivingEntity> check) {
		super(mob, type, 8, 
				false, false, check);
		this.turret = turret;
		this.range = range;
		this.targetConditions.ignoreLineOfSight(); // vanilla line of sight has a limit of 128 blocks
	}
	
	@Override
	protected AABB getTargetSearchArea(double targetDistance) {
		return mob.getBoundingBox().inflate(getFollowDistance(), getVerticalRange(), getFollowDistance());
	}
	
	public double getVerticalRange() {
		return turret.getAIVerticalRange();
	}
	
	@Override
	protected double getFollowDistance() {
		return range;
	}
	
	@Override
	public void start() {
		super.start();
	}

}
