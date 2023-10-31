package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.client.overlay.components.RadarOverlay;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;

public interface OverlayOnVehicleRenderer<T extends EntityVehicle> extends TextureOnEntityRenderer<T> {
	
	public default void renderOverlayComponents(T vehicle, PoseStack poseStack, 
			MultiBufferSource bufferSource, int packedLight, float partialTicks) {
		poseStack.pushPose();
		// TODO 1.3 render radar and other overlay component's onto the vehicle's dash
		Quaternion q = UtilAngles.lerpQ(partialTicks, vehicle.getPrevQ(), vehicle.getClientQ());
        poseStack.mulPose(q);
        
		renderTexture(vehicle, RadarOverlay.RADAR, poseStack, bufferSource, packedLight, 
				Vec3.ZERO, 1, 1, 0, 0, 0);
		renderTexture(vehicle, RadarOverlay.RADAR, poseStack, bufferSource, packedLight, 
				Vec3.ZERO, 1, 1, 45, 90, 0);
		
		poseStack.popPose();
	}
	
}
