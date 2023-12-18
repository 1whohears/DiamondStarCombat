package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.client.renderer.EntityScreenRenderer;

import net.minecraft.world.phys.Vec3;

public class EntityScreenData {
	
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
