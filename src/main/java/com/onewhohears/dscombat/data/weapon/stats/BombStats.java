package com.onewhohears.dscombat.data.weapon.stats;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.weapon.AbstractWeaponBuilders;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.BombInstance;

import net.minecraft.resources.ResourceLocation;

public class BombStats extends BulletStats {
	
	public static class Builder extends AbstractWeaponBuilders.BombBuilder<Builder> {
		protected Builder(String namespace, String name) {
			super(namespace, name, WeaponType.BOMB);
		}
		public static Builder bombBuilder(String namespace, String name) {
			return new Builder(namespace, name);
		}
		
	}
	
	public BombStats(ResourceLocation key, JsonObject json, WeaponType type) {
		super(key, json, type);
	}
	
	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new BombInstance<>(this);
	}
	
	@Override
	public double getMobTurretRange() {
		return 20;
	}
	
	@Override
	public String getWeaponTypeCode() {
		String code = "B";
		if (isCausesFire()) code += "I";
		return code;
	}
	
	@Override
	public String getDefaultIconLocation() {
		return MODID+":textures/ui/weapon_icons/bomb.png";
	}
	
	public boolean isBullet() {
		return false;
	}
	
	public boolean isAimAssist() {
		return true;
	}
	
}
