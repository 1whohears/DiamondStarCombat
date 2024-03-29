package com.onewhohears.dscombat.util;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.radar.RadarData;
import com.onewhohears.dscombat.data.radar.RadarPresets;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.item.ItemPart;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class UtilParse {
	
	private static final Logger LOGGER = LogUtils.getLogger();
	public static final Gson GSON = new Gson();
	
	public static CompoundTag getComoundFromResource(String path) {
		CompoundTag compound;
        DataInputStream dis;
        try {
            dis = new DataInputStream(new GZIPInputStream(getResourceAsStream(path)));
            compound = NbtIo.read(dis);
            dis.close();
        } catch (Exception e) {
        	LOGGER.error("ERROR: COULD NOT PARSE COMPOUNDTAG "+path);
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
	
	public static JsonObject getJsonFromResource(Resource resource) {
		JsonObject json;
		try {
			BufferedReader br = resource.openAsReader();
			json = GSON.fromJson(br, JsonObject.class);
			br.close();
		} catch (Exception e) {
			LOGGER.error("ERROR: COULD NOT PARSE JSON "+resource.sourcePackId());
			e.printStackTrace();
			return new JsonObject();
		}
		return json;
	}
	
	public static JsonObject getJsonFromResource(String path) {
		JsonObject json;
        InputStreamReader isr;
        try {
        	isr = new InputStreamReader(getResourceAsStream(path));
            json = GSON.fromJson(isr, JsonObject.class);
            isr.close();
        } catch (Exception e) {
        	LOGGER.error("ERROR: COULD NOT PARSE JSON "+path);
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
	public static PartData parsePartFromItem(ItemStack stack) {
		//System.out.println("parsePartFromItem = "+stack+" "+stack.getTag());
		if (!(stack.getItem() instanceof ItemPart part)) return null;
		if (stack.hasTag()) return parsePartFromCompound(stack.getTag());
		return part.getPartData();
	}
	
	@Nullable
	public static PartData parsePartFromCompound(CompoundTag tag) {
		//System.out.println("parsePartFromCompound tag = "+tag);
		if (tag == null) return null;
		if (tag.isEmpty()) return null;
		if (!tag.contains("itemid")) return null;
		String itemId = tag.getString("itemid");
		Item item = UtilItem.getItem(itemId, null);
		if (!(item instanceof ItemPart part)) return null;
		if (tag.getBoolean("filled")) return part.getFilledPartData(tag.getString("param"));
		PartData data = part.getPartData();
		boolean old = tag.getInt("parse_version") < PartData.PARSE_VERSION;
		if (old || tag.getBoolean("readnbt")) data.read(tag);
		return data;
	}
	
	@Nullable
	public static WeaponData parseWeaponFromCompound(CompoundTag tag) {
		if (tag == null) return null;
		if (!tag.contains("weaponId")) return null;
		String weaponId = tag.getString("weaponId");
		WeaponData data = WeaponPresets.get().getPreset(weaponId);
		if (data == null) return null;
		data.readNBT(tag);
		return data;
	}
	
	@Nullable
	public static RadarData parseRadarFromCompound(CompoundTag tag) {
		if (tag == null) return null;
		if (!tag.contains("id")) return null;
		String id = tag.getString("id");
		RadarData data = RadarPresets.get().getPreset(id);
		if (data == null) return null;
		data.readNBT(tag);
		return data;
	}

	public static float fixFloatNbt(CompoundTag nbt, String tag, CompoundTag presetNbt, float min) {
		if (nbt.contains(tag)) {
			float f = nbt.getFloat(tag);
			if (f > min) return f;
		} 
		float nbtf = presetNbt.getFloat(tag);
		nbt.putFloat(tag, nbtf);
		return nbtf;
	}

	public static float fixFloatNbt(CompoundTag nbt, String tag, float alt) {
		if (!nbt.contains(tag)) {
			nbt.putFloat(tag, alt);
			return alt;
		}
		return nbt.getFloat(tag);
	}
	
	public static String prettyVec3(Vec3 v) {
		return String.format("[%3.1f,%3.1f,%3.1f]", v.x, v.y, v.z);
	}
	
	public static String prettyVec3(Vec3 v, int decimals) {
		String f = "%3."+decimals+"f";
		return String.format("["+f+","+f+","+f+"]", v.x, v.y, v.z);
	}
	
	public static String getRandomString(String[]... arrays) {
		int size = 0;
		for (int i = 0; i < arrays.length; ++i) size += arrays[i].length;
		int k = 0, r = (int)(Math.random()*size);
		for (int i = 0; i < arrays.length; ++i) 
			for (int j = 0; j < arrays[i].length; ++j) 
				if (k++ == r) return arrays[i][j];
		return "";
	}
	
	/**
	 * @param weights this array must be the same size as arrays
	 * @param arrays
	 * @return a random string in arrays
	 */
	public static String getRandomString(int[] weights, String[]... arrays) {
		if (weights.length != arrays.length) return "";
		int size = 0;
		for (int i = 0; i < arrays.length; ++i) size += arrays[i].length * weights[i];
		int k = 0, r = (int)(Math.random()*size);
		for (int i = 0; i < arrays.length; ++i) 
			for (int w = 0; w < weights[i]; ++w)
				for (int j = 0; j < arrays[i].length; ++j) 
					if (k++ == r) return arrays[i][j];
		return "";
	}
	
	public static boolean getBooleanSafe(JsonObject json, String name, boolean alt) {
		if (!json.has(name)) return alt;
		return json.get(name).getAsBoolean();
	}
	
	public static int getIntSafe(JsonObject json, String name, int alt) {
		if (!json.has(name)) return alt;
		return json.get(name).getAsInt();
	}
	
	public static float getFloatSafe(JsonObject json, String name, float alt) {
		if (!json.has(name)) return alt;
		return json.get(name).getAsFloat();
	}
	
	public static String getStringSafe(JsonObject json, String name, String alt) {
		if (!json.has(name)) return alt;
		return json.get(name).getAsString();
	}
	
	public static String toColorString(Color color) {
		return Integer.toHexString(0xFF000000 | color.getRGB()).substring(2);
	}
	
	public static Vec3 readVec3(JsonObject json, String name) {
		if (!json.has(name)) return Vec3.ZERO;
		JsonObject vec = json.get(name).getAsJsonObject();
		double x = 0, y = 0, z = 0;
		if (vec.has("x")) x = vec.get("x").getAsDouble();
		if (vec.has("y")) y = vec.get("y").getAsDouble();
		if (vec.has("z")) z = vec.get("z").getAsDouble();
		return new Vec3(x, y, z);
	}
	
	public static void writeVec3(JsonObject json, String name, Vec3 vec) {
		JsonObject v = new JsonObject();
		v.addProperty("x", vec.x);
		v.addProperty("y", vec.y);
		v.addProperty("z", vec.z);
		json.add(name, v);
	}
	
	public static String vec2ToString(Vec2... v) {
		String r = "";
		for (int i = 0; i < v.length; ++i) r += "["+v[i].x+","+v[i].y+"]";
		return r;
	}
	
}
