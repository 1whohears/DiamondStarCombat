package com.onewhohears.dscombat.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.parts.EngineData;
import com.onewhohears.dscombat.data.parts.FuelTankData;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.parts.RadarPartData;
import com.onewhohears.dscombat.data.parts.SeatData;
import com.onewhohears.dscombat.data.parts.WeaponPartData;
import com.onewhohears.dscombat.data.parts.WeaponRackData;
import com.onewhohears.dscombat.data.weapon.BombData;
import com.onewhohears.dscombat.data.weapon.BulletData;
import com.onewhohears.dscombat.data.weapon.MissileData;
import com.onewhohears.dscombat.data.weapon.WeaponData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.phys.Vec3;

public class UtilParse {
	
	public static final Gson gson = new Gson();
	
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
	
	public static CompoundTag getCompoundFromJson(JsonObject json) {
		return JsonToNBTUtil.getTagFromJson(json);
	}
	
	public static CompoundTag getCompoundFromJsonResource(String path) {
		return getCompoundFromJson(getJsonFromResource(path));
	}
	
	public static JsonObject getJsonFromResource(String path) {
		JsonObject json;
        //DataInputStream dis;
        InputStreamReader isr;
        try {
            //dis = new DataInputStream(new GZIPInputStream(UtilParse.class.getResourceAsStream(path)));
        	//isr = new InputStreamReader(dis, "UTF-8");
        	isr = new InputStreamReader(UtilParse.class.getResourceAsStream(path));
            json = gson.fromJson(isr, JsonObject.class);
            //dis.close();
            isr.close();
        }
        catch (Exception e) {
        	System.out.println("ERROR: COULD NOT PARSE JSON "+path);
            e.printStackTrace();
        	return new JsonObject();
        }
        return json;
	}
	
	public static List<JsonObject> getJsonsFromDirectory(String path) {
		List<JsonObject> jsons = new ArrayList<JsonObject>();
		//InputStreamReader isr = new InputStreamReader(UtilParse.class.getResourceAsStream(path));
		//System.out.println(isr);
		/*Path dir;
		try {
			dir = Path.of(UtilParse.class.getResource(path).toURI());
		} catch (URISyntaxException e1) {
			System.out.println("ERROR: COULD NOT PARSE JSONS "+path);
			e1.printStackTrace();
			return jsons;
		}
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.json")) {
			for (Path p : stream) {
				BufferedReader buffReader = Files.newBufferedReader(p);
				jsons.add(gson.fromJson(buffReader, JsonObject.class));
			}
			stream.close();
		} catch (IOException e) {
			System.out.println("ERROR: COULD NOT PARSE JSONS "+path);
			e.printStackTrace();
		}*/
		return jsons;
	}
	
	/**
	 * https://stackoverflow.com/questions/3923129/get-a-list-of-resources-from-classpath-directory
	 */
	public List<String> getResourceFiles(String path) throws IOException {
	    List<String> filenames = new ArrayList<>();
	    try (InputStream in = getResourceAsStream(path);
	            BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
	        String resource;
	        while ((resource = br.readLine()) != null) {
	            filenames.add(resource);
	        }
	    }
	    return filenames;
	}

	private InputStream getResourceAsStream(String resource) {
	    final InputStream in
	            = getContextClassLoader().getResourceAsStream(resource);

	    return in == null ? getClass().getResourceAsStream(resource) : in;
	}

	private ClassLoader getContextClassLoader() {
	    return Thread.currentThread().getContextClassLoader();
	}
	
	public static List<CompoundTag> getCompoundsFromJsonDirectory(String path) {
		List<JsonObject> jsons = getJsonsFromDirectory(path);
		List<CompoundTag> compounds = new ArrayList<CompoundTag>();
		for (JsonObject j : jsons) compounds.add(getCompoundFromJson(j));
		return compounds;
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
			return new WeaponRackData(tag);
		case INTERNAL_WEAPON:
			return new WeaponPartData(tag);
		case ENGINE:
			return new EngineData(tag);
		case FUEL_TANK:
			return new FuelTankData(tag);
		case INTERNAL_RADAR:
			return new RadarPartData(tag);
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
