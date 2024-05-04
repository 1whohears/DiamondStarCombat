package com.onewhohears.dscombat.data.weapon.stats;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.PosMissileInstance;

import net.minecraft.resources.ResourceLocation;

public class PosMissileStats extends MissileStats {

	public PosMissileStats(ResourceLocation key, JsonObject json) {
		super(key, json);
	}
	
	@Override
	public JsonPresetType getType() {
		return WeaponType.POS_MISSILE;
	}
	
	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new PosMissileInstance<>(this);
	}
	
	@Override
	public String getWeaponTypeCode() {
		return "AGP";
	}
	
	@Override
	public String getDefaultIconLocation() {
		return MODID+":textures/ui/weapon_icons/pos_missile.png";
	}

}
