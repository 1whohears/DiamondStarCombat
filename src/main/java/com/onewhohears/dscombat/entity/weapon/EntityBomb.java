package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.weapon.BombData;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.damagesource.WeaponDamageSource;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityBomb extends EntityBullet {

	public EntityBomb(EntityType<? extends EntityBomb> type, Level level) {
		super(type, level);
	}
	
	public EntityBomb(Level level, Entity owner, BombData data) {
		super(level, owner, data);
	}
	
	@Override
	public void init() {	
	}
	
	@Override
	protected void tickSetMove() {
		setDeltaMovement(getDeltaMovement().add(0, -Config.SERVER.accGravity.get()*BULLET_GRAVITY_SCALE, 0));
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
	public WeaponData.WeaponType getWeaponType() {
		return WeaponData.WeaponType.BOMB;
	}
	
	@Override
	public WeaponData.WeaponClientImpactType getClientImpactType() {
		return WeaponData.WeaponClientImpactType.MED_BOMB_EXPLODE;
	}

}
