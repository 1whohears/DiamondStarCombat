package com.onewhohears.dscombat.data.weapon.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.NoWeaponInstance;

import net.minecraft.resources.ResourceLocation;

public class NoWeaponStats extends WeaponStats {
	
	private static final NoWeaponStats defaultStats = new NoWeaponStats();
	
	public static NoWeaponStats get() {
		return defaultStats;
	}
	
	private static JsonObject getNewJson() {
		JsonObject json = new JsonObject();
		json.addProperty("presetId", "no_weapon");
		return json;
	}
	
	private NoWeaponStats() {
		super(new ResourceLocation("dscombat:no_weapon"), getNewJson());
	}

	@Override
	public double getMobTurretRange() {
		return 0;
	}
	
	@Override
	public String getWeaponTypeCode() {
		return "";
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new NoWeaponInstance(this);
	}
	
	public boolean isNoWeapon() {
		return true;
	}
	
	@Override
	public JsonPresetType getType() {
		return WeaponType.NONE;
	}

}
