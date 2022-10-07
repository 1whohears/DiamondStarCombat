package com.onewhohears.dscombat.data;

import java.nio.charset.Charset;

import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public abstract class PartData {
	
	private String id;
	private Vec3 pos;
	private EntityAbstractAircraft parent;
	
	public static enum PartType {
		SEAT,
		TURRENT
	}
	
	protected PartData(String id, Vec3 pos) {
		this.id = id;
		this.pos = pos;
	}
	
	public PartData(CompoundTag tag) {
		id = tag.getString("id");
		double x, y, z;
		x = tag.getDouble("posx");
		y = tag.getDouble("posy");
		z = tag.getDouble("posz");
		pos = new Vec3(x, y, z);
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("type", this.getType().ordinal());
		tag.putString("id", getId());
		tag.putDouble("posx", getRelativePos().x);
		tag.putDouble("posy", getRelativePos().y);
		tag.putDouble("posz", getRelativePos().z);
		return tag;
	}
	
	public PartData(FriendlyByteBuf buffer) {
		// type int is read in DataSerializers
		int idLength = buffer.readInt();
		id = buffer.readCharSequence(idLength, Charset.defaultCharset()).toString();
		double x, y, z;
		x = buffer.readDouble();
		y = buffer.readDouble();
		z = buffer.readDouble();
		pos = new Vec3(x, y, z);
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.getType().ordinal());
		buffer.writeInt(getId().length());
		buffer.writeCharSequence(getId(), Charset.defaultCharset());
		buffer.writeDouble(getRelativePos().x);
		buffer.writeDouble(getRelativePos().y);
		buffer.writeDouble(getRelativePos().z);
	}
	
	public abstract PartType getType();
	
	public String getId() {
		return id;
	}
	
	public Vec3 getRelativePos() {
		return pos;
	}
	
	public EntityAbstractAircraft getParent() {
		return parent;
	}
	
	public void setup(EntityAbstractAircraft craft) {
		System.out.println("setting up part "+getId()+" client side "+craft.level.isClientSide);
		parent = craft;
	}
	
	public void clientSetup(EntityAbstractAircraft craft) {
		System.out.println("setting up part "+getId()+" client side "+craft.level.isClientSide);
		parent = craft;
	}
	
	@Override
	public String toString() {
		return "["+getType().name()+"@"+getId()+"]";
	}
	
}
