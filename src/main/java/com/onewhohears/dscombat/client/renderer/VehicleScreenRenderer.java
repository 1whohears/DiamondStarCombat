package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.client.renderer.MultiBufferSource;

public interface VehicleScreenRenderer<T extends EntityVehicle> extends EntityScreenRenderer<T> {
	
	public boolean shouldRenderScreens(T vehicle);
	
	public void renderVehicleScreens(T vehicle, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float partialTicks);
	
}
