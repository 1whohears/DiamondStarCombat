package com.onewhohears.dscombat.data.weapon.stats;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.weapon.AbstractWeaponBuilders;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.BombInstance;
import com.onewhohears.dscombat.util.UtilMCText;

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
	
	public BombStats(ResourceLocation key, JsonObject json) {
		super(key, json);
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
		return UtilMCText.transString("weapon_code.dscombat.pickle");
	}
	
	@Override
	public String getDefaultIconLocation() {
		return MODID+":textures/ui/weapon_icons/bomb.png";
	}
	
	@Override
	public boolean isBullet() {
		return false;
	}
	
	@Override
	public boolean isAimAssist() {
		return true;
	}
	
	@Override
	public JsonPresetType getType() {
		return WeaponType.BOMB;
	}
	
}
