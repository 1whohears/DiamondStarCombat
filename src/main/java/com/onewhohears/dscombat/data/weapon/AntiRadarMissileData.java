package com.onewhohears.dscombat.data.weapon;

import java.util.List;

import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.weapon.AntiRadarMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AntiRadarMissileData extends MissileData {

	public AntiRadarMissileData(CompoundTag tag) {
		super(tag);
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = super.write();
		
		return tag;
	}
	
	public AntiRadarMissileData(FriendlyByteBuf buffer) {
		super(buffer);
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.ANTIRADAR_MISSILE;
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		list.add(2, new ComponentColor(Component.literal("TARGETS GROUNDED"), 0xaaaa00));
		list.add(3, new ComponentColor(Component.literal("ANTI-RADAR GUIDED"), 0xaaaa00));
		return list;
	}
	
	@Override
	public WeaponData copy() {
		return new AntiRadarMissileData(this.write());
	}
	
	@Override
	public EntityWeapon getEntity(Level level, Entity owner) {
		return new AntiRadarMissile(level, owner, this);
	}
	
	@Override
	public EntityWeapon getShootEntity(Level level, Entity owner, Vec3 direction, EntityAircraft vehicle) {
		AntiRadarMissile missile = (AntiRadarMissile) super.getShootEntity(level, owner, direction, vehicle);
		if (missile == null) return null;
		return missile;
	}

}
