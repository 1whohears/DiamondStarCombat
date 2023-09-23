package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.damagesource.WeaponDamageSource;
import com.onewhohears.dscombat.data.weapon.TorpedoData;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TorpedoMissile extends TrackEntityMissile {
	
	public final double ACC_GRAVITY = Config.SERVER.accGravity.get();
	
	public TorpedoMissile(EntityType<? extends TorpedoMissile> type, Level level) {
		super(type, level);
	}
	
	public TorpedoMissile(Level level, Entity owner, TorpedoData data) {
		super(level, owner, data);
	}
	
	@Override
	protected void tickSetMove() {
		if (isInWater()) super.tickSetMove();
		else {
			Vec3 cm = getDeltaMovement();
			cm = cm.add(0, -ACC_GRAVITY, 0);
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
