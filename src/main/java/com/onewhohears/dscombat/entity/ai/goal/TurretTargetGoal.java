package com.onewhohears.dscombat.entity.ai.goal;

import java.util.function.Predicate;

import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.util.UtilEntity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class TurretTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
	
	public static TurretTargetGoal<Player> targetPlayers(Mob mob, EntityTurret turret) {
		return new TurretTargetGoal<>(mob, turret, Player.class, turret.getHorizontalRange(), 
				checkCanTarget(mob, turret, false));
	}
	
	public static TurretTargetGoal<LivingEntity> targetEnemy(Mob mob, EntityTurret turret) {
		return new TurretTargetGoal<>(mob, turret, LivingEntity.class, turret.getHorizontalRange(), 
				checkCanTarget(mob, turret, true));
	}
	
	public static Predicate<LivingEntity> checkCanTarget(Mob mob, EntityTurret turret, boolean enemyCheck) {
		return (entity) -> {
			WeaponData wd = turret.getWeaponData();
			if (wd == null) return false;
			if (entity == null) return false;
			if (entity.isRemoved()) return false;
			if (entity.isDeadOrDying()) return false;
			if (!entity.isAttackable()) return false;
			if (entity.isSpectator()) return false;
			if (mob.isAlliedTo(entity)) return false;
			if (enemyCheck) {
				if (mob.getTeam() == null && entity instanceof Player) return false;
				if (!(entity instanceof Enemy)) return false;
			}
			if (wd.getType().isIRMissile()) {
				if (UtilEntity.isOnGroundOrWater(entity)) return false;
			} else if (wd.getType().isTrackMissile()) {
				EntityVehicle vehicle = turret.getParentVehicle();
				if (vehicle == null) return false;
				if (!vehicle.radarSystem.hasTarget(entity)) return false;
			}
			return true;
		};
	}
	
	private final EntityTurret turret;
	private final double range;
	
	@Override
	protected void findTarget() {
		WeaponData wd = turret.getWeaponData();
		EntityVehicle vehicle = turret.getParentVehicle();
		if (wd == null || vehicle == null) return;
		if (wd.requiresRadar()) {
			if (targetType != Player.class && targetType != ServerPlayer.class) 
				target = vehicle.radarSystem.getLivingTargetByWeapon(wd);
			else target = vehicle.radarSystem.getPlayerTargetByWeapon(wd);
			return;
		}
		if (targetType != Player.class && targetType != ServerPlayer.class) 
			target = mob.level.getNearestEntity(mob.level.getEntitiesOfClass(targetType, 
					getTargetSearchArea(getFollowDistance()), (entity) -> true), targetConditions, 
					mob, mob.getX(), mob.getEyeY(), mob.getZ());
		else target = mob.level.getNearestPlayer(targetConditions, 
					mob, mob.getX(), mob.getEyeY(), mob.getZ());
	}
	
	private TurretTargetGoal(Mob mob, EntityTurret turret,  
			Class<T> type, double range, Predicate<LivingEntity> check) {
		super(mob, type, 0, 
				true, false, check);
		this.turret = turret;
		this.range = range;
		this.setUnseenMemoryTicks(20);
	}
	
	@Override
	protected AABB getTargetSearchArea(double targetDistance) {
		return mob.getBoundingBox().inflate(getHorizontalRange(), getVerticalRange(), getHorizontalRange());
	}
	
	public double getHorizontalRange() {
		return turret.getHorizontalRange();
	}
	
	public double getVerticalRange() {
		return turret.getVerticalRange();
	}
	
	@Override
	protected double getFollowDistance() {
		return range;
	}

}
