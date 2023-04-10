package com.onewhohears.dscombat.data.weapon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class WeaponDamageSource extends EntityDamageSource {
	
	protected final EntityWeapon weapon;
	protected final float torqueK;
	
	public WeaponDamageSource(@Nonnull String damageTypeId, @Nullable Entity shooter, @Nonnull EntityWeapon weapon, boolean explosion, float torque) {
		super(damageTypeId, shooter);
		this.weapon = weapon;
		this.torqueK = torque;
		this.setProjectile();
		if (explosion) this.setExplosion();
	}
	
	public static WeaponDamageSource bullet(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource("dscombat.bullet", shooter, weapon, false, 0);
	}
	
	public static WeaponDamageSource bullet_explode(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource("dscombat.bullet_explode", shooter, weapon, true, 0.05f);
	}
	
	public static WeaponDamageSource bomb(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource("dscombat.bomb", shooter, weapon, true, 0.10f);
	}
	
	public static WeaponDamageSource missile(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource("dscombat.missile", shooter, weapon, true, 0.50f);
	}
	
	@Nonnull
	@Override
	public Entity getDirectEntity() {
		return weapon;
	}
	
	@Nullable
	@Override
	public Entity getEntity() {
		return entity;
	}
	
	public float getTorqueK() {
		return torqueK;
	}
	
	@Override
	public boolean scalesWithDifficulty() {
		return false;
	}
	
	@Override
	public Vec3 getSourcePosition() {
		return weapon.position();
	}
	
	@Override
	public String toString() {
		return "WDS: "+msgId+" / "+weapon+" / "+entity;
	}
	
	@Override
	public Component getLocalizedDeathMessage(LivingEntity livingEntity) {
		LivingEntity killer = livingEntity.getKillCredit();
		String s = "death.attack." + msgId;
		if (killer == null) return Component.translatable(s, livingEntity.getDisplayName());
		int dist = (int)livingEntity.position().distanceTo(weapon.getShootPos());
		s += ".player";
		return Component.translatable(s, livingEntity.getDisplayName(), killer.getDisplayName(), dist+"");
	}

}
