package com.onewhohears.dscombat.data.weapon;

import java.util.List;

import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.TorpedoMissile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TorpedoData extends TrackMissileData {
	
	public TorpedoData(CompoundTag tag) {
		super(tag);
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = super.write();
		
		return tag;
	}
	
	public TorpedoData(FriendlyByteBuf buffer) {
		super(buffer);
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.TORPEDO;
	}
	
	@Override
	public WeaponData copy() {
		return new TorpedoData(this.write());
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
	public EntityWeapon getShootEntity(Level level, Entity owner, Vec3 direction, EntityAircraft vehicle) {
		TorpedoMissile missile = (TorpedoMissile) super.getShootEntity(level, owner, direction, vehicle);
		if (missile == null) return null;
		return missile;
	}
	
}
