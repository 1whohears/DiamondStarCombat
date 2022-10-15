package com.onewhohears.dscombat.data;

import javax.annotation.Nullable;

import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class WeaponDamageSource extends EntityDamageSource {
	
	protected final Entity exploder;
	
	public WeaponDamageSource(LivingEntity shooter, Entity exploder, boolean explosion) {
		super("explosion.player", shooter);
		if (explosion) setExplosion();
		this.setProjectile();
		this.exploder = exploder;
	}
	
	@Nullable
	@Override
	public Entity getDirectEntity() {
		return exploder;
	}
	
	@Nullable
	@Override
	public Entity getEntity() {
		return entity;
	}

}
