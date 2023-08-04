package com.onewhohears.dscombat.data.weapon;

import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.JsonPreset;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityBomb;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
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
	public EntityWeapon getShootEntity(Level level, Entity owner, Vec3 pos, Vec3 direction, @Nullable EntityAircraft vehicle) {
		EntityBomb bomb = (EntityBomb) super.getShootEntity(level, owner, pos, direction, vehicle);
		if (bomb == null) return null;
		if (vehicle != null) {
			bomb.setDeltaMovement(vehicle.getDeltaMovement());
		}
		return bomb;
	}
	
	@Override
	public EntityWeapon getEntity(Level level, Entity owner) {
		return new EntityBomb(level, owner, this);
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
		return WeaponType.BOMB;
	}
	
	@Override
	public <T extends JsonPreset> T copy() {
		return (T) new BombData(getKey(), getJsonData());
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		
		return list;
	}
	
	@Override
	public String getWeaponTypeCode() {
		String code = "B";
		if (isCausesFire()) code += "I";
		return code;
	}
}
