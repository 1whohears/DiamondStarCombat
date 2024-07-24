package com.onewhohears.dscombat.data.vehicle;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.world.phys.Vec3;

public class RotableHitboxData {
	
	public static JsonObject createHitboxJson(String name, double sizeX, double sizeY, double sizeZ, 
			double posX, double posY, double posZ, float max_health, float max_armor, 
			boolean remove_on_destroy, boolean damage_parts, boolean damage_root) {
		JsonObject hitbox = new JsonObject();
		hitbox.addProperty("name", name);
		UtilParse.writeVec3(hitbox, "size", new Vec3(sizeX, sizeY, sizeZ));
		UtilParse.writeVec3(hitbox, "rel_pos", new Vec3(posX, posY, posZ));
		hitbox.addProperty("max_health", max_health);
		hitbox.addProperty("max_armor", max_armor);
		hitbox.addProperty("remove_on_destroy", remove_on_destroy);
		hitbox.addProperty("damage_parts", damage_parts);
		hitbox.addProperty("damage_root", damage_root);
		return hitbox;
	}
	
	private final String name;
	private final Vec3 size, rel_pos;
	private final float max_health, max_armor;
	private final boolean remove_on_destroy, damage_parts, damage_root;
	
	public RotableHitboxData(JsonObject json) {
		name = json.get("name").getAsString();
		size = UtilParse.readVec3(json, "size");
		rel_pos = UtilParse.readVec3(json, "rel_pos");
		max_health = UtilParse.getFloatSafe(json, "max_health", 0);
		max_armor = UtilParse.getFloatSafe(json, "max_armor", 0);
		remove_on_destroy = UtilParse.getBooleanSafe(json, "remove_on_destroy", false);
		damage_parts = UtilParse.getBooleanSafe(json, "damage_parts", false);
		damage_root = UtilParse.getBooleanSafe(json, "damage_root", false);
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

	public float getMaxHealth() {
		return max_health;
	}

	public float getMaxArmor() {
		return max_armor;
	}
	
	public boolean isRemoveOnDestroy() {
		return remove_on_destroy;
	}

	public boolean isDamageParts() {
		return damage_parts;
	}

	public boolean isDamageRoot() {
		return damage_root;
	}
	
}
