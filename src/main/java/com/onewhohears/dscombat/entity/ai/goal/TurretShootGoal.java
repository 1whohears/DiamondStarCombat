package com.onewhohears.dscombat.entity.ai.goal;

import com.onewhohears.dscombat.entity.parts.EntityTurret;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class TurretShootGoal extends Goal {
	
	private final EntityTurret turret;
	private final Mob mob;
	private Entity target;
	
	public TurretShootGoal(Mob mob, EntityTurret turret) {
		this.mob = mob;
		this.turret = turret;
	}
	
	@Override
	public void tick() {
		
	}
	
	@Override
	public boolean canUse() {
		if (!turret.getParent().getPassengerSeat(mob).equals(turret)) return false;
		Entity target_entity = mob.getTarget();
		if (target_entity != null && target_entity.isAlive()) {
			target = target_entity;
			return true;
		} else return false;
	}
	
	public boolean canContinueToUse() {
		return canUse() || target.isAlive() && !mob.getNavigation().isDone();
	}
	
	public void stop() {
		target = null;
	}
	
	public boolean requiresUpdateEveryTick() {
		return true;
	}

}
