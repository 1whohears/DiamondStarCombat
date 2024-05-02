package com.onewhohears.dscombat.data.weapon;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPreset;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityBomb;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class BombData extends BulletData {
	
	public static class Builder extends AbstractWeaponBuilders.BombBuilder<Builder> {
		
		protected Builder(String namespace, String name, JsonPresetFactory<? extends BombData> sup) {
			super(namespace, name, sup, WeaponType.BOMB);
		}
		
		public static Builder bombBuilder(String namespace, String name) {
			return new Builder(namespace, name, (key, json) -> new BombData(key, json));
		}
		
	}
	
	public BombData(ResourceLocation key, JsonObject json) {
		super(key, json);
	}
	
	@Override
	public EntityWeapon getShootEntity(WeaponShootParameters params) {
		EntityBomb bomb = (EntityBomb) super.getShootEntity(params);
		if (bomb == null) return null;
		if (params.vehicle != null) {
			bomb.setDeltaMovement(params.vehicle.getDeltaMovement());
		}
		return bomb;
	}
	
	@Override
	public double getMobTurretRange() {
		return 20;
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.BOMB;
	}
	
	@Override
	public <T extends JsonPreset> T copy() {
		return (T) new BombData(getKey(), getJsonData());
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
	
	@Override
	protected Vec3 getStartMove(EntityVehicle vehicle) {
		return vehicle.getDeltaMovement();
	}
	
}
