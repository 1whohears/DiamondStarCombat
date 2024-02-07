package com.onewhohears.dscombat.data.aircraft;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.client.renderer.EntityScreenRenderer;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.world.phys.Vec3;

public class EntityScreenData {
	
	public static EntityScreenData getScreenFromJson(JsonObject json) {
		int id = json.get("id").getAsInt();
		Vec3 pos = UtilParse.readVec3(json, "pos");
		float width = json.get("width").getAsFloat();
		float height = json.get("height").getAsFloat();
		Vec3 rot = UtilParse.readVec3(json, "rot");
		return new EntityScreenData(id, pos, width, height, 
				(float)rot.x, (float)rot.y, (float)rot.z);
	}
	
	private static int oldId = 0;
	
	public final int type, instanceId;
	public Vec3 rel_pos;
	public float width, height;
	public float xRot, yRot, zRot;
	
	public EntityScreenData(int type, Vec3 pos, float width, float height, float xRot, float yRot, float zRot) {
		this.type = type;
		this.instanceId = EntityScreenRenderer.getFreeScreenId(oldId);
		oldId = instanceId;
		this.rel_pos = pos;
		this.width = width;
		this.height = height;
		this.xRot = xRot;
		this.yRot = yRot;
		this.zRot = zRot;
	}
	
}
