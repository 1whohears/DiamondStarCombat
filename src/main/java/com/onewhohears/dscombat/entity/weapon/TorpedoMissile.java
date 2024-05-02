package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.aircraft.DSCPhyCons;
import com.onewhohears.dscombat.data.weapon.stats.TorpedoStats;
import com.onewhohears.dscombat.entity.damagesource.WeaponDamageSource;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TorpedoMissile<T extends TorpedoStats> extends TrackEntityMissile<T> {
	
	public TorpedoMissile(EntityType<? extends TorpedoMissile<?>> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
	}
	
	@Override
	protected void tickSetMove() {
		if (isInWater()) super.tickSetMove();
		else {
			Vec3 cm = getDeltaMovement();
			cm = cm.add(0, -DSCPhyCons.GRAVITY, 0);
			setDeltaMovement(cm);
		}
	}
	
	@Override
	public Fluid getFluidClipContext() {
		return ClipContext.Fluid.NONE;
	}
	
	@Override
	protected WeaponDamageSource getImpactDamageSource() {
		return WeaponDamageSource.WeaponDamageType.MISSILE_CONTACT.getSource(getOwner(), this);
	}

	@Override
	protected WeaponDamageSource getExplosionDamageSource() {
		return WeaponDamageSource.WeaponDamageType.TORPEDO.getSource(getOwner(), this);
	}
	
}
