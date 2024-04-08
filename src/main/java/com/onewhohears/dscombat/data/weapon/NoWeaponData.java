package com.onewhohears.dscombat.data.weapon;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPreset;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class NoWeaponData extends WeaponData {
	
	public static NoWeaponData get() {
		NoWeaponData wd = new NoWeaponData();
		wd.setInternal();
		return wd;
	}
	
	private static JsonObject getNewJson() {
		JsonObject json = new JsonObject();
		json.addProperty("presetId", "no_weapon");
		return json;
	}
	
	private NoWeaponData() {
		super(new ResourceLocation("dscombat:no_weapon"), getNewJson());
	}

	@Override
	public WeaponType getType() {
		return WeaponType.NONE;
	}

	@Override
	public EntityWeapon getEntity(Level level) {
		return null;
	}

	@Override
	public String getWeaponTypeCode() {
		return "";
	}
	
	@Override
	public int getCurrentAmmo() {
		return 0;
	}

	@Override
	public <T extends JsonPreset> T copy() {
		return (T) new NoWeaponData();
	}

	@Override
	public double getMobTurretRange() {
		return 0;
	}

}
