package com.onewhohears.dscombat.data.weapon.stats;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.weapon.AbstractWeaponBuilders;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.BulletInstance;
import com.onewhohears.onewholibs.util.UtilMCText;

import com.onewhohears.onewholibs.util.UtilParse;
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
	private final float inaccuracy;
	private final int explodeNum;
    private final boolean useSpeedScale;
	
	public BulletStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		this.damage = UtilParse.getFloatSafe(json, "damage", 0);
		this.speed = UtilParse.getFloatSafe(json, "speed", 0);
		this.explosive = UtilParse.getBooleanSafe(json, "explosive", false);
		this.destroyTerrain = UtilParse.getBooleanSafe(json, "destroyTerrain", false);
		this.causesFire = UtilParse.getBooleanSafe(json, "causesFire", false);
		this.explosionRadius = UtilParse.getFloatSafe(json, "explosionRadius", 0);
		this.inaccuracy = UtilParse.getFloatSafe(json, "inaccuracy", 0);
		this.explodeNum = UtilParse.getIntSafe(json, "explodeNum", 1);
        this.useSpeedScale = UtilParse.getBooleanSafe(json, "useSpeedScale", false);
	}
	
	public float getDamage() {
		return damage;
	}
	
	public double getUnscaledSpeed() {
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

	public float getInaccuracy() {
		return inaccuracy;
	}
	
	public int getExplodeNum() {
		return explodeNum;
	}

    public boolean isUseSpeedScale() {
        return useSpeedScale;
    }

    public double getSpeed() {
        if (isUseSpeedScale()) return DSCPhyCons.getIRLScale() * getUnscaledSpeed();
        return getUnscaledSpeed();
    }
	
	@Override
	public double getMobTurretRange() {
		return Math.min(300, getSpeed() * getMaxAge());
	}
	
	@Override
	public void addToolTips(List<Component> tips, boolean advanced) {
		super.addToolTips(tips, advanced);
		tips.add(UtilMCText.translatable("info.dscombat.damage").append(": "+getDamage())
				.setStyle(Style.EMPTY.withColor(INFO_COLOR)));
		if (advanced) tips.add(UtilMCText.translatable("info.dscombat.max_speed").append(": "+getSpeed())
				.setStyle(Style.EMPTY.withColor(INFO_COLOR)));
		if (isExplosive()) {
			tips.add(UtilMCText.translatable("info.dscombat.explosion_radius")
				.append(": "+getExplosionRadius()).setStyle(Style.EMPTY.withColor(0xAA0000)));
			if (advanced) tips.add(UtilMCText.translatable("info.dscombat.number_of_explosions")
				.append(": "+getExplodeNum()).setStyle(Style.EMPTY.withColor(0xAA0000)));
		}
		if (advanced) tips.add(UtilMCText.translatable("info.dscombat.inaccuracy").append(": "+ getInaccuracy())
				.setStyle(Style.EMPTY.withColor(INFO_COLOR)));
		if (isCausesFire()) tips.add(UtilMCText.translatable("info.dscombat.incendiary")
				.setStyle(Style.EMPTY.withColor(0xAA0000)));
	}

	@Override
	public String getWeaponTypeCode() {
		return UtilMCText.transString("weapon_code.dscombat.gun");
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
	
	@Override
	public boolean isBullet() {
		return true;
	}
	
	@Override
	public boolean isAimAssist() {
		return true;
	}

	@Override
	public JsonPresetType getType() {
		return WeaponType.BULLET;
	}

}
