package com.onewhohears.dscombat.data.weapon;

import java.util.List;

import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.PositionMissile;
import com.onewhohears.dscombat.util.UtilEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PosMissileData extends MissileData {

	public PosMissileData(CompoundTag tag) {
		super(tag);
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = super.write();
		return tag;
	}
	
	public PosMissileData(FriendlyByteBuf buffer) {
		super(buffer);
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.POS_MISSILE;
	}
	
	@Override
	public WeaponData copy() {
		return new PosMissileData(this.write());
	}
	
	@Override
	public EntityWeapon getEntity(Level level, Entity owner) {
		return new PositionMissile(level, owner, this);
	}
	
	@Override
	public EntityWeapon getShootEntity(Level level, Entity owner, Vec3 direction, EntityAircraft vehicle) {
		PositionMissile missile = (PositionMissile) super.getShootEntity(level, owner, direction, vehicle);
		if (missile == null) return null;
		missile.targetPos = UtilEntity.getLookingAtBlockPos(owner, 1000);
		return missile;
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		list.add(2, new ComponentColor(new TextComponent("TARGETS POSITION"), 0xaaaa00));
		return list;
	}

}
