package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.client.renderer.MultiBufferSource;

public interface OverlayOnVehicleRenderer<T extends EntityVehicle> extends TextureOnEntityRenderer<T> {
	
	public default void renderOverlayComponents(EntityVehicle vehicle, PoseStack poseStack, 
			MultiBufferSource multiBufferSource, int packedLight, float partialTicks) {
		// TODO 1.3 render radar and other overlay component's onto the vehicle's dash
	}
	
}
