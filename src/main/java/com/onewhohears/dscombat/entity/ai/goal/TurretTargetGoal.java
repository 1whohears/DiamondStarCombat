package com.onewhohears.dscombat.entity.ai.goal;

import java.util.function.Predicate;

import com.onewhohears.dscombat.entity.parts.EntityTurret;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class TurretTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
	
	public static TurretTargetGoal<Player> targetPlayers(Mob mob, EntityTurret turret, double range) {
		return new TurretTargetGoal<>(mob, turret, range, Player.class, (player) -> {
			return mob.isAlliedTo(player);
		});
	}
	
	public static TurretTargetGoal<LivingEntity> targetLiving(Mob mob, EntityTurret turret, double range) {
		return new TurretTargetGoal<>(mob, turret, range, LivingEntity.class, (entity) -> {
			if (!entity.isAttackable()) return false;
			return mob.isAlliedTo(entity);
		});
	}
	
	private final EntityTurret turret;
	private final double range;
	
	@Override
	protected void findTarget() {
		super.findTarget();
	}
	
	private TurretTargetGoal(Mob mob, EntityTurret turret, double range, 
			Class<T> type, Predicate<LivingEntity> check) {
		super(mob, type, 0, 
				true, false, check);
		this.turret = turret;
		this.range = range;
	}
	
	@Override
	protected AABB getTargetSearchArea(double targetDistance) {
		return mob.getBoundingBox().inflate(range, 200, range);
	}
	
	@Override
	protected double getFollowDistance() {
		return range;
	}

}
