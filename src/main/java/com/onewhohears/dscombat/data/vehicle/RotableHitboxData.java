package com.onewhohears.dscombat.data.vehicle;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.world.phys.Vec3;

public class RotableHitboxData {
	
	public static JsonObject createHitboxJson(String name, double sizeX, double sizeY, double sizeZ, 
			double posX, double posY, double posZ) {
		JsonObject hitbox = new JsonObject();
		hitbox.addProperty("name", name);
		UtilParse.writeVec3(hitbox, "size", new Vec3(sizeX, sizeY, sizeZ));
		UtilParse.writeVec3(hitbox, "rel_pos", new Vec3(posX, posY, posZ));
		return hitbox;
	}
	
	private final String name;
	private final Vec3 size;
	private final Vec3 rel_pos;
	
	public RotableHitboxData(JsonObject json) {
		name = json.get("name").getAsString();
		size = UtilParse.readVec3(json, "size");
		rel_pos = UtilParse.readVec3(json, "rel_pos");
	}
	
	public String getName() {
		return name;
	}

	public Vec3 getSize() {
		return size;
	}

	public Vec3 getRelPos() {
		return rel_pos;
	}
	
}
