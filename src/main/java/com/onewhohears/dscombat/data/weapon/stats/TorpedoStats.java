package com.onewhohears.dscombat.data.weapon.stats;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.TorpedoInstance;

import net.minecraft.resources.ResourceLocation;

public class TorpedoStats extends TrackMissileStats {
	
	public TorpedoStats(ResourceLocation key, JsonObject json) {
		super(key, json);
	}
	
	@Override
	public JsonPresetType getType() {
		return WeaponType.TORPEDO;
	}
	
	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new TorpedoInstance<>(this);
	}
	
	public TargetType getTargetType() {
		return TargetType.WATER;
	}
	
	@Override
	public String getWeaponTypeCode() {
		return "TR";
	}
	
	@Override
	public String getDefaultIconLocation() {
		return MODID+":textures/ui/weapon_icons/torpedo.png";
	}
	
}
