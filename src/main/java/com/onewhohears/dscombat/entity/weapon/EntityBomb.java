package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.aircraft.DSCPhyCons;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.stats.BombStats;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.damagesource.WeaponDamageSource;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityBomb<T extends BombStats> extends EntityBullet<T> {
	
	public EntityBomb(EntityType<? extends EntityBomb<?>> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
	}
	
	@Override
	public WeaponType getWeaponType() {
		return WeaponType.BOMB;
	}
	
	@Override
	public void init() {	
	}
	
	@Override
	protected void tickSetMove() {
		setDeltaMovement(getDeltaMovement().add(0, -DSCPhyCons.GRAVITY, 0));
	}
	
	@Override
	protected WeaponDamageSource getImpactDamageSource() {
		return WeaponDamageSource.WeaponDamageType.BOMB.getSource(getOwner(), this);
	}
	
	@Override
	protected WeaponDamageSource getExplosionDamageSource() {
		return WeaponDamageSource.WeaponDamageType.BOMB.getSource(getOwner(), this);
	}
	
	@Override
	public boolean isPickable() {
		return true;
	}
	
	@Override
	public WeaponStats.WeaponClientImpactType getClientImpactType() {
		return WeaponStats.WeaponClientImpactType.MED_BOMB_EXPLODE;
	}

}
