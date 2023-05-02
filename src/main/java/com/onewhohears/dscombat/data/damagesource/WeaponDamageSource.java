package com.onewhohears.dscombat.data.damagesource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.network.chat.Component;
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
	}
	
	public static WeaponDamageSource bullet(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource(getBulletDeath(), shooter, weapon, false);
	}
	
	public static WeaponDamageSource bullet_explode(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource(getBulletExplodeDeath(), shooter, weapon, true);
	}
	
	public static WeaponDamageSource bomb(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource(getBombDeath(), shooter, weapon, true);
	}
	
	public static WeaponDamageSource missile_contact(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource(getMissileContactDeath(), shooter, weapon, false);
	}
	
	public static WeaponDamageSource missile(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource(getMissileDeath(), shooter, weapon, true);
	}
	
	public static WeaponDamageSource torpedo(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource(getTorpedoDeath(), shooter, weapon, true);
	}
	
	public static WeaponDamageSource ir_missile(Entity shooter, EntityWeapon weapon) {
		return new WeaponDamageSource(getIRMissileDeath(), shooter, weapon, true);
	}
	
	public static final String[] saltyDeaths = {"salty1"};
	public static final String[] explodeDeaths = {"explode1","explode2"};
	public static final String[] bulletDeaths = {"bullet1","bullet2"};
	public static final String[] bombDeaths = {"bomb1","bomb2"};
	public static final String[] missileContactDeaths = {"missile_contact1","missile_contact2"};
	public static final String[] missileDeaths = {"missile1","missile2","missile3","missile4"};
	public static final String[] torpedoDeaths = {"torpedo1","torpedo2"};
	public static final String[] irMissileDeaths = {"ir_missile1","ir_missile2"};
	
	public static String getBulletDeath() {
		return UtilParse.getRandomString(new int[]{1,2}, saltyDeaths, bulletDeaths);
	}
	
	public static String getBulletExplodeDeath() {
		return UtilParse.getRandomString(new int[]{1,2,4}, saltyDeaths, bulletDeaths, explodeDeaths);
	}
	
	public static String getBombDeath() {
		return UtilParse.getRandomString(new int[]{1,4,2}, saltyDeaths, bombDeaths, explodeDeaths);
	}
	
	public static String getMissileContactDeath() {
		return UtilParse.getRandomString(new int[]{1,2,4}, saltyDeaths, missileDeaths, missileContactDeaths);
	}
	
	public static String getMissileDeath() {
		return UtilParse.getRandomString(new int[]{1,4,2}, saltyDeaths, missileDeaths, explodeDeaths);
	}
	
	public static String getTorpedoDeath() {
		return UtilParse.getRandomString(new int[]{1,2,1,4}, saltyDeaths, missileDeaths, explodeDeaths, torpedoDeaths);
	}
	
	public static String getIRMissileDeath() {
		return UtilParse.getRandomString(new int[]{1,2,1,4}, saltyDeaths, missileDeaths, explodeDeaths, irMissileDeaths);
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
		String s = "death.attack."+DSCombatMod.MODID+"."+msgId;
		if (killer == null) return Component.translatable(s, livingEntity.getDisplayName());
		int dist = (int)livingEntity.position().distanceTo(weapon.getShootPos());
		s += ".player";
		return Component.translatable(s, livingEntity.getDisplayName(), killer.getDisplayName(), dist+"");
	}

}
