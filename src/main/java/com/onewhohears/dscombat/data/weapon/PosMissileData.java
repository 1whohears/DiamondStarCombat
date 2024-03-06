package com.onewhohears.dscombat.data.weapon;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.JsonPreset;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.PositionMissile;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class PosMissileData extends MissileData {

	public PosMissileData(ResourceLocation key, JsonObject json) {
		super(key, json);
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
	}
	
	@Override
	public CompoundTag writeNbt() {
		CompoundTag tag = super.writeNbt();
		return tag;
	}
	
	@Override
	public void readBuffer(FriendlyByteBuf buffer) {
		super.readBuffer(buffer);
	}
	
	@Override
	public void writeBuffer(FriendlyByteBuf buffer) {
		super.writeBuffer(buffer);
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
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		list.add(2, new ComponentColor(UtilMCText.literal("TARGETS POSITION"), 0xaaaa00));
		return list;
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
