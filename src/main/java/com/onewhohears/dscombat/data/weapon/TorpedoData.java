package com.onewhohears.dscombat.data.weapon;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.TorpedoMissile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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
	public WeaponData copy() {
		return new TorpedoData(getKey(), getJsonData());
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		
		return list;
	}
	
	public TargetType getTargetType() {
		return TargetType.WATER;
	}
	
	@Override
	public EntityWeapon getEntity(Level level, Entity owner) {
		return new TorpedoMissile(level, owner, this);
	}
	
	@Override
	public EntityWeapon getShootEntity(Level level, Entity owner, Vec3 pos, Vec3 direction, EntityAircraft vehicle, boolean ignoreRecoil) {
		TorpedoMissile missile = (TorpedoMissile) super.getShootEntity(level, owner, pos, direction, vehicle, ignoreRecoil);
		if (missile == null) return null;
		return missile;
	}
	
	@Override
	public String getWeaponTypeCode() {
		return "TR";
	}
	
}
