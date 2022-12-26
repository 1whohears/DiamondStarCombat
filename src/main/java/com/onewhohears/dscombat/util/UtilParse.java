package com.onewhohears.dscombat.util;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.zip.GZIPInputStream;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.parts.BuffData;
import com.onewhohears.dscombat.data.parts.EngineData;
import com.onewhohears.dscombat.data.parts.ExternalEngineData;
import com.onewhohears.dscombat.data.parts.FlareDispenserData;
import com.onewhohears.dscombat.data.parts.FuelTankData;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.parts.RadarPartData;
import com.onewhohears.dscombat.data.parts.SeatData;
import com.onewhohears.dscombat.data.parts.TurretData;
import com.onewhohears.dscombat.data.parts.WeaponPartData;
import com.onewhohears.dscombat.data.parts.WeaponRackData;
import com.onewhohears.dscombat.data.weapon.BombData;
import com.onewhohears.dscombat.data.weapon.BulletData;
import com.onewhohears.dscombat.data.weapon.IRMissileData;
import com.onewhohears.dscombat.data.weapon.PosMissileData;
import com.onewhohears.dscombat.data.weapon.TrackMissileData;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.item.ItemPart;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilParse {
	
	public static final Gson gson = new Gson();
	
	public static CompoundTag getComoundFromResource(String path) {
		CompoundTag compound;
        DataInputStream dis;
        try {
            dis = new DataInputStream(new GZIPInputStream(getResourceAsStream(path)));
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
	
	public static CompoundTag getCompoundFromJson(JsonObject json) {
		return JsonToNBTUtil.getTagFromJson(json);
	}
	
	public static CompoundTag getCompoundFromJsonResource(String path) {
		return getCompoundFromJson(getJsonFromResource(path));
	}
	
	public static JsonObject getJsonFromResource(String path) {
		JsonObject json;
        InputStreamReader isr;
        try {
        	isr = new InputStreamReader(getResourceAsStream(path));
            json = gson.fromJson(isr, JsonObject.class);
            isr.close();
        }
        catch (Exception e) {
        	System.out.println("ERROR: COULD NOT PARSE JSON "+path);
            e.printStackTrace();
        	return new JsonObject();
        }
        return json;
	}

	private static InputStream getResourceAsStream(String resource) {
	    return UtilParse.class.getResourceAsStream(resource);
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
		if (!tag.contains("type")) {
			if (!tag.contains("itemid")) return null;
			String itemId = tag.getString("itemid");
			Item item;
			try {
				item = ForgeRegistries.ITEMS.getDelegate(
					new ResourceLocation(itemId)).get().get();
			} catch(NoSuchElementException e) {
				return null;
			}
			if (!(item instanceof ItemPart part)) return null;
			if (tag.getBoolean("filled")) return part.getFilledPartData(tag.getString("param"));
			return part.getPartData();
		}
		PartType type = PartType.values()[tag.getInt("type")];
		switch (type) {
		case SEAT:
			return new SeatData(tag);
		case TURRENT:
			return new TurretData(tag);
		case WEAPON_RACK:
			return new WeaponRackData(tag);
		case INTERNAL_WEAPON:
			return new WeaponPartData(tag);
		case ENGINE:
			return new EngineData(tag);
		case FUEL_TANK:
			return new FuelTankData(tag);
		case INTERNAL_RADAR:
			return new RadarPartData(tag);
		case FLARE_DISPENSER:
			return new FlareDispenserData(tag);
		case EXTERNAL_ENGINE:
			return new ExternalEngineData(tag);
		case BUFF_DATA:
			return new BuffData(tag);
		}
		return null;
	}
	
	@Nullable
	public static WeaponData parseWeaponFromCompound(CompoundTag tag) {
		if (tag == null) return null;
		if (tag.isEmpty()) return null;
		if (!tag.contains("type")) {
			String preset = tag.getString("preset");
			if (preset.isEmpty()) return null;
			CompoundTag data = WeaponPresets.getNbtById(preset);
			if (data == null) return null;
			tag.merge(data);
		}
		int index = tag.getInt("type");
		WeaponData.WeaponType type = WeaponData.WeaponType.values()[index];
		switch (type) {
		case BOMB:
			return new BombData(tag);
		case BULLET:
			return new BulletData(tag);
		case IR_MISSILE:
			return new IRMissileData(tag);
		case POS_MISSILE:
			return new PosMissileData(tag);
		case TRACK_MISSILE:
			return new TrackMissileData(tag);
		}
		return null;
	}
	
}
