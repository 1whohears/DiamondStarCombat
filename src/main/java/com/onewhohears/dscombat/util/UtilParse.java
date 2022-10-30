package com.onewhohears.dscombat.util;

import java.io.DataInputStream;
import java.util.zip.GZIPInputStream;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.parts.SeatData;
import com.onewhohears.dscombat.data.parts.WeaponPartData;
import com.onewhohears.dscombat.data.weapon.BombData;
import com.onewhohears.dscombat.data.weapon.BulletData;
import com.onewhohears.dscombat.data.weapon.MissileData;
import com.onewhohears.dscombat.data.weapon.WeaponData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.phys.Vec3;

public class UtilParse {
	
	public static CompoundTag getComoundFromResource(String path) {
		CompoundTag compound;
        DataInputStream dis;
        try {
            dis = new DataInputStream(new GZIPInputStream(UtilParse.class.getResourceAsStream(path)));
            compound = NbtIo.read(dis);
            dis.close();
        }
        catch (Exception e) {
        	System.out.println("ERROR: COULD NOT PARSE COMPOUNDTAG "+path);
            e.printStackTrace();
        	return new CompoundTag();
        }
        return compound;
	}
	
	public static void writeVec3(CompoundTag tag, Vec3 v, String name) {
		tag.putDouble(name+"x", v.x);
		tag.putDouble(name+"y", v.y);
		tag.putDouble(name+"z", v.z);
	}
	
	public static Vec3 readVec3(CompoundTag tag, String name) {
		double x, y, z;
		x = tag.getDouble(name+"x");
		y = tag.getDouble(name+"y");
		z = tag.getDouble(name+"z");
		return new Vec3(x, y, z);
	}
	
	@Nullable
	public static PartData parsePartFromCompound(CompoundTag tag) {
		if (tag == null) return null;
		if (tag.isEmpty()) return null;
		if (!tag.contains("type")) return null;
		PartType type = PartType.values()[tag.getInt("type")];
		switch (type) {
		case SEAT:
			return new SeatData(tag);
		case TURRENT:
			return null;
		case WEAPON_RACK:
			return new WeaponPartData(tag);
		}
		return null;
	}
	
	@Nullable
	public static WeaponData parseWeaponFromCompound(CompoundTag tag) {
		if (tag == null) return null;
		if (tag.isEmpty()) return null;
		if (!tag.contains("type")) return null;
		int index = tag.getInt("type");
		WeaponData.WeaponType type = WeaponData.WeaponType.values()[index];
		switch (type) {
		case BOMB:
			return new BombData(tag);
		case BULLET:
			return new BulletData(tag);
		case ROCKET:
			return new MissileData(tag);
		}
		return null;
	}
	
}
