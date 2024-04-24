package com.onewhohears.dscombat.data.weapon;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPreset;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.TorpedoMissile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class TorpedoData extends TrackMissileData {
	
	public TorpedoData(ResourceLocation key, JsonObject json) {
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
		return WeaponType.TORPEDO;
	}
	
	@Override
	public <T extends JsonPreset> T copy() {
		return (T) new TorpedoData(getKey(), getJsonData());
	}
	
	public TargetType getTargetType() {
		return TargetType.WATER;
	}
	
	@Override
	public EntityWeapon getShootEntity(WeaponShootParameters params) {
		TorpedoMissile missile = (TorpedoMissile) super.getShootEntity(params);
		if (missile == null) return null;
		return missile;
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
