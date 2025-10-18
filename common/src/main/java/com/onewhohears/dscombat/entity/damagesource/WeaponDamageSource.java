package com.onewhohears.dscombat.entity.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.onewholibs.util.UtilMCText;

import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class WeaponDamageSource extends DamageSource {
	
	protected final WeaponDamageType type;
	protected final EntityWeapon<?> weapon;
	protected final String deathMsgId;
	
	public WeaponDamageSource(WeaponDamageType type, @Nullable Entity shooter, @NotNull EntityWeapon<?> weapon) {
		super(type.type, shooter);
		this.type = type;
		this.weapon = weapon;
		//setProjectile();
		//if (type.explosion) setExplosion();
		//if (type.bypassArmor) bypassArmor();
		this.deathMsgId = type.deathMessages.get();
	}
	
	public enum WeaponDamageType {
		BULLET("bullet", WeaponDamageSource::getBulletDeath, false, false, DamageTypes.ARROW),
		BULLET_EXPLODE("bullet_explode", WeaponDamageSource::getBulletExplodeDeath, true, false, DamageTypes.EXPLOSION),
		BOMB("bomb", WeaponDamageSource::getBombDeath, true, false, DamageTypes.EXPLOSION),
		MISSILE_CONTACT("missile_contact", WeaponDamageSource::getMissileContactDeath, false, true, DamageTypes.ARROW),
		MISSILE("missile", WeaponDamageSource::getMissileDeath, true, false, DamageTypes.EXPLOSION),
		TORPEDO("tordepo", WeaponDamageSource::getTorpedoDeath, true, false, DamageTypes.EXPLOSION),
		IR_MISSILE("ir_missile", WeaponDamageSource::getIRMissileDeath, true, false, DamageTypes.EXPLOSION);
		@Nullable
		public static WeaponDamageType byId(String id) {
			for (WeaponDamageType wdt : values()) if (wdt.damageTypeId.equals(id)) return wdt;
			return null;
		}
		public final String damageTypeId;
		public final RandomDeathMessageFactory deathMessages;
		public final boolean explosion, bypassArmor;
        public final Holder<DamageType> type;
		WeaponDamageType(String damageTypeId, RandomDeathMessageFactory deathMessages,
                         boolean explosion, boolean bypassArmor, ResourceKey<DamageType> type) {
			this.damageTypeId = damageTypeId;
			this.deathMessages = deathMessages;
			this.explosion = explosion;
			this.bypassArmor = bypassArmor;
            this.type = (Holder<DamageType>) type;
		}
		public WeaponDamageSource getSource(@Nullable Entity shooter, @NotNull EntityWeapon<?> weapon) {
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
	
	@NotNull
	@Override
	public Entity getDirectEntity() {
		return weapon;
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
	public @NotNull String toString() {
		return "WDS: "+getMsgId()+" / "+weapon+" / "+getEntity();
	}
	
	@Override
	public @NotNull Component getLocalizedDeathMessage(LivingEntity livingEntity) {
		LivingEntity killer = livingEntity.getKillCredit();
		String s = "death.attack."+DSCombatMod.MODID+"."+deathMsgId;
		if (killer == null) return UtilMCText.translatable(s, livingEntity.getDisplayName());
		int dist = (int)livingEntity.position().distanceTo(weapon.getShootPos());
		s += ".player";
		return UtilMCText.translatable(s, livingEntity.getDisplayName(), killer.getDisplayName(), dist+"");
	}

}
