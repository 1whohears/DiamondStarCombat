package com.onewhohears.dscombat.data.weapon.stats;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.weapon.AbstractWeaponBuilders;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.GrenadeInstance;
import com.onewhohears.onewholibs.util.UtilMCText;

import net.minecraft.resources.ResourceLocation;

public class GrenadeStats extends BombStats {
	
	public static class Builder extends AbstractWeaponBuilders.BombBuilder<Builder> {
		protected Builder(String namespace, String name) {
			super(namespace, name, WeaponType.GRENADE);
		}
		public static Builder grenadeBuilder(String namespace, String name) {
			return new Builder(namespace, name);
		}
	}
	
	public GrenadeStats(ResourceLocation key, JsonObject json) {
		super(key, json);
	}
	
	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new GrenadeInstance(this);
	}
	
	@Override
	public double getMobTurretRange() {
		return 15;
	}
	
	@Override
	public String getWeaponTypeCode() {
		return UtilMCText.transString("weapon_code.dscombat.grenade");
	}
	
	@Override
	public String getDefaultIconLocation() {
		return MODID+":textures/ui/weapon_icons/grenade.png";
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
		return WeaponType.GRENADE;
	}
	
}
