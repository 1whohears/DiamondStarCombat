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
	
	protected final WeaponDamageType type;
	protected final EntityWeapon weapon;
	protected final String deathMsgId;
	
	public WeaponDamageSource(WeaponDamageType type, @Nullable Entity shooter, @Nonnull EntityWeapon weapon) {
		super(type.damageTypeId, shooter);
		this.type = type;
		this.weapon = weapon;
		setProjectile();
		if (type.explosion) setExplosion();
		if (type.bypassArmor) bypassArmor();
		this.deathMsgId = type.deathMessages.get();
	}
	
	public enum WeaponDamageType {
		BULLET("bullet", () -> getBulletDeath(), false, false),
		BULLET_EXPLODE("bullet_explode", () -> getBulletExplodeDeath(), true, false),
		BOMB("bomb", () -> getBombDeath(), true, false),
		MISSILE_CONTACT("missile_contact", () -> getMissileContactDeath(), false, true),
		MISSILE("missile", () -> getMissileDeath(), true, false),
		TORPEDO("tordepo", () -> getTorpedoDeath(), true, false),
		IR_MISSILE("ir_missile", () -> getIRMissileDeath(), true, false);
		@Nullable
		public static WeaponDamageType byId(String id) {
			for (WeaponDamageType wdt : values()) if (wdt.damageTypeId.equals(id)) return wdt;
			return null;
		}
		public final String damageTypeId;
		public final RandomDeathMessageFactory deathMessages;
		public final boolean explosion, bypassArmor;
		private WeaponDamageType(String damageTypeId, RandomDeathMessageFactory deathMessages, boolean explosion, boolean bypassArmor) {
			this.damageTypeId = damageTypeId;
			this.deathMessages = deathMessages;
			this.explosion = explosion;
			this.bypassArmor = bypassArmor;
		}
		public WeaponDamageSource getSource(@Nullable Entity shooter, @Nonnull EntityWeapon weapon) {
			return new WeaponDamageSource(this, shooter, weapon);
		}
		public boolean isContact() {
			return this == BULLET || this == MISSILE_CONTACT;
		}
		public boolean isMissileExplode() {
			return this == MISSILE || this == TORPEDO || this == IR_MISSILE;
		}
	}
	
	public interface RandomDeathMessageFactory {
		String get();
	}
	
	public WeaponDamageType getWeaponDamageType() {
		return type;
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
		String s = "death.attack."+DSCombatMod.MODID+"."+deathMsgId;
		if (killer == null) return Component.translatable(s, livingEntity.getDisplayName());
		int dist = (int)livingEntity.position().distanceTo(weapon.getShootPos());
		s += ".player";
		return Component.translatable(s, livingEntity.getDisplayName(), killer.getDisplayName(), dist+"");
	}

}
