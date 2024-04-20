package com.onewhohears.dscombat.data.weapon;

import com.onewhohears.dscombat.data.jsonpreset.JsonPreset;
import com.onewhohears.dscombat.data.jsonpreset.JsonPreset.JsonPresetFactory;
import com.onewhohears.dscombat.data.recipe.DSCIngredientBuilder;
import com.onewhohears.dscombat.data.weapon.TrackMissileData.TargetType;
import com.onewhohears.dscombat.data.weapon.WeaponData.WeaponType;

import net.minecraft.resources.ResourceLocation;

public class AbstractWeaponBuilders {
	
	public static abstract class WeaponBuilder<C extends WeaponBuilder<C>> extends DSCIngredientBuilder<C> {
		
		protected final WeaponType type;
		
		protected WeaponBuilder(String namespace, String name, JsonPresetFactory<? extends WeaponData> sup, WeaponType type) {
			super(namespace, name, sup);
			this.type = type;
		}
		
		@Override
		public <T extends JsonPreset> T build() {
			getData().addProperty("type", type.getId());
			return super.build();
		}

		public C setCraftNum(int craftNum) {
			return setInt("craftNum", craftNum);
		}
		
		public C setMaxAge(int maxAge) {
			return setInt("maxAge", maxAge);
		}
		
		public C setMaxAmmo(int maxAmmo) {
			return setInt("maxAmmo", maxAmmo);
		}
		
		public C setFireRate(int fireRate) {
			return setInt("fireRate", fireRate);
		}
		
		public C setCanShootOnGround(boolean canShootOnGround) {
			return setBoolean("canShootOnGround", canShootOnGround);
		}
		
		public C setEntityType(ResourceLocation entityTypeKey) {
			return setString("entityTypeKey", entityTypeKey.toString());
		}
		
		public C setShootSound(ResourceLocation shootSoundKey) {
			return setString("shootSoundKey", shootSoundKey.toString());
		}
		
		public C setRackEntityType(ResourceLocation rackTypeKey) {
			return setString("rackTypeKey", rackTypeKey.toString());
		}
		
		public C setNoRack() {
			return setString("rackTypeKey", "");
		}
		
		public C setCompatibleWeaponPart(ResourceLocation compatibleWeaponPart) {
			return setString("compatibleWeaponPart", compatibleWeaponPart.toString());
		}
		
		public C setCompatibleTurret(ResourceLocation compatibleTurret) {
			return setString("compatibleTurret", compatibleTurret.toString());
		}
		
		public C setNoCompatible() {
			return setString("compatibleWeaponPart", "");
		}
		
		public C setItem(ResourceLocation itemKey) {
			return setString("itemKey", itemKey.toString());
		}
		
		public C setIcon(ResourceLocation weaponIcon) {
			return setString("icon", weaponIcon.toString());
		}
		
	}
	
	public abstract static class BulletBuilder<C extends BulletBuilder<C>> extends WeaponBuilder<C> {
		
		protected BulletBuilder(String namespace, String name, JsonPresetFactory<? extends BulletData> sup, WeaponType type) {
			super(namespace, name, sup, type);
		}
		
		public C setDamage(float damage) {
			return setFloat("damage", damage);
		}
		
		public C setSpeed(float speed) {
			return setFloat("speed", speed);
		}
		
		public C setExplosive(boolean explosive) {
			return setBoolean("explosive", explosive);
		}
		
		public C setDestoryTerrain(boolean destroyTerrain) {
			return setBoolean("destroyTerrain", destroyTerrain);
		}
		
		public C setCausesFire(boolean causesFire) {
			return setBoolean("causesFire", causesFire);
		}
		
		public C setExplosionRadius(float explosionRadius) {
			return setFloat("explosionRadius", explosionRadius);
		}
		
		public C setInnacuracy(float innacuracy) {
			return setFloat("innacuracy", innacuracy);
		}
		
		public C setExplodeNum(int explodeNum) {
			return setInt("explodeNum", explodeNum);
		}
		
	}
	
	public abstract static class BombBuilder<C extends BombBuilder<C>> extends BulletBuilder<C> {
		
		protected BombBuilder(String namespace, String name, JsonPresetFactory<? extends BombData> sup, WeaponType type) {
			super(namespace, name, sup, type);
		}
		
	}
	
	public abstract static class BunkerBusterBuilder<C extends BunkerBusterBuilder<C>> extends BombBuilder<C> {
		
		protected BunkerBusterBuilder(String namespace, String name, JsonPresetFactory<? extends BunkerBusterData> sup, WeaponType type) {
			super(namespace, name, sup, type);
		}
		
		public C setBlockStrength(float blockStrength) {
			return setFloat("blockStrength", blockStrength);
		}
		
	}
	
	public abstract static class MissileBuilder<C extends MissileBuilder<C>> extends BulletBuilder<C> {
		
		protected MissileBuilder(String namespace, String name, JsonPresetFactory<? extends MissileData> sup, WeaponType type) {
			super(namespace, name, sup, type);
		}
		
		public C setTurnRadius(float turnRadius) {
			return setFloat("turnRadius", turnRadius);
		}
		
		public C setAcceleration(float acceleration) {
			return setFloat("acceleration", acceleration);
		}
		
		public C setFuseDistance(float fuseDist) {
			return setFloat("fuseDist", fuseDist);
		}
		
		public C setFieldOfView(float fov) {
			return setFloat("fov", fov);
		}
		
		public C setBleed(float bleed) {
			return setFloat("bleed", bleed);
		}
		
		public C setFuelTicks(int fuelTicks) {
			return setInt("fuelTicks", fuelTicks);
		}
		
		public C setSeeThroWaterNum(int water) {
			return setInt("seeThroWater", water);
		}
		
		public C setSeeThroBlockNum(int block) {
			return setInt("seeThroBlock", block);
		}
		
		/**
		 * IR Missiles only
		 */
		public C setFlareResistance(float flareResistance) {
			return setFloat("flareResistance", flareResistance);
		}
		
		/**
		 * Track Missile only
		 */
		public C setTargetType(TargetType targetType) {
			return setString("targetType", targetType.name());
		}
		
		/**
		 * Track Missile only
		 */
		public C setNotActiveTrack() {
			return setBoolean("activeTrack", false);
		}
		
		/**
		 * Track Missile only
		 */
		public C setActiveTrack() {
			return setBoolean("activeTrack", true);
		}
		
		/**
		 * Anti Radar Missile only
		 */
		public C setScanRange(float scan_range) {
			return setFloat("scan_range", scan_range);
		}
		
	}
	
}
