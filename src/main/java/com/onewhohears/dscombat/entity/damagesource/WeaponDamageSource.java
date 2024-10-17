package com.onewhohears.dscombat.entity.damagesource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.onewholibs.util.UtilMCText;

import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.damagesource.DamageSource;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageTypes;

public class WeaponDamageSource extends DamageSource {

	protected final WeaponDamageType type;
	protected final EntityWeapon<?> weapon;
	protected final String deathMsgId;
	@Nullable
	protected final Entity shooter;

	// Updated constructor to use Holder<DamageType>
	public WeaponDamageSource(Holder<DamageType> damageType, @Nullable Entity shooter, @Nonnull EntityWeapon<?> weapon) {
		super(damageType);
		this.type = WeaponDamageType.byDamageTypeHolder(damageType);
		this.weapon = weapon;
		this.shooter = shooter;
		this.deathMsgId = this.type.deathMessages.get();
	}

	// Enum for weapon damage types
	public enum WeaponDamageType {
		BULLET((Holder<DamageType>) DamageTypes.MOB_PROJECTILE, () -> getBulletDeath(), false, false),
		BULLET_EXPLODE((Holder<DamageType>) DamageTypes.MOB_PROJECTILE, () -> getBulletExplodeDeath(), true, false),
		BOMB((Holder<DamageType>) DamageTypes.EXPLOSION, () -> getBombDeath(), true, false),
		MISSILE_CONTACT((Holder<DamageType>) DamageTypes.MOB_PROJECTILE, () -> getMissileContactDeath(), false, true),
		MISSILE((Holder<DamageType>) DamageTypes.MOB_PROJECTILE, () -> getMissileDeath(), true, false),
		TORPEDO((Holder<DamageType>) DamageTypes.MOB_PROJECTILE, () -> getTorpedoDeath(), true, false),
		IR_MISSILE((Holder<DamageType>) DamageTypes.EXPLOSION, () -> getIRMissileDeath(), true, false);

		private final Holder<DamageType> damageTypeHolder;
		public final RandomDeathMessageFactory deathMessages;
		public final boolean explosion, bypassArmor;

		WeaponDamageType(Holder<DamageType> damageTypeHolder, RandomDeathMessageFactory deathMessages, boolean explosion, boolean bypassArmor) {
			this.damageTypeHolder = damageTypeHolder;
			this.deathMessages = deathMessages;
			this.explosion = explosion;
			this.bypassArmor = bypassArmor;
		}

		// Method to get a WeaponDamageType by its DamageType holder
		public static WeaponDamageType byDamageTypeHolder(Holder<DamageType> holder) {
			for (WeaponDamageType type : values()) {
				if (type.damageTypeHolder.equals(holder)) {
					return type;
				}
			}
			return null;
		}

		public WeaponDamageSource getSource(@Nullable Entity shooter, @Nonnull EntityWeapon<?> weapon) {
			return new WeaponDamageSource(this.damageTypeHolder, shooter, weapon);
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

	// Death messages arrays (same as your original ones)
	public static final String[] saltyDeaths = {"salty1"};
	public static final String[] explodeDeaths = {"explode1", "explode2"};
	public static final String[] bulletDeaths = {"bullet1", "bullet2"};
	public static final String[] bombDeaths = {"bomb1", "bomb2"};
	public static final String[] missileContactDeaths = {"missile_contact1", "missile_contact2"};
	public static final String[] missileDeaths = {"missile1", "missile2", "missile3", "missile4"};
	public static final String[] torpedoDeaths = {"torpedo1", "torpedo2"};
	public static final String[] irMissileDeaths = {"ir_missile1", "ir_missile2"};

	// Utility methods for selecting random death messages
	public static String getBulletDeath() {
		return UtilParse.getRandomString(new int[]{1, 2}, saltyDeaths, bulletDeaths);
	}

	public static String getBulletExplodeDeath() {
		return UtilParse.getRandomString(new int[]{1, 2, 4}, saltyDeaths, bulletDeaths, explodeDeaths);
	}

	public static String getBombDeath() {
		return UtilParse.getRandomString(new int[]{1, 4, 2}, saltyDeaths, bombDeaths, explodeDeaths);
	}

	public static String getMissileContactDeath() {
		return UtilParse.getRandomString(new int[]{1, 2, 4}, saltyDeaths, missileDeaths, missileContactDeaths);
	}

	public static String getMissileDeath() {
		return UtilParse.getRandomString(new int[]{1, 4, 2}, saltyDeaths, missileDeaths, explodeDeaths);
	}

	public static String getTorpedoDeath() {
		return UtilParse.getRandomString(new int[]{1, 2, 1, 4}, saltyDeaths, missileDeaths, explodeDeaths, torpedoDeaths);
	}

	public static String getIRMissileDeath() {
		return UtilParse.getRandomString(new int[]{1, 2, 1, 4}, saltyDeaths, missileDeaths, explodeDeaths, irMissileDeaths);
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
		return "WDS: " + getMsgId() + " / " + weapon + " / " + getEntity();
	}

	@Override
	public Component getLocalizedDeathMessage(LivingEntity livingEntity) {
		LivingEntity killer = livingEntity.getKillCredit();
		String s = "death.attack." + DSCombatMod.MODID + "." + deathMsgId;
		if (killer == null) return UtilMCText.translatable(s, livingEntity.getDisplayName());
		int dist = (int) livingEntity.position().distanceTo(weapon.getShootPos());
		s += ".player";
		return UtilMCText.translatable(s, livingEntity.getDisplayName(), killer.getDisplayName(), dist + "");
	}

	@Nullable
	@Override
	public Entity getDirectEntity() {
		return weapon;
	}

	@Nullable
	@Override
	public Entity getEntity() {
		return shooter;
	}
}

