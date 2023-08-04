package com.onewhohears.dscombat.data.weapon;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.JsonPreset;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.IRMissile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class IRMissileData extends MissileData {
	
	private final float flareResistance;
	
	public IRMissileData(ResourceLocation key, JsonObject json) {
		super(key, json);
		flareResistance = json.get("flareResistance").getAsFloat();
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
		return WeaponType.IR_MISSILE;
	}
	
	@Override
	public <T extends JsonPreset> T copy() {
		return (T) new IRMissileData(getKey(), getJsonData());
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
	public EntityWeapon getShootEntity(Level level, Entity owner, Vec3 pos, Vec3 direction, EntityAircraft vehicle) {
		IRMissile missile = (IRMissile) super.getShootEntity(level, owner, pos, direction, vehicle);
		if (missile == null) return null;
		return missile;
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		list.add(2, new ComponentColor(Component.literal("TARGETS FLYING"), 0xaaaa00));
		list.add(3, new ComponentColor(Component.literal("IR GUIDED"), 0xaaaa00));
		if (getFlareResistance() != 0) if (getFov() != -1) list.add(new ComponentColor(Component.literal("Flare Resistance: ").append(getFlareResistance()+""), 0x040404));
		return list;
	}
	
	@Override
	public String getWeaponTypeCode() {
		return "AAIR";
	}

}
