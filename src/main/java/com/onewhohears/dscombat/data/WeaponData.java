package com.onewhohears.dscombat.data;

import java.nio.charset.Charset;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.entity.weapon.EntityAbstractWeapon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class WeaponData {
	
	private String id;
	private Vec3 pos;
	private int maxAge;
	
	public static enum WeaponType {
		BULLET,
		ROCKET,
		BOMB
	}
	
	protected WeaponData(String id, Vec3 pos, int maxAge) {
		this.id = id;
		this.pos = pos;
		this.maxAge = maxAge;
	}
	
	public WeaponData(CompoundTag tag) {
		id = tag.getString("id");
		double x, y, z;
		x = tag.getDouble("posx");
		y = tag.getDouble("posy");
		z = tag.getDouble("posz");
		pos = new Vec3(x, y, z);
		maxAge = tag.getInt("maxAge");
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("type", this.getType().ordinal());
		tag.putString("id", getId());
		tag.putDouble("posx", getLaunchPos().x);
		tag.putDouble("posy", getLaunchPos().y);
		tag.putDouble("posz", getLaunchPos().z);
		tag.putInt("maxAge", maxAge);
		return tag;
	}
	
	public WeaponData(FriendlyByteBuf buffer) {
		// type int is read in DataSerializers
		int idLength = buffer.readInt();
		id = buffer.readCharSequence(idLength, Charset.defaultCharset()).toString();
		double x, y, z;
		x = buffer.readDouble();
		y = buffer.readDouble();
		z = buffer.readDouble();
		pos = new Vec3(x, y, z);
		maxAge = buffer.readInt();
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.getType().ordinal());
		buffer.writeInt(getId().length());
		buffer.writeCharSequence(getId(), Charset.defaultCharset());
		buffer.writeDouble(getLaunchPos().x);
		buffer.writeDouble(getLaunchPos().y);
		buffer.writeDouble(getLaunchPos().z);
		buffer.writeInt(maxAge);
	}
	
	public abstract WeaponType getType();
	
	public abstract EntityAbstractWeapon shoot(Level level, Entity vehicle, Entity owner, Vec3 direction, Quaternion vehicleQ);
	
	public String getId() {
		return id;
	}
	
	public Vec3 getLaunchPos() {
		return pos;
	}
	
	public int getMaxAge() {
		return maxAge;
	}
	
}
