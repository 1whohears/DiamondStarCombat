package com.onewhohears.dscombat.entity.ai.goal;

import java.util.function.Predicate;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.util.UtilEntity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class TurretTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
	
	public static TurretTargetGoal<Player> targetPlayers(Mob mob, EntityTurret turret) {
		return new TurretTargetGoal<>(mob, turret, Player.class, checkCanTarget(mob));
	}
	
	public static TurretTargetGoal<LivingEntity> targetLiving(Mob mob, EntityTurret turret) {
		return new TurretTargetGoal<>(mob, turret, LivingEntity.class, checkCanTarget(mob));
	}
	
	public static Predicate<LivingEntity> checkCanTarget(Mob mob) {
		return (entity) -> {
			if (entity == null) return false;
			if (!entity.isAttackable()) return false;
			if (mob.isAlliedTo(entity)) return false;
			if (!UtilEntity.canEntitySeeEntity(mob, entity, Config.COMMON.maxBlockCheckDepth.get())) return false;
			return true;
		};
	}
	
	private final EntityTurret turret;
	
	@Override
	protected void findTarget() {
		super.findTarget();
	}
	
	private TurretTargetGoal(Mob mob, EntityTurret turret,  
			Class<T> type, Predicate<LivingEntity> check) {
		super(mob, type, 0, 
				true, false, check);
		this.turret = turret;
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
		return getHorizontalRange();
	}

}
