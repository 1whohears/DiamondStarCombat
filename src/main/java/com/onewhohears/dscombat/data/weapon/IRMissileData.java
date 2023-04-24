package com.onewhohears.dscombat.data.weapon;

import java.util.List;

import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.IRMissile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class IRMissileData extends MissileData {
	
	private float flareResistance;
	
	public IRMissileData(CompoundTag tag) {
		super(tag);
		flareResistance = tag.getFloat("flareResistance");
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putFloat("flareResistance", flareResistance);
		return tag;
	}
	
	public IRMissileData(FriendlyByteBuf buffer) {
		super(buffer);
		flareResistance = buffer.readFloat();
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeFloat(flareResistance);
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.IR_MISSILE;
	}
	
	@Override
	public WeaponData copy() {
		return new IRMissileData(this.write());
	}
	
	/**
	 * only matters if this is an IR missile
	 * 0 means immune to flares
	 * 1 means flares effect tracking normally
	 * @return missiles flare resistance
	 */
	public float getFlareResistance() {
		return flareResistance;
	}
	
	@Override
	public EntityWeapon getEntity(Level level, Entity owner) {
		return new IRMissile(level, owner, this);
	}
	
	@Override
	public EntityWeapon getShootEntity(Level level, Entity owner, Vec3 direction, EntityAircraft vehicle) {
		IRMissile missile = (IRMissile) super.getShootEntity(level, owner, direction, vehicle);
		if (missile == null) return null;
		return missile;
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		list.add(2, new ComponentColor(new TextComponent("TARGETS FLYING"), 0xaaaa00));
		list.add(3, new ComponentColor(new TextComponent("IR GUIDED"), 0xaaaa00));
		if (getFlareResistance() != 0) if (getFov() != -1) list.add(new ComponentColor(new TextComponent("Flare Resistance: ").append(getFlareResistance()+""), 0x040404));
		return list;
	}

}
