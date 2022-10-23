package com.onewhohears.dscombat.util;

import java.io.DataInputStream;
import java.util.zip.GZIPInputStream;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.SeatData;
import com.onewhohears.dscombat.data.parts.PartData.PartType;

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
	
	public static PartData parsePartFromCompound(CompoundTag tag) {
		PartType type = PartType.values()[tag.getInt("type")];
		switch (type) {
		case SEAT:
			return new SeatData(tag);
		case TURRENT:
			return null;
		}
		return null;
	}
	
}
