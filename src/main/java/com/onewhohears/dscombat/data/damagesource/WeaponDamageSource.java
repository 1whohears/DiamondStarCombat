package com.onewhohears.dscombat.data.damagesource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class WeaponDamageSource extends EntityDamageSource {
	
	protected final EntityWeapon weapon;
	
	public WeaponDamageSource(@Nonnull String damageTypeId, @Nullable Entity shooter, @Nonnull EntityWeapon weapon, boolean explosion) {
		super(damageTypeId, shooter);
		this.weapon = weapon;
		setProjectile();
		if (explosion) setExplosion();
		// IDEA 7 random custom kill messages
	}
	
	public static WeaponDamageSource bullet(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource("dscombat.bullet", shooter, weapon, false);
	}
	
	public static WeaponDamageSource bullet_explode(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource("dscombat.bullet_explode", shooter, weapon, true);
	}
	
	public static WeaponDamageSource bomb(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource("dscombat.bomb", shooter, weapon, true);
	}
	
	public static WeaponDamageSource missile_contact(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource("dscombat.missile_contact", shooter, weapon, false);
	}
	
	public static WeaponDamageSource missile(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource("dscombat.missile", shooter, weapon, true);
	}
	
	public static WeaponDamageSource torpedo(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource("dscombat.torpedo", shooter, weapon, true);
	}
	
	public static WeaponDamageSource ir_missile(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource("dscombat.ir_missile", shooter, weapon, true);
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
		if (killer == null) return new TranslatableComponent(s, livingEntity.getDisplayName());
		int dist = (int)livingEntity.position().distanceTo(weapon.getShootPos());
		s += ".player";
		return new TranslatableComponent(s, livingEntity.getDisplayName(), killer.getDisplayName(), dist+"");
	}

}
