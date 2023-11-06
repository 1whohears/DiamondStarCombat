package com.onewhohears.dscombat.entity.aircraft;

import com.onewhohears.dscombat.client.renderer.VehicleScreenRenderer.VehicleScreenType;

import net.minecraft.world.phys.Vec3;

public class VehicleScreenData {
	
	public final VehicleScreenType type;
	public final Vec3 pos;
	public final float width, height;
	public final float xRot, yRot, zRot;
	
	public VehicleScreenData(VehicleScreenType type, Vec3 pos, float width, float height, float xRot, float yRot, float zRot) {
		this.type = type;
		this.pos = pos;
		this.width = width;
		this.height = height;
		this.xRot = xRot;
		this.yRot = yRot;
		this.zRot = zRot;
	}
	
}
