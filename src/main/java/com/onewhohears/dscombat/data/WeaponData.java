package com.onewhohears.dscombat.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public abstract class WeaponData {
	
	public static enum WeaponType {
		BULLET,
		ROCKET,
		BOMB
	}
	
	protected WeaponData() {
	}
	
	public WeaponData(CompoundTag tag) {
	}
	
	public abstract CompoundTag write();
	
	public WeaponData(FriendlyByteBuf buffer) {
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.getType().ordinal());
	}
	
	public abstract WeaponData copy();
	
	public abstract WeaponType getType();
	
	public abstract float getDamage();
	
	public abstract double getSpeed();
	
}
