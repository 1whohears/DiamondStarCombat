package com.onewhohears.dscombat.data.weapon;

import java.util.List;

import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityAbstractWeapon;
import com.onewhohears.dscombat.entity.weapon.PositionMissile;
import com.onewhohears.dscombat.util.UtilEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
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
	public EntityAbstractWeapon getEntity(Level level, Entity owner) {
		return new PositionMissile(level, owner, this);
	}
	
	@Override
	public EntityAbstractWeapon getShootEntity(Level level, Entity owner, Vec3 direction, EntityAbstractAircraft vehicle) {
		PositionMissile missile = (PositionMissile) super.getShootEntity(level, owner, direction, vehicle);
		if (missile == null) return null;
		missile.targetPos = UtilEntity.getLookingAtBlockPos(owner, 1000);
		return missile;
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		list.add(2, new ComponentColor(Component.literal("TARGETS POSITION"), 0xaaaa00));
		if (getFov() != -1) list.add(new ComponentColor(Component.literal("FOV: ").append(getFov()+""), 0x040404));
		list.add(new ComponentColor(Component.literal("Turn Rate: ").append(getMaxRot()+""), 0x040404));
		list.add(new ComponentColor(Component.literal("Acceleration: ").append(getAcceleration()+""), 0x040404));
		return list;
	}

}
