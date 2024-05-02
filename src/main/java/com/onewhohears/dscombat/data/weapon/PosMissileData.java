package com.onewhohears.dscombat.data.weapon;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPreset;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.PositionMissile;
import com.onewhohears.dscombat.util.UtilEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class PosMissileData extends MissileData {

	public PosMissileData(ResourceLocation key, JsonObject json) {
		super(key, json);
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.POS_MISSILE;
	}
	
	@Override
	public <T extends JsonPreset> T copy() {
		return (T) new PosMissileData(getKey(), getJsonData());
	}
	
	@Override
	public EntityWeapon getShootEntity(WeaponShootParameters params) {
		PositionMissile missile = (PositionMissile) super.getShootEntity(params);
		if (missile == null) return null;
		Entity looker = params.owner;
		if (params.vehicle != null && params.vehicle.getGimbalForPilotCamera() != null) {
			looker = params.vehicle.getGimbalForPilotCamera();
			looker.setXRot(params.owner.getXRot());
			looker.setYRot(params.owner.getYRot());
		}
		missile.targetPos = UtilEntity.getLookingAtBlockPos(looker, 300);
		return missile;
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
