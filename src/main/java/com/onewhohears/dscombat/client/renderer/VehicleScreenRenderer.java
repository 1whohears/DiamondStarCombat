package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

public interface VehicleScreenRenderer<T extends EntityVehicle> extends EntityScreenRenderer<T> {
	
	public default boolean shouldRenderScreens(T vehicle) {
		Minecraft m = Minecraft.getInstance();
		if (m.player == null) return false;
		if (vehicle.getVehicleScreens().length == 0) return false;
		Entity seat = vehicle.getPilotSeat();
		if (seat == null) return false;
		return m.player.distanceToSqr(seat) < 64;
	}
	
	public default void renderVehicleScreens(T vehicle, PoseStack poseStack, 
			MultiBufferSource buffer, int packedLight, float partialTicks) {
		poseStack.pushPose();
		
		Quaternion q = UtilAngles.lerpQ(partialTicks, vehicle.getPrevQ(), vehicle.getClientQ());
        poseStack.mulPose(q);
        
        for (EntityScreenData screen : vehicle.getVehicleScreens()) {
        	renderScreen(vehicle, screen.instanceId, screen.type, 
        			poseStack, buffer, partialTicks, packedLight, 
        			screen.rel_pos, screen.width, screen.height, 
        			screen.xRot, screen.yRot, screen.zRot);
        }
		
		poseStack.popPose();
	}
	
}
