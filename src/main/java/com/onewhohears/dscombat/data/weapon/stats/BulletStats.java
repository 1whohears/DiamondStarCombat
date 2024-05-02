package com.onewhohears.dscombat.data.weapon.stats;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.weapon.AbstractWeaponBuilders;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.BulletInstance;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class BulletStats extends WeaponStats {
	
	public static class Builder extends AbstractWeaponBuilders.BulletBuilder<Builder> {
		protected Builder(String namespace, String name) {
			super(namespace, name, WeaponType.BULLET);
		}
		public static Builder bulletBuilder(String namespace, String name) {
			return new Builder(namespace, name);
		}
	}
	
	private final float damage;
	private final double speed;
	private final boolean explosive;
	private final boolean destroyTerrain;
	private final boolean causesFire;
	private final float explosionRadius;
	private final float innacuracy;
	private final int explodeNum;
	
	public BulletStats(ResourceLocation key, JsonObject json, WeaponType type) {
		super(key, json, type);
		this.damage = json.get("damage").getAsFloat();
		this.speed = json.get("speed").getAsDouble();
		this.explosive = json.get("explosive").getAsBoolean();
		this.destroyTerrain = json.get("destroyTerrain").getAsBoolean();
		this.causesFire = json.get("causesFire").getAsBoolean();
		this.explosionRadius = json.get("explosionRadius").getAsFloat();
		this.innacuracy = json.get("innacuracy").getAsFloat();
		this.explodeNum = UtilParse.getIntSafe(json, "explodeNum", 1);
	}
	
	public float getDamage() {
		return damage;
	}
	
	public double getSpeed() {
		return speed;
	}

	public boolean isExplosive() {
		return explosive;
	}

	public boolean isDestroyTerrain() {
		return destroyTerrain;
	}

	public float getExplosionRadius() {
		return explosionRadius;
	}

	public boolean isCausesFire() {
		return causesFire;
	}

	public float getInnacuracy() {
		return innacuracy;
	}
	
	public int getExplodeNum() {
		return explodeNum;
	}
	
	@Override
	public double getMobTurretRange() {
		return Math.min(300, getSpeed() * getMaxAge());
	}
	
	@Override
	public void addToolTips(List<Component> tips, boolean advanced) {
		super.addToolTips(tips, advanced);
		tips.add(UtilMCText.literal("Damage: ").append(getDamage()+"").setStyle(Style.EMPTY.withColor(INFO_COLOR)));
		if (advanced) tips.add(UtilMCText.literal("Max Speed: ").append(getSpeed()+"").setStyle(Style.EMPTY.withColor(INFO_COLOR)));
		if (isExplosive()) {
			tips.add(UtilMCText.literal("Explosion Radius: ")
				.append(getExplosionRadius()+"").setStyle(Style.EMPTY.withColor(0xAA0000)));
			if (advanced) tips.add(UtilMCText.literal("Explosions: ")
				.append(getExplodeNum()+"").setStyle(Style.EMPTY.withColor(0xAA0000)));
		}
		if (advanced) tips.add(UtilMCText.literal("Innacuracy: ").append(getInnacuracy()+"").setStyle(Style.EMPTY.withColor(INFO_COLOR)));
		if (isCausesFire()) tips.add(UtilMCText.literal("INCENDIARY").setStyle(Style.EMPTY.withColor(0xAA0000)));
	}

	@Override
	public String getWeaponTypeCode() {
		String code = "S";
		if (isExplosive()) code += "E";
		if (isCausesFire()) code += "I";
		return code;
	}
	
	@Override
	public String getDefaultIconLocation() {
		if (isExplosive()) return MODID+":textures/ui/weapon_icons/he_bullet.png";
		return MODID+":textures/ui/weapon_icons/bullet.png";
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new BulletInstance<>(this);
	}
	
	public boolean isBullet() {
		return true;
	}
	
	public boolean isAimAssist() {
		return true;
	}

}
